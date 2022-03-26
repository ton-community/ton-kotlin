//package ton.tlb.old
//
//import kotlinx.serialization.json.JsonNull
//import kotlinx.serialization.json.JsonObject
//import kotlinx.serialization.json.JsonPrimitive
//import kotlinx.serialization.json.buildJsonObject
//import ton.bitstring.BitString
//import ton.cell.CellReader
//import ton.tlb.old.types.InbuiltTypeFactory
//import ton.tlb.old.types.TonTypeFactory
//import ton.tlb.old.types.TypeCombinator
//
//data class TlbDecoder(
//    val reader: CellReader,
//    val parent: TlbDecoder? = null,
//) : InbuiltTypeFactory, TonTypeFactory {
//    var cellRefPointer = 0
//
//    fun decode(typeCombinator: TypeCombinator) = typeCombinator.decode(this)
//
//    fun decodeToJson(typeCombinator: TypeCombinator) = (decode(typeCombinator) as Map<*, *>).toJson()
//
//    private fun Map<*, *>.toJson(): JsonObject = buildJsonObject {
//        forEach { (field, value) ->
//            val key = field.toString()
//            val element = when (value) {
//                is Map<*, *> -> value.toJson()
//                "bool_true" -> JsonPrimitive(true)
//                "bool_false" -> JsonPrimitive(false)
//                is String -> JsonPrimitive(value)
//                is Number -> JsonPrimitive(value)
//                is BitString -> JsonPrimitive(value.toString())
//                else -> JsonNull
//            }
//            put(key, element)
//        }
//    }
//}
