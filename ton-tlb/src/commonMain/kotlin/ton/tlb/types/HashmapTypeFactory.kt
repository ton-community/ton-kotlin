@file:Suppress("FunctionName")

package ton.tlb.types

interface HashmapTypeFactory : InbuiltTypeFactory {
    // hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;
    fun Hashmap(n: TypeExpression<Int>, x: TypeExpression<*>): TypeCombinator = TypeCombinator("Hashmap") {
        constructor("hm_edge") {
            val l = NegatedTypeExpression()
            field("label", HmLabel(l, n))
            val m = n - l
            field("node", HashmapNode(m, x))
        }
    }

    // hmn_leaf#_ {X:Type} value:X = HashmapNode 0 X;
    // hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;
    fun HashmapNode(n: TypeExpression<Int>, x: TypeExpression<*>): TypeCombinator =
        TypeCombinator("HashmapNode") { decoder ->
            val nValue = n.decode(decoder)
            if (nValue == 0) {
                constructor("hmn_leaf") {
                    field("value", x)
                }
            } else {
                val nn = constant(nValue - 1)
                constructor("hmn_fork") {
                    field("left", cellReference { Hashmap(nn, x) })
                    field("right", cellReference { Hashmap(nn, x) })
                }
            }
        }

    // hml_short$0 {m:#} {n:#} len:(Unary ~n) {n <= m} s:(n * Bit) = HmLabel ~n m;
    // hml_long$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;
    // hml_same$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;
    fun HmLabel(n: NegatedTypeExpression, m: TypeExpression<Int>) = TypeCombinator("HmLabel") {
        constructor("hml_short$0") {
            val nn = NegatedTypeExpression()
            field("len", Unary(nn))
            field("s", bits(nn))
            n(nn)
        }
        constructor("hml_long$10") {
            val nn = field("n", lessThanOrEqualsInt(m))
            field("s", bits(nn))
            n(nn)
        }
        constructor("hml_same$11") {
            field("v", Bit())
            val nn = field("n", lessThanOrEqualsInt(m))
            n(nn)
        }
    }

    // unary_zero$0 = Unary ~0;
    // unary_succ$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);
    fun Unary(n: NegatedTypeExpression): TypeCombinator = TypeCombinator("Unary") {
        constructor("unary_zero$0") {
            n(constant(0))
        }
        constructor("unary_succ$1") {
            val nn = NegatedTypeExpression()
            field("x", Unary(nn))
            n(nn + 1)
        }
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