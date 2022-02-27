@file:OptIn(ExperimentalSerializationApi::class)

package ton.tlb

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule
import ton.bitstring.BitString
import ton.bitstring.BitStringSerializer

open class TlbDecoder(
    val tlb: Tlb,
    val bitStringReader: BitStringReader,
    val deserializer: DeserializationStrategy<*>,
) : CompositeDecoder, Decoder {
    override val serializersModule: SerializersModule get() = tlb.serializersModule

    private val decodedValues = HashMap<Int, Any>()
    private var idx = 0
    override fun decodeElementIndex(descriptor: SerialDescriptor) =
        if (descriptor.elementsCount > idx) idx++ else CompositeDecoder.DECODE_DONE

//    override fun beginStructure(descriptor: SerialDescriptor) = when (descriptor.kind) {
//        PolymorphicKind.SEALED -> TlbSealedDecoder(tlb, bitStringReader)
//        StructureKind.OBJECT -> TlbObjectDecoder(tlb, bitStringReader)
//        else -> TODO(descriptor.kind.toString()+" "+descriptor.kind::class.toString())
//    }

    @OptIn(InternalSerializationApi::class)
    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        if (deserializer is SealedClassSerializer<*>) {
            val elementDescriptor = deserializer.descriptor.getElementDescriptor(1)

            val constructors = elementDescriptor.elementDescriptors.asSequence().mapNotNull {
                val tlbConstructorPrefix =
                    it.annotations.asSequence().filterIsInstance<TlbConstructorPrefix>().firstOrNull()
                if (tlbConstructorPrefix != null) {
                    tlbConstructorPrefix.bitPrefix to it
                } else null
            }.sortedBy { it.first.size }

            val prefixConstructor = ArrayList<Boolean>()
            val (_, constructorDescriptor) = constructors.find { (prefix, constructorDescriptor) ->
                while (prefixConstructor.size < prefix.size) {
                    prefixConstructor.add(bitStringReader.readBit())
                }
                prefix.contentEquals(prefixConstructor.toBooleanArray())
            } ?: throw SerializationException("Can't find constructor for $deserializer")

            val ser = deserializer.findPolymorphicSerializer(this, constructorDescriptor.serialName)
            return ser.deserialize(TlbDecoder(tlb, bitStringReader, ser)) as T
        }
        if (deserializer is BitStringSerializer) {
            val annotations = this.deserializer.descriptor.getElementAnnotations(idx - 1)
            val field = annotations.filterIsInstance<TlbBits>().firstOrNull()?.lengthField ?: ""
            val fieldIndex = this.deserializer.descriptor.getElementIndex(field)
            val bits = decodedValues[fieldIndex] as? Int ?: Int.MAX_VALUE
            println("read bit string: $bits")
            return bitStringReader.readBitString(bits) as T
        }

        return super.decodeSerializableValue(deserializer)
    }


    /**
     * Invoked to decode a value when specialized `decode*` method was not overridden.
     */
    open fun decodeValue(): Any = throw SerializationException("${this::class} can't retrieve untyped values")

    override fun decodeNotNullMark(): Boolean = true
    override fun decodeNull(): Nothing? = null
    override fun decodeBoolean(): Boolean = decodeValue() as Boolean
    override fun decodeByte(): Byte = decodeValue() as Byte
    override fun decodeShort(): Short = decodeValue() as Short
    override fun decodeInt(): Int = decodeValue() as Int
    override fun decodeLong(): Long = decodeValue() as Long
    override fun decodeFloat(): Float = decodeValue() as Float
    override fun decodeDouble(): Double = decodeValue() as Double
    override fun decodeChar(): Char = decodeValue() as Char
    override fun decodeString(): String = decodeValue() as String
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeValue() as Int
    override fun decodeInline(inlineDescriptor: SerialDescriptor): Decoder = this

    // overwrite by default
    open fun <T : Any?> decodeSerializableValue(
        deserializer: DeserializationStrategy<T>,
        previousValue: T? = null,
    ): T = decodeSerializableValue(deserializer)

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = this

    override fun endStructure(descriptor: SerialDescriptor) {
    }

    final override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = decodeBoolean()
    final override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = decodeByte()
    final override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = decodeShort()
    final override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int {
        val annotations = descriptor.getElementAnnotations(index)
        val bits = annotations.filterIsInstance<TlbNumber>().firstOrNull()?.bitSize ?: Int.SIZE_BITS
        val int = bitStringReader.readInt(bits)
        println("int: $int bits: $bits")
        decodedValues[index] = int
        return int
    }

    final override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = decodeLong()
    final override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = decodeFloat()
    final override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = decodeDouble()
    final override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = decodeChar()
    final override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String = decodeString()
    fun decodeBitStringElement(descriptor: SerialDescriptor, index: Int): BitString {
        val annotations = descriptor.getElementAnnotations(index)
        val field = annotations.filterIsInstance<TlbBits>().firstOrNull()?.lengthField ?: ""
        val bits = decodedValues[descriptor.getElementIndex(field)] as? Int ?: Int.SIZE_BITS
        return bitStringReader.readBitString(bits)
    }

    final override fun decodeInlineElement(
        descriptor: SerialDescriptor,
        index: Int,
    ): Decoder = decodeInline(descriptor.getElementDescriptor(index))

    final override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?,
    ): T = decodeSerializableValue(deserializer, previousValue)

    final override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?,
    ): T? {
        val isNullabilitySupported = deserializer.descriptor.isNullable
        return if (isNullabilitySupported || decodeNotNullMark()) decodeSerializableValue(deserializer,
            previousValue) else decodeNull()
    }
}