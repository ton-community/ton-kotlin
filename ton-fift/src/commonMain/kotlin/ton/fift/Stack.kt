@file:Suppress("NOTHING_TO_INLINE")

package ton.fift

import ton.types.ExceptionCode
import kotlin.jvm.JvmInline

class Stack(
    val storage: ArrayDeque<StackEntry> = ArrayDeque()
) {
    fun push(stackEntry: StackEntry) {
        storage.addLast(stackEntry)
    }

    fun pop(): StackEntry = try {
        storage.removeLast()
    } catch (e: NoSuchElementException) {
        throw FiftException(ExceptionCode.StackUnderflow)
    }

    fun pop(index: Int): StackEntry = try {
        storage.removeAt(storage.lastIndex - index)
    } catch (e: NoSuchElementException) {
        throw FiftException(ExceptionCode.StackUnderflow)
    }

    operator fun get(index: Int = 0): StackEntry = try {
        storage[storage.lastIndex - index]
    } catch (e: NoSuchElementException) {
        throw FiftException(ExceptionCode.StackUnderflow)
    }

    operator fun set(index: Int, stackEntry: StackEntry) {
        storage[storage.lastIndex - index] = stackEntry
    }

    inline fun swap(firstIndex: Int, secondIndex: Int) {
        val swap = get(firstIndex)
        set(firstIndex, get(secondIndex))
        set(secondIndex, swap)
    }
}

fun Stack.push(int: Int) = push(int.toInt257())
fun Stack.push(long: Long) = push(long.toInt257())
fun Stack.push(int257: Int257) = push(int257.toStackEntry())
fun Stack.push(string: String) = push(string.toStackEntry())
fun Stack.popInt257() = (pop() as StackEntry.Integer).value
fun Stack.popString() = (pop() as StackEntry.String).value

sealed interface StackEntry {
    override fun toString(): kotlin.String

    @JvmInline
    value class Integer(
        val value: Int257
    ) : StackEntry {
        override fun toString() = value.toString()
    }

    @JvmInline
    value class String(
        val value: kotlin.String
    ) : StackEntry {
        override fun toString() = "\"$value\""
    }

    @JvmInline
    value class WordDef(
        val value: ton.fift.WordDef
    ) : StackEntry {
        override fun toString() = value.toString()
    }

    @JvmInline
    value class WordList(
        val value: MutableList<ton.fift.WordDef>
    ) : StackEntry {
        override fun toString() = value.toString()
    }
}

fun Int257.toStackEntry() = StackEntry.Integer(this)
fun String.toStackEntry() = StackEntry.String(this)