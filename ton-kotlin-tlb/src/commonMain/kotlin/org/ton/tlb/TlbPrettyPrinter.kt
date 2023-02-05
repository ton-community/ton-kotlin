package org.ton.tlb

import org.ton.bitstring.BitString
import org.ton.bitstring.Bits256

public class TlbPrettyPrinter(
    private val stringBuilder: StringBuilder = StringBuilder(),
    private var indent: Int = 2
) {
    private var level: Int = 0

    public fun open(msg: String = ""): TlbPrettyPrinter = apply {
        stringBuilder.append('(').append(msg)
        level++
    }

    public fun close(msg: String = ""): TlbPrettyPrinter = apply {
        check(level > 0) { "TlbPrettyPrinter is already closed" }
        level--
        stringBuilder.append(msg).append(')')
    }

    public fun newLine() {
        if (indent > 0) {
            if (level > 0) stringBuilder.appendLine()
            stringBuilder.append(" ".repeat(level * indent))
        }
    }

    public fun field(type: Any?): TlbPrettyPrinter = apply {
        if (type == null) return@apply
        level++
        if (type is TlbObject) {
            newLine()
        }
        stringBuilder.append(" ")
        type(type)
        level--
    }

    public fun field(name: String, type: Any?): TlbPrettyPrinter = apply {
        if (type == null) return@apply
        if (type is TlbObject) {
            newLine()
        }
        stringBuilder.append(' ').append(name).append(':')
        type(type)
    }

    public fun type(type: Any?): TlbPrettyPrinter = apply {
        when (type) {
            null -> return@apply
            is TlbObject -> type.print(this)
            is Boolean -> stringBuilder.append(if (type) 1 else 0)
            is Bits256 -> stringBuilder.append(type.toString())
            is BitString -> stringBuilder.append("x{$type}")
            else -> stringBuilder.append(type)
        }
    }

    public inline fun type(name: String = "", block: TlbPrettyPrinter.() -> Unit): TlbPrettyPrinter = apply {
        open(name).apply(block).close()
    }

    public inline operator fun invoke(block: TlbPrettyPrinter.() -> Unit): TlbPrettyPrinter = apply {
        apply(block)
    }

    override fun toString(): String = stringBuilder.toString()
}

public inline fun tlbPrettyPrinter(block: TlbPrettyPrinter.() -> Unit): String =
    TlbPrettyPrinter().apply(block).toString()

public inline fun tlbPrettyPrinter(name: String, noinline block: TlbPrettyPrinter.() -> Unit): String =
    TlbPrettyPrinter().type(name, block).toString()
