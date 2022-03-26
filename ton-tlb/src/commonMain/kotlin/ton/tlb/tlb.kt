package ton.tlb

import ton.bitstring.toInt
import ton.cell.CellReader
import ton.cell.slice

data class Field(
    val name: String,
    val typeExpression: TypeExpression,
) : TypeExpression by typeExpression {

    override fun toString(): String = buildString {
        if (name != "_") {
            append(name)
            append('=')
        }
        append(typeExpression)
    }
}

interface TypeExpression {
    val value: Any
    val fields: MutableList<Field>

    operator fun contains(name: String) = fields.any { it.name == name }
    operator fun get(name: String) = fields.find { it.name == name }!!
    operator fun set(name: String, typeExpression: TypeExpression) {
        if (contains(name)) {
            val oldField = get(name)
            val index = fields.indexOf(oldField)
            fields[index] = oldField.copy(typeExpression = typeExpression)
        } else {
            fields.add(Field(name, typeExpression))
        }
    }

    operator fun set(name: String, typeExpression: () -> TypeExpression) {
        val value = object : TypeExpressionImpl() {
            val typeValue_ by lazy { typeExpression() }
            override val value: Any
                get() = typeValue_.value
            override val fields: MutableList<Field>
                get() = typeValue_.fields
        }
        set(name, value)
        value.typeValue_
    }

    fun toInt(): Int = value as Int
}

abstract class TypeExpressionImpl : TypeExpression {
    override val value: Any = 0
    override val fields: MutableList<Field> = ArrayList()

    override fun toString(): String {
        return if (fields.isNotEmpty()) {
            fields.joinToString(prefix = "(", postfix = ")")
        } else {
            value.toString()
        }
    }
}

operator fun TypeExpression.plus(other: TypeExpression) = object : TypeExpressionImpl() {
    override val value: Int get() = this@plus.toInt() + other.toInt()
}

fun value(value: Any = 0) = object : TypeExpressionImpl() {
    override val value: Any = value
}

fun value(fields: List<Field>) = object : TypeExpressionImpl() {
    override val fields: MutableList<Field> = fields.toMutableList()
}

fun CellReader.bit() = object : TypeExpressionImpl() {
    override val value: Any get() = readBit().toInt()
}

fun CellReader.bits(typeExpression: TypeExpression) = object : TypeExpressionImpl() {
    override val value: Any get() = readBitString(typeExpression.toInt())
}

fun CellReader.leq(typeExpression: TypeExpression) = object : TypeExpressionImpl() {
    override val value: Any get() = readUInt(typeExpression.toInt()).toInt()
}

fun CellReader.cellReference(typeExpression: CellReader.() -> TypeExpression) = object : TypeExpressionImpl() {
    val type = typeExpression(readCell().slice())
    override val value: Any get() = type.value
    override val fields: MutableList<Field> get() = type.fields
}

fun CellReader.unary(param: (TypeExpression) -> Unit = {}): TypeExpression {
    return if (readBit()) {
        unarySucc(param)
    } else {
        unaryZero(param)
    }
}

fun CellReader.unarySucc(param: (TypeExpression) -> Unit) = object : TypeExpressionImpl() {
    init {
        set("x") { unary { set("n", it) } }
        param(get("n") + value(1))
    }
}

fun CellReader.unaryZero(param: (TypeExpression) -> Unit): TypeExpression = value(0).also(param)

fun CellReader.hmLabel(n: (TypeExpression) -> Unit, m: TypeExpression): TypeExpression {
    return if (readBit()) {
        if (readBit()) { // hml_same$11 {m:#} v:Bit n:(#<= m) = HmLabel ~n m;
            hml_same(n, m)
        } else { // hml_long$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;
            hml_long(n, m)
        }
    } else { // hml_short$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;
        hml_short(n, m)
    }
}

// hml_short$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;
fun CellReader.hml_short(n: (TypeExpression) -> Unit, m: TypeExpression): TypeExpression = object : TypeExpressionImpl() {
    init {
        set("len") { unary { set("n", it) } }
        set("s") { bits(get("n")) }
        n(get("n"))
    }
}

// hml_long$10 {m:#} n:(#<= m) s:(n * Bit) = HmLabel ~n m;
fun CellReader.hml_long(n: (TypeExpression) -> Unit, m: TypeExpression): TypeExpression = object : TypeExpressionImpl() {
    init {
        set("n", leq(m))
        set("s", bits(get("n")))
        n(get("n"))
    }
}

fun CellReader.hml_same(n: (TypeExpression) -> Unit, m: TypeExpression) = object : TypeExpressionImpl() {
    init {
        set("v", bit())
        set("n", leq(m))
        n(get("n"))
    }
}

fun CellReader.HashMap(n: TypeExpression, x: () -> TypeExpression) = hm_edge(n, x)
fun CellReader.hm_edge(n: TypeExpression, x: () -> TypeExpression) = object : TypeExpressionImpl() {
    init {
        set("label") { hmLabel({ set("l", it) }, n) }
        set("m", value(get("n").toInt() - get("l").toInt()))
        set("node") { HashmapNode(get("m"), x) }
    }
}

fun CellReader.HashmapNode(m: TypeExpression, x: () -> TypeExpression): TypeExpressionImpl {
    return if (m.toInt() == 0) {
        hmn_leaf(m, x)
    } else {
        hmn_fork(m, x)
    }
}

fun CellReader.hmn_leaf(m: TypeExpression, x: () -> TypeExpression) = object : TypeExpressionImpl() {
    init {
        set("value", x)
    }
}

fun CellReader.hmn_fork(m: TypeExpression, x: () -> TypeExpression) = object : TypeExpressionImpl() {
    init {
        set("n", value(m.toInt() - 1))
        set("left") { cellReference { HashMap(get("n"), x) } }
        set("right") { cellReference { HashMap(get("n"), x) } }
    }
}
