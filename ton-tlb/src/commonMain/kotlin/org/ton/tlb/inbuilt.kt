package org.ton.tlb

import org.ton.bitstring.toInt
import org.ton.cell.CellReader

fun CellReader.value(value: Int = 0) = TypeExpressionIntConstant(value)

fun CellReader.value(name: String, fields: List<Field>) = TypeExpressionObjectConstant(name, fields.toMutableList())

fun CellReader.bit() = object : TypeExpressionImpl("Bit") {
    override val value by lazy { readBit().toInt() }
}

fun CellReader.bits256() = bits(256)
fun CellReader.bits(bits: Int) = bits(value(bits))
fun CellReader.bits(typeExpression: TypeExpression) = object : TypeExpressionImpl("bits") {
    override val value by lazy { readBitString(typeExpression.toInt()) }
}

fun CellReader.cell() = object : TypeExpressionImpl("cell") {
    override val value by lazy { cell }
}

fun CellReader.int8() = int(8)
fun CellReader.int16() = uint(16)
fun CellReader.int32() = int(32)
fun CellReader.int64() = int(64)
fun CellReader.int(bits: Int) = int(value(bits))
fun CellReader.int(typeExpression: TypeExpression) = object : TypeExpressionImpl("int") {
    override val value by lazy { readInt(typeExpression.toInt()) }
}

fun CellReader.uint8() = uint(8)
fun CellReader.uint16() = uint(16)
fun CellReader.uint32() = uint(32)
fun CellReader.uint64() = uint(64)
fun CellReader.uint(bits: Int) = uint(value(bits))
fun CellReader.uint(typeExpression: TypeExpression) = object : TypeExpressionImpl("uint") {
    override val value by lazy {
        val bits = typeExpression.toInt()
        if (bits > UInt.SIZE_BITS) {
            readULong(bits)
        } else {
            readUInt(bits)
        }
    }
}

fun CellReader.leq(bits: Int) = leq(value(bits))
fun CellReader.leq(typeExpression: TypeExpression) = object : TypeExpressionImpl("#<=") {
    override val value by lazy {
        val countLeadingZeroBits = typeExpression.toInt().countLeadingZeroBits()
        val bits = UInt.SIZE_BITS - countLeadingZeroBits
        readUInt(bits).toInt()
    }
}

fun CellReader.les(bits: Int) = les(value(bits))
fun CellReader.les(typeExpression: TypeExpression) = object : TypeExpressionImpl("#<") {
    override val value by lazy {
        val countLeadingZeroBits = (typeExpression.toInt() - 1).countLeadingZeroBits()
        val bits = UInt.SIZE_BITS - countLeadingZeroBits
        readUInt(bits).toInt()
    }
}

fun CellReader.Any() = type("Any")

fun CellReader.cellReference(typeExpression: CellReader.() -> TypeExpression) = object : TypeExpressionImpl("^") {
    val type by lazy {
        val cell = readCell()
        typeExpression(cell)
    }
    override val value: Any get() = type.value
    override val fields: MutableList<Field> get() = type.fields
}

fun CellReader.type(name: String, block: TypeExpression.() -> Unit): TypeExpression =
    object : TypeExpressionImpl(name) {
        init {
            block(this)
        }
    }

fun CellReader.type(name: String): TypeExpression =
    object : TypeExpressionImpl(name) {
        override val value: Any = name
        override fun toString(): String = name
    }

fun CellReader.Bool(): TypeExpression =
    if (readBit()) {
        bool_true()
    } else {
        bool_false()
    }

// bool_true$1 = Bool;
private fun CellReader.bool_true() = type("bool_true")

// bool_false$0 = Bool;
private fun CellReader.bool_false() = type("bool_false")

fun CellReader.Maybe(x: () -> TypeExpression): TypeExpression =
    if (readBit()) {
        just(x)
    } else {
        nothing(x)
    }

// just$1 {X:Type} value:X = Maybe X;
private fun CellReader.just(x: () -> TypeExpression) = type("just") {
    set("value", x)
}

// nothing$0 {X:Type} = Maybe X;
private fun CellReader.nothing(x: () -> TypeExpression) = type("nothing")

fun CellReader.Either(x: () -> TypeExpression, y: () -> TypeExpression): TypeExpression =
    if (readBit()) {
        right(x, y)
    } else {
        left(x, y)
    }

// right$1 {X:Type} {Y:Type} value:Y = Either X Y;
private fun CellReader.right(x: () -> TypeExpression, y: () -> TypeExpression) = type("right") {
    set("value", y)
}

// left$0 {X:Type} {Y:Type} value:X = Either X Y;
private fun CellReader.left(x: () -> TypeExpression, y: () -> TypeExpression) = type("left") {
    set("value", x)
}

// pair$_ {X:Type} {Y:Type} first:X second:Y = Both X Y;
fun CellReader.Both(x: () -> TypeExpression, y: () -> TypeExpression) = type("pair") {
    set("first", x)
    set("second", y)
}