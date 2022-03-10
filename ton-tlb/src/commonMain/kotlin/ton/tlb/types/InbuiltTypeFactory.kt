package ton.tlb.types

@Suppress("FunctionName")
interface InbuiltTypeFactory {
    // bool_false$0 = Bool;
    // bool_true$1 = Bool;
    fun Bool() = TypeCombinator("Bool") {
        constructor("bool_false$0")
        constructor("bool_true$1")
    }

    // bool_false$0 = BoolFalse;
    fun BoolFalse() = TypeCombinator("BoolFalse") {
        constructor("bool_false$0")
    }

    // bool_true$1 = BoolTrue;
    fun BoolTrue() = TypeCombinator("BoolTrue") {
        constructor("bool_true$1")
    }

    // nothing$0 {X:Type} = Maybe X;
    // just$1 {X:Type} value:X = Maybe X;
    fun Maybe(x: TypeExpression<*>) = TypeCombinator("Maybe") {
        constructor("nothing$0")
        constructor("just$1") {
            field("value", x)
        }
    }

    // left$0 {X:Type} {Y:Type} value:X = Either X Y;
    // right$1 {X:Type} {Y:Type} value:Y = Either X Y;
    fun Either(x: TypeExpression<*>, y: TypeExpression<*>) = TypeCombinator("Either") {
        constructor("left$0") {
            field("value", x)
        }
        constructor("right$1") {
            field("value", y)
        }
    }

    // pair$_ {X:Type} {Y:Type} first:X second:Y = Both X Y;
    fun Both(x: TypeExpression<*>, y: TypeExpression<*>) = TypeCombinator("Both") {
        constructor("pair") {
            field("first", x)
            field("second", y)
        }
    }

    // bit$_ (## 1) = Bit;
    fun Bit() = TypeCombinator("Bit") {
        constructor("bit") {
            field("_", uint(1))
        }
    }
}