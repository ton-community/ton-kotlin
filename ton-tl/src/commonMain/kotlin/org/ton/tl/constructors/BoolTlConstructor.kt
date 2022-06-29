package org.ton.tl.constructors

import io.ktor.utils.io.core.*
import org.ton.tl.readTl
import org.ton.tl.writeTl

private enum class Bool(val value: Boolean) {
    TRUE(true),
    FALSE(false);

    companion object {
        operator fun get(value: Boolean) = if (value) TRUE else FALSE
    }
}

private object BoolTlCombinator : EnumTlCombinator<Bool>(
    Bool::class,
    Bool.TRUE to "boolTrue = Bool",
    Bool.FALSE to "boolFalse = Bool"
)

fun Input.readBoolTl() = readTl(BoolTlCombinator).value
fun Output.writeBoolTl(value: Boolean) = writeTl(BoolTlCombinator, Bool[value])
