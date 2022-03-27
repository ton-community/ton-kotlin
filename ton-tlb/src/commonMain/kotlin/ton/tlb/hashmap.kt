package ton.tlb

import ton.bitstring.toInt
import ton.cell.CellReader

fun CellReader.Unary(param: (TypeExpression) -> Unit = {}): TypeExpression =
    if (readBit()) {
        // unary_succ$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);
        unarySucc(param)
    } else {
        // unary_zero$0 = Unary ~0;
        unary_zero(param)
    }

// unary_succ$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);
private fun CellReader.unarySucc(param: (TypeExpression) -> Unit) = type("unary_succ") {
    set("x") { Unary { set("n") { it } } }
    param(get("n") + value(1))
}

// unary_zero$0 = Unary ~0;
private fun CellReader.unary_zero(param: (TypeExpression) -> Unit): TypeExpression = type("unary_zero") {
    param(value(0))
}

fun CellReader.HmLabel(n: (TypeExpression) -> Unit, m: TypeExpression): TypeExpression =
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
private fun CellReader.hml_short(n: (TypeExpression) -> Unit, m: TypeExpression) = type("hml_short") {
    set("len") { Unary { set("n") { it } } }
    set("s") { bits(get("n")) }
    n(get("n"))
}

// hml_long$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;
private fun CellReader.hml_long(n: (TypeExpression) -> Unit, m: TypeExpression) = type("hml_long") {
    set("n") { leq(m) }
    set("s") { bits(get("n")) }
    n(get("n"))
}

// hml_same$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;
private fun CellReader.hml_same(n: (TypeExpression) -> Unit, m: TypeExpression) = type("hml_same") {
    set("v", ::bit)
    set("n") { leq(m) }
    n(get("n"))
}

fun CellReader.HashMap(n: TypeExpression, x: () -> TypeExpression): TypeExpression = hm_edge(n, x)

// hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;
private fun CellReader.hm_edge(n: TypeExpression, x: () -> TypeExpression) = type("hm_edge") {
    set("label") { HmLabel({ set("l") { it } }, n) }
    set("m") { value(get("n").toInt() - get("l").toInt()) }
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
private fun CellReader.hmn_leaf(m: TypeExpression, x: () -> TypeExpression) = type("hmn_leaf") {
    set("value", x)
}

// hmn_fork#_ {n:#} {X:Type} left:^(Hashmap n X) right:^(Hashmap n X) = HashmapNode (n + 1) X;
private fun CellReader.hmn_fork(m: TypeExpression, x: () -> TypeExpression) = type("hmn_fork") {
    set("n") { value(m.toInt() - 1) }
    set("left") { cellReference { HashMap(get("n"), x) } }
    set("right") { cellReference { HashMap(get("n"), x) } }
}

fun CellReader.HashmapE(n: TypeExpression, x: () -> TypeExpression): TypeExpression {
    return if (readBit()) {
        hme_root(n, x)
    } else {
        hme_empty(n, x)
    }
}

// hme_empty$0 {n:#} {X:Type} = HashmapE n X;
private fun CellReader.hme_empty(n: TypeExpression, x: () -> TypeExpression) = type("hme_empty")

// hme_root$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;
private fun CellReader.hme_root(n: TypeExpression, x: () -> TypeExpression) = type("hme_root") {
    set("root") { cellReference { HashMap(n, x) } }
}