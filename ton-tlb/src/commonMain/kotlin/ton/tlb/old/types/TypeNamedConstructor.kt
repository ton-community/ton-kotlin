//package ton.tlb.old.types
//
//import ton.bitstring.BitString
//import ton.bitstring.buildBitString
//import ton.tlb.old.TlbDecoder
//
//interface TypeConstructor : TypeExpression<Map<TypeField<*>, Any?>> {
//    val fields: MutableList<TypeField<*>>
//    val negated: MutableList<NegatedTypeExpression>
//    override fun decode(decoder: TlbDecoder): Map<TypeField<*>, Any?> {
//        val fields = fields.toList().associateWith { field ->
//            field.decode(decoder)
//        }
//        negated.forEach {
//
//        }
//        return fields
//    }
//
//    operator fun <T> NegatedTypeExpression.invoke(value: TypeExpression<Int>) {
//        this.value = value
//    }
//}
//
//data class TypeNamedConstructor(
//    val name: String,
//    val prefix: BitString = BitString(0),
//    override val fields: MutableList<TypeField<*>> = ArrayList(),
//    override val negated: MutableList<NegatedTypeExpression> = ArrayList(),
//) : TypeConstructor {
//    override fun toString(): String = name
//}
//
//fun TypeCombinator.constructor(
//    name: String,
//    prefix: BitString? = null,
//    builder: TypeNamedConstructor.() -> Unit = {},
//) {
//    val hashTag = name.split("#")
//    val bitTag = name.split("$")
//    val (newName, newPrefix) = if (hashTag.size >= 2) {
//        val tag = hashTag[1]
//        hashTag[0] to BitString(tag)
//    } else if (bitTag.size >= 2) {
//        val tag = bitTag[1]
//        val bitString = buildBitString {
//            tag.forEach {
//                writeBit(it == '1')
//            }
//        }
//        bitTag[0] to bitString
//    } else {
//        name to (prefix ?: BitString())
//    }
//    constructors[newPrefix] = { TypeNamedConstructor(newName, newPrefix).apply(builder) }
//}
