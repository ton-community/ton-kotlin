package ton.tlb.types

interface HashmapTypeFactory : InbuiltTypeFactory {
    // hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;
    fun Hashmap(n: TypeExpression<Int>, x: TypeExpression<*>) = TypeCombinator("Hashmap") {
        TODO()
    }

    // hme_empty$0 {n:#} {X:Type} = HashmapE n X;
    // hme_root$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;
    fun HashmapE(n: TypeExpression<Int>, x: TypeExpression<*>) = TypeCombinator("HashmapE") {
        constructor("hme_empty$0")
        constructor("hme_root$1") {
            field("root", cellReference {
                Hashmap(n, x)
            })
        }
    }
}