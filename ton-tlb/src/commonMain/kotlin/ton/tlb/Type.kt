package ton.tlb

data class Type(
    val name: String,
    val isProducesNat: Boolean = false,
    var arity: Int = -1,
    val constructors: List<Constructor> = emptyList(),
    var beginsWith: BitPrefix = BitPrefix()
) {
//    fun recomputeBeginsWith(): Boolean {
//        var changes = false
//        constructors.forEach { constructor ->
//            if(constructor.recomputeBeginsWith()) {
//                changes = true
//            }
//        }
//        return changes
//    }
}