package ton.tlb.types

import ton.bitstring.BitString
import ton.tlb.TlbDecoder

data class TypeCombinator(
    val name: String,
    val arguments: MutableList<TypeExpression<*>> = ArrayList(),
    val constructors: MutableMap<BitString, TypeNamedConstructor> = HashMap(),
) : TypeExpression<Any?> {
    override fun decode(decoder: TlbDecoder): Any? {
        println("Start decoding combinator: $name - ${constructors.map { it.key.toString(true) to it.value }}")
//            println("reader: ${decoder.reader}")
        var constructorPrefix = BitString(0)
        var matchedTypeConstructor: TypeNamedConstructor? = null
        while (constructorPrefix.bitSize <= UInt.SIZE_BITS) {
            matchedTypeConstructor = constructors[constructorPrefix]
//            println("Try to match prefix: ${constructorPrefix.toString(true)} result: $matchedTypeConstructor")
            if (matchedTypeConstructor == null) {
                constructorPrefix = BitString(constructorPrefix.bitSize + 1) {
                    writeBitString(constructorPrefix)
                    writeBit(decoder.reader.readBit())
                }
            } else break
        }
        checkNotNull(matchedTypeConstructor) { "Constructor not found for ${constructorPrefix.toString(true)}" }
        println("Matched constructor: ${matchedTypeConstructor.name}")
//            println("reader: ${decoder.reader}")
        val result = matchedTypeConstructor.decode(decoder)
        println("End decoding combinator: $name")
        return result.ifEmpty {
            matchedTypeConstructor.name
        }
    }

    private data class CombinatorArgument<T>(
        val index: Int,
    ) : TypeExpression<T> {
        override fun decode(decoder: TlbDecoder) = throw RuntimeException("Not mapped argument")
    }

    fun <T> argument(index: Int): TypeExpression<T> = CombinatorArgument(index)
}

fun TypeCombinator(name: String, builder: TypeCombinator.() -> Unit) = TypeCombinator(name).apply(builder)