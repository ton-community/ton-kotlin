package ton.tlb

import ton.cell.CellReader

fun CellReader.unary(param: (TypeExpression) -> Unit = {}): TypeExpression =
    if (readBit()) {
        // unary_succ$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);
        unarySucc(param)
    } else {
        // unary_zero$0 = Unary ~0;
        unaryZero(param)
    }

// unary_succ$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);
fun CellReader.unarySucc(param: (TypeExpression) -> Unit) = type {
    set("x") { unary { set("n", it) } }
    param(get("n") + value(1))
}

// unary_zero$0 = Unary ~0;
fun CellReader.unaryZero(param: (TypeExpression) -> Unit): TypeExpression = value(0).also(param)

fun CellReader.hmLabel(n: (TypeExpression) -> Unit, m: TypeExpression): TypeExpression =
    if (readBit()) {
        if (readBit()) {
            // hml_same$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;
            hml_same(n, m)
        } else {
            // hml_long$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;
            hml_long(n, m)
        }
    } else {
        // hml_short$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;
        hml_short(n, m)
    }

// hml_short$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;
fun CellReader.hml_short(n: (TypeExpression) -> Unit, m: TypeExpression) = type {
    set("len") { unary { set("n", it) } }
    set("s") { bits(get("n")) }
    n(get("n"))
}

// hml_long$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;
fun CellReader.hml_long(n: (TypeExpression) -> Unit, m: TypeExpression) = type {
    set("n", leq(m))
    set("s", bits(get("n")))
    n(get("n"))
}

// hml_same$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;
fun CellReader.hml_same(n: (TypeExpression) -> Unit, m: TypeExpression) = type {
    set("v", bit())
    set("n", leq(m))
    n(get("n"))
}

fun CellReader.HashMap(n: TypeExpression, x: () -> TypeExpression): TypeExpression = hm_edge(n, x)

// hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;
fun CellReader.hm_edge(n: TypeExpression, x: () -> TypeExpression) = type {
    set("label") { hmLabel({ set("l", it) }, n) }
    set("m", value(get("n").toInt() - get("l").toInt()))
    set("node") { HashmapNode(get("m"), x) }
}

fun CellReader.HashmapNode(m: TypeExpression, x: () -> TypeExpression): TypeExpression =
    if (m.toInt() == 0) {
        // hmn_leaf#_ {X:Type} value:X = HashmapNode 0 X;
        hmn_leaf(m, x)
    } else {
        // hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;
        hmn_fork(m, x)
    }

// hmn_leaf#_ {X:Type} value:X = HashmapNode 0 X;
fun CellReader.hmn_leaf(m: TypeExpression, x: () -> TypeExpression) = type {
    set("value", x)
}

// hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;
fun CellReader.hmn_fork(m: TypeExpression, x: () -> TypeExpression) = type {
    set("n", value(m.toInt() - 1))
    set("left") { cellReference { HashMap(get("n"), x) } }
    set("right") { cellReference { HashMap(get("n"), x) } }
}
