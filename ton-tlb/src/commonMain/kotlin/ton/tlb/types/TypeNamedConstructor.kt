package ton.tlb.types

import ton.bitstring.BitString
import ton.tlb.TlbDecoder

interface TypeConstructor : TypeExpression<Map<TypeField<*>, Any?>> {
    val fields: MutableList<TypeField<*>>
    override fun decode(decoder: TlbDecoder): Map<TypeField<*>, Any?> =
        fields.toList().associateWith { field ->
            field.decode(decoder)
        }
}

data class TypeNamedConstructor(
    val name: String,
    val prefix: BitString = BitString(0),
    override val fields: MutableList<TypeField<*>> = ArrayList(),
) : TypeConstructor {
    override fun toString(): String = name
}

fun TypeCombinator.constructor(
    name: String,
    prefix: BitString? = null,
    builder: TypeNamedConstructor.() -> Unit = {},
) {
    val hashTag = name.split("#")
    val bitTag = name.split("$")
    val constructor = if (hashTag.size >= 2) {
        val tag = hashTag[1]
        TypeNamedConstructor(hashTag[0], prefix = BitString(tag))
    } else if (bitTag.size >= 2) {
        val tag = bitTag[1]
        val bitString = BitString(tag.length) {
            tag.forEach {
                writeBit(it == '1')
            }
        }
        TypeNamedConstructor(bitTag[0], prefix = bitString)
    } else {
        TypeNamedConstructor(name, prefix = prefix ?: BitString())
    }
    constructor.apply(builder).also {
        constructors[it.prefix] = it
    }
}
