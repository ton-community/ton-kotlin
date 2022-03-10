package ton.tlb.types

import ton.bitstring.BitString
import ton.bitstring.buildBitString
import ton.tlb.TlbDecoder

data class TypeCombinator(
    val name: String,
    val builder: TypeCombinator.(TlbDecoder) -> Unit = {},
) : TypeExpression<Any?> {
    val constructors: MutableMap<BitString, () -> TypeNamedConstructor> = HashMap()

    override fun decode(decoder: TlbDecoder): Any? {
        constructors.clear()
        builder(this, decoder)
        println("Start decoding combinator: $name - ${constructors.map { it.key.toString(true) to it.value }}")
//            println("reader: ${decoder.reader}")
        var constructorPrefix = BitString(0)
        var matchedTypeConstructor: (() -> TypeNamedConstructor)? = null
        while (constructorPrefix.size <= UInt.SIZE_BITS) {
            matchedTypeConstructor = constructors[constructorPrefix]
//            println("Try to match prefix: ${constructorPrefix.toString(true)} result: $matchedTypeConstructor")
            if (matchedTypeConstructor == null) {
                constructorPrefix = buildBitString {
                    writeBitString(constructorPrefix)
                    writeBit(decoder.reader.readBit())
                }
            } else break
        }
        checkNotNull(matchedTypeConstructor) { "Constructor not found for ${constructorPrefix.toString(true)}" }
        val constructor = matchedTypeConstructor()
        println("Matched constructor: ${constructor.name}")
//            println("reader: ${decoder.reader}")
        val result = constructor.decode(decoder)
        println("End decoding combinator: $name")
        return result.ifEmpty {
            constructor.name
        }
    }

    private data class CombinatorArgument<T>(
        val index: Int,
    ) : TypeExpression<T> {
        override fun decode(decoder: TlbDecoder) = throw RuntimeException("Not mapped argument")
    }

    fun <T> argument(index: Int): TypeExpression<T> = CombinatorArgument(index)
}