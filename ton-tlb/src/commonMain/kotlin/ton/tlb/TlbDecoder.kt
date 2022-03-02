package ton.tlb

import ton.bitstring.BitString
import ton.bitstring.BitStringReader

data class TlbDecoder(
    val cell: Cell,
    val parent: TlbDecoder? = null,
    val reader: BitStringReader = BitStringReader(cell.data),
    val registeredTypes: MutableMap<String, TypeCombinator> = parent?.registeredTypes ?: HashMap(),
    val values: MutableMap<Field<*>, Any?> = LinkedHashMap(),
) {
    var cellRefPointer = 0

    override fun toString(): String = values.toString()

    interface TypeExpression<T> {
        fun decode(decoder: TlbDecoder): T
    }

    fun any() = AnyType

    object AnyType : TypeExpression<BitString> {
        override fun decode(decoder: TlbDecoder): BitString =
            decoder.reader.readBitString(decoder.reader.bitString.bitSize - decoder.reader.pos)
    }

    fun uint(bitsCount: Int) = UintType(bitsCount)
    fun uint(bitsCount: TypeExpression<Int>) = UintType(bitsCount)
    data class UintType(
        val bitsCount: TypeExpression<Int>,
    ) : TypeExpression<Int> {
        constructor(bitCount: Int) : this(IntConstant(bitCount))

        override fun decode(decoder: TlbDecoder): Int {
            return decoder.reader.readUInt(bitsCount.decode(decoder)).toInt()
        }
    }

    fun int(bitsCount: Int) = IntType(bitsCount)
    fun int(bitsCount: TypeExpression<Int>) = UintType(bitsCount)
    data class IntType(
        val bitsCount: TypeExpression<Int>,
    ) : TypeExpression<Int> {
        constructor(bitCount: Int) : this(IntConstant(bitCount))

        override fun decode(decoder: TlbDecoder): Int {
            return decoder.reader.readUInt(bitsCount.decode(decoder)).toInt()
        }
    }

    fun bool() = BoolType

    object BoolType : TypeExpression<Boolean> {
        override fun decode(decoder: TlbDecoder): Boolean = decoder.reader.readBit()
    }

    fun ulong(bitsCount: Int) = ULongType(bitsCount)
    fun ulong(bitsCount: TypeExpression<Int>) = ULongType(bitsCount)
    data class ULongType(
        val bitsCount: TypeExpression<Int>,
    ) : TypeExpression<Long> {
        constructor(bitsCount: Int) : this(IntConstant(bitsCount))

        override fun decode(decoder: TlbDecoder): Long {
            return decoder.reader.readULong(bitsCount.decode(decoder)).toLong()
        }
    }

    data class IntConstant(
        val value: Int,
    ) : TypeExpression<Int> {
        override fun decode(decoder: TlbDecoder): Int = value
    }

    operator fun TypeExpression<Int>.times(other: TypeExpression<Int>) = object : TypeExpression<Int> {
        override fun decode(decoder: TlbDecoder): Int =
            this@times.decode(decoder) * other.decode(decoder)
    }

    operator fun TypeExpression<Int>.times(other: Int) = object : TypeExpression<Int> {
        override fun decode(decoder: TlbDecoder): Int =
            this@times.decode(decoder) * other
    }

    operator fun Int.times(other: TypeExpression<Int>) = object : TypeExpression<Int> {
        override fun decode(decoder: TlbDecoder): Int =
            this@times * other.decode(decoder)
    }

    fun bitString(bitsCount: TypeExpression<Int>) = BitStringType(bitsCount)
    fun bitString(bitsCount: Int) = BitStringType(bitsCount)
    data class BitStringType(
        val bitsCount: TypeExpression<Int>,
    ) : TypeExpression<BitString> {
        constructor(bitsCount: Int) : this(IntConstant(bitsCount))

        override fun decode(decoder: TlbDecoder): BitString =
            decoder.reader.readBitString(bitsCount.decode(decoder))
    }

    fun <T> cellReference(typeExpression: TlbDecoder.() -> TypeExpression<T>) = CellReference(typeExpression)
    class CellReference<T>(
        val decoderContext: TlbDecoder.() -> TypeExpression<T>,
    ) : TypeExpression<T> {
        override fun decode(decoder: TlbDecoder): T {
            val cell = decoder.cell.references[decoder.cellRefPointer++]
            val cellDecoder = TlbDecoder(cell, decoder)
            println("Start decoding cell: $cell")
            val result = decoderContext(cellDecoder).decode(cellDecoder)
            decoder.values.putAll(cellDecoder.values)
            println("Stop decoding cell: $cell result: $result")
            return result
        }
    }

    data class Field<T>(
        val name: String,
        val type: TypeExpression<T>,
    ) : TypeExpression<T> {
        private var cachedValue: T? = null

        override fun decode(decoder: TlbDecoder): T =
            cachedValue ?: (decoder.values[this] as T).also {
                cachedValue = it
            }

        override fun toString(): String = name
    }

    data class Constructor(
        val name: String,
        val prefix: BitString = BitString(0),
        val fields: MutableList<Field<*>> = ArrayList(),
    ) : TypeExpression<Constructor> {
        override fun decode(decoder: TlbDecoder) = apply {
            fields.forEach { field ->
                println("Start decode field $field")
                val result = field.type.decode(decoder)
                decoder.values[field] = result
                println("End decode field $field - result: $result")
            }
        }

        fun <T> field(name: String, type: TypeExpression<T>): Field<T> = Field(name, type).also {
            fields.add(it)
        }

        override fun toString(): String = name
    }

    data class TypeCombinator(
        val name: String,
        val constructors: MutableMap<BitString, Constructor> = HashMap(),
        val arguments: List<TypeExpression<*>> = emptyList(),
    ) : TypeExpression<Pair<Constructor, Map<Field<*>, Any?>>> {
        override fun decode(decoder: TlbDecoder): Pair<Constructor, Map<Field<*>, Any?>> {
            println("Start decoding combinator: $name - ${constructors.map { it.key.toString(true) to it.value }}")
//            println("reader: ${decoder.reader}")
            var constructorPrefix = BitString(0)
            var matchedConstructor: Constructor? = null
            while (constructorPrefix.bitSize <= UInt.SIZE_BITS) {
                matchedConstructor = constructors[constructorPrefix]
                println("Try to match prefix: ${constructorPrefix.toString(true)} result: $matchedConstructor")
                if (matchedConstructor == null) {
                    constructorPrefix = BitString(constructorPrefix.bitSize + 1) {
                        writeBitString(constructorPrefix)
                        writeBit(decoder.reader.readBit())
                    }
                } else break
            }
            checkNotNull(matchedConstructor) { "Constructor not found for ${constructorPrefix.toString(true)}" }
            println("Matched constructor: ${matchedConstructor.name}")
//            println("reader: ${decoder.reader}")
            val contextDecoder = TlbDecoder(decoder.cell, decoder, decoder.reader)
            matchedConstructor.decode(contextDecoder)
            println("End decoding combinator: $name")
            return matchedConstructor to contextDecoder.values
        }

        fun constructor(name: String, prefix: BitString = BitString(0), builder: Constructor.() -> Unit = {}) =
            Constructor(name, prefix).apply(builder).also {
                constructors[it.prefix] = it
            }

        operator fun invoke(vararg typeExpression: TypeExpression<*>): TypeCombinator {
            return TypeCombinator(name,
                arguments = typeExpression.toList(),
                constructors = constructors.asSequence().map { (key, constructor) ->
                    key to constructor.copy(fields = constructor.fields.asSequence().map { field ->
                        val type = field.type
                        if (type is CombinatorArgument) {
                            Field(name, typeExpression[type.index])
                        } else {
                            field
                        }
                    }.toMutableList())
                }.toMap().toMutableMap())
        }

        private data class CombinatorArgument<T>(
            val index: Int,
        ) : TypeExpression<T> {
            override fun decode(decoder: TlbDecoder) = throw RuntimeException("Not mapped argument")
        }

        fun <T> argument(index: Int): TypeExpression<T> = CombinatorArgument(index)
    }

    fun typeCombinator(name: String, builder: TypeCombinator.() -> Unit) = TypeCombinator(name).apply(builder).also {
        registeredTypes[it.name] = it
    }


    fun decode(typeCombinatorName: String) = registeredTypes[typeCombinatorName]?.decode(this)
}
