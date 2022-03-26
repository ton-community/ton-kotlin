package ton.tlb

import ton.bitstring.toInt
import ton.cell.Cell
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
    override val value: Any get() {
        val countLeadingZeroBits = typeExpression.toInt().countLeadingZeroBits()
        val bits = UInt.SIZE_BITS - countLeadingZeroBits
        return readUInt(bits).toInt()
    }
}

fun CellReader.les(typeExpression: TypeExpression) = object : TypeExpressionImpl() {
    override val value: Any get() {
        val countLeadingZeroBits = (typeExpression.toInt() - 1).countLeadingZeroBits()
        val bits = UInt.SIZE_BITS - countLeadingZeroBits
        return readUInt(bits).toInt()
    }
}

fun CellReader.cellReference(typeExpression: CellReader.() -> TypeExpression) = object : TypeExpressionImpl() {
    val type = typeExpression(readCell().slice())
    override val value: Any get() = type.value
    override val fields: MutableList<Field> get() = type.fields
}

fun CellReader.type(block: TypeExpression.() -> Unit = {}): TypeExpression = object : TypeExpressionImpl() {
    init {
        block(this)
    }
}
