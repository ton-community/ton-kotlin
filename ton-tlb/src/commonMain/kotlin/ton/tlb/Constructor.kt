package ton.tlb

import ton.bitstring.BitString

data class Constructor(
    val name: String,
    var beginsWith: BitString,
    val fields: List<Field> = emptyList(),
    val params: List<TypeExpression> = emptyList(),
    val paramNegated: List<Boolean> = emptyList()
) {
//    fun recomputeBeginsWith(): Boolean {
//       fields.forEach { field ->
//           if (!field.implicit && !field.constraint) {
//               val expression = field.type
//               if (expression is ReferenceTypeExpression) {
//                   return@forEach
//               }
//               if (expression !is ApplyTypeExpression) {
//                   return false
//               }
//           }
//       }
//    }
}