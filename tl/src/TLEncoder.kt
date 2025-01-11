import kotlinx.io.*
import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.UByteArraySerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import org.ton.tl.ByteStringBase64Serializer
import org.ton.tl.TL
import org.ton.tl.annotation.TLConditional
import org.ton.tl.annotation.TLFixedSize
import org.ton.tl.annotation.getTlCombinatorId

@OptIn(ExperimentalSerializationApi::class)
public class TLEncoder(
    private val tl: TL,
    private val sink: Sink,
    private val intValuesCache: IntArray,
    private val boxed: Boolean
) : CompositeEncoder, Encoder {

    override val serializersModule: SerializersModule
        get() = tl.serializersModule

    override fun encodeNull() {
        //
    }

    override fun encodeBoolean(value: Boolean): Unit = encodeInt((if (value) 0x997275b5 else 0xbc799737).toInt())
    override fun encodeByte(value: Byte): Unit = encodeInt(value.toInt())
    override fun encodeChar(value: Char): Unit = encodeInt(value.code)
    override fun encodeShort(value: Short): Unit = sink.writeShortLe(value)
    override fun encodeInt(value: Int): Unit = sink.writeIntLe(value)
    override fun encodeLong(value: Long): Unit = sink.writeLongLe(value)
    override fun encodeFloat(value: Float): Unit = encodeInt(value.toBits())
    override fun encodeInline(descriptor: SerialDescriptor): Encoder = this
    override fun encodeDouble(value: Double): Unit = encodeLong(value.toBits())
    override fun encodeString(value: String): Unit = encodeByteArray(value.encodeToByteArray())

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int): Unit =
        encodeInt(enumDescriptor.getTlCombinatorId(index))


    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean): Unit =
        encodeBoolean(value)

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte): Unit =
        encodeByte(value)

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char): Unit =
        encodeChar(value)

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short): Unit =
        encodeShort(value)

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        intValuesCache[index] = value
        encodeInt(value)
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long): Unit =
        encodeLong(value)

    @ExperimentalSerializationApi
    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        val annotations = descriptor.getElementAnnotations(index)
        for (k in annotations.indices) {
            val annotation = annotations[k]
            if (annotation is TLConditional) {
                val elementIndex = descriptor.getElementIndex(annotation.field)
                val flags = intValuesCache[elementIndex]
                if (flags == -1) {
                    error("No value found for `${annotation.field}`")
                }
                if (1 shl annotation.value and flags != 0) {
                    if (value == null) {
                        throw Exception("field `${descriptor.getElementName(index)}` flagged as not-null by '${annotation.field}' field is null")
                    } else {
                        encodeSerializableValue(serializer, value)
                    }
                }
                break
            }
        }
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        when (serializer) {
            ByteArraySerializer() -> {
                val fixedSize = descriptor.getTLFixedSize(index)
                encodeByteArray(value as ByteArray, fixedSize)
            }

            UByteArraySerializer() -> {
                val fixedSize = descriptor.getTLFixedSize(index)
                encodeByteArray((value as UByteArray).asByteArray(), fixedSize)
            }

            ByteStringBase64Serializer -> {
                val fixedSize = descriptor.getTLFixedSize(index)
                encodeByteString(value as ByteString, fixedSize)
            }

            else -> encodeSerializableValue(serializer, value)
        }
    }

    private fun SerialDescriptor.getTLFixedSize(index: Int): Int {
        val annotations = getElementAnnotations(index)
        for (k in annotations.indices) {
            val annotation = annotations[k]
            if (annotation is TLFixedSize) {
                val value = annotation.value
                if (value != -1) {
                    return value
                }
                val field = annotation.field
                val fieldIndex = getElementIndex(field)
                if (fieldIndex == CompositeDecoder.UNKNOWN_NAME) {
                    error("Unknown field '$field'")
                }
                return intValuesCache[fieldIndex]
            }
        }
        return -1
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float): Unit =
        encodeFloat(value)

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double): Unit =
        encodeDouble(value)

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder =
        this

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
//        if (index == 0 && descriptor.kind is PolymorphicKind) {
//            val elementDescriptor = descriptor.getElementDescriptor(1)
//            val index = elementDescriptor.getElementIndex(value)
//            val tlCombinatorId = elementDescriptor.getElementDescriptor(index).getTlCombinatorId()
//            encodeInt(tlCombinatorId)
//        } else {
//            encodeString(value)
//        }
        if (index == 0 && descriptor.kind is PolymorphicKind) {
            return
        }
        encodeString(value)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        when (serializer) {
            ByteArraySerializer() -> encodeByteArray(value as ByteArray)
            UByteArraySerializer() -> encodeByteArray((value as UByteArray).asByteArray())
            ByteStringBase64Serializer -> encodeByteString(value as ByteString)
            else -> serializer.serialize(this, value)
        }
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        encodeInt(collectionSize)
        return beginStructure(descriptor)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        val flagFields = IntArray(descriptor.elementsCount) { -1 }
//        println("begin structure: ${descriptor.kind} | ${descriptor.serialName}")
        var boxed = this.boxed
        when (descriptor.kind) {
            StructureKind.CLASS -> {
                if (boxed) {
                    encodeInt(descriptor.getTlCombinatorId())
                    boxed = false
                }
            }

            StructureKind.OBJECT -> encodeInt(descriptor.getTlCombinatorId())
            PolymorphicKind.SEALED -> {
                boxed = true
            }

            else -> {}
        }
        return TLEncoder(tl, sink, flagFields, boxed)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        // do nothing
    }

    public fun encodeByteArray(value: ByteArray, fixedSize: Int = -1) {
        if (fixedSize > 0) {
            require(value.size == fixedSize) {
                "Expected byte array of size $fixedSize, but got ${value.size}"
            }
            sink.write(value)
        } else {
            encodeBytesPadding(value.size) {
                sink.write(value)
            }
        }
    }

    public fun encodeByteString(value: ByteString, fixedSize: Int = -1) {
        if (fixedSize > 0) {
            require(value.size == fixedSize) {
                "Expected byte string of size $fixedSize, but got ${value.size}"
            }
            sink.write(value)
        } else {
            encodeBytesPadding(value.size) {
                sink.write(value)
            }
        }
    }

    private inline fun encodeBytesPadding(size: Int, block: () -> Unit) {
        var totalLength = size
        if (totalLength < 254) {
            sink.writeUByte(totalLength.toUByte())
            totalLength++
        } else if (totalLength < (1 shl 24)) {
            sink.writeUByte(254u)
            sink.writeUByte((totalLength and 255).toUByte())
            sink.writeUByte(((totalLength shr 8) and 255).toUByte())
            sink.writeUByte((totalLength shr 16).toUByte())
            totalLength += 4
        } else if (totalLength < Int.MAX_VALUE) {
            sink.writeUByte(255u)
            sink.writeUByte((totalLength and 255).toUByte())
            sink.writeUByte(((totalLength shr 8) and 255).toUByte())
            sink.writeUByte(((totalLength shr 16) and 255).toUByte())
            sink.writeUByte(((totalLength shr 24) and 255).toUByte())
            sink.writeByte(0)
            sink.writeByte(0)
            sink.writeByte(0)
            totalLength += 8
        } else {
            error("Too big byte array: $totalLength")
        }
        block()
        while (totalLength++ % 4 > 0) {
            sink.writeByte(0)
        }
    }
}