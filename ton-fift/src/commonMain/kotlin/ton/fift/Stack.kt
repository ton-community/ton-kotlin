@file:Suppress("NOTHING_TO_INLINE")

package ton.fift

import ton.types.ExceptionCode
import kotlin.jvm.JvmInline

class Stack(
    private val storage: ArrayDeque<StackEntry> = ArrayDeque(),
) : Iterable<StackEntry> {
    val depth: Int get() = storage.size

    override fun iterator(): Iterator<StackEntry> = storage.iterator()

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
fun Stack.push(wordList: WordList) = push(wordList.toStackEntry())
fun Stack.push(wordDef: WordDef) = push(wordDef.toStackEntry())
fun Stack.push(boolean: Boolean) = push(int257(boolean))
fun Stack.pushArgCount(args: Int) {
    push(args)
    push(NopWordDef)
}

fun Stack.popInt257() = (pop() as StackEntry.Integer).value
fun Stack.popString() = (pop() as StackEntry.String).value
fun Stack.popWordList() = (pop() as StackEntry.WordList).value
fun Stack.popWordDef() = (pop() as StackEntry.WordDef).value
fun Stack.pickWordList() = (get(0) as StackEntry.WordList).value

sealed interface StackEntry {
    override fun toString(): kotlin.String

    @JvmInline
    value class Integer(
        val value: Int257,
    ) : StackEntry {
        override fun toString() = value.toString()

        override fun toWordDef(): ton.fift.WordDef = object : ton.fift.WordDef {
            override val isActive: Boolean = false

            override fun execute(fift: FiftInterpretator) {
                fift.stack.push(value)
            }

            override fun toString(): kotlin.String = value.toString()
        }
    }

    @JvmInline
    value class String(
        val value: kotlin.String,
    ) : StackEntry {
        override fun toString() = "\"$value\""

        override fun toWordDef(): ton.fift.WordDef = object : ton.fift.WordDef {
            override val isActive: Boolean = false

            override fun execute(fift: FiftInterpretator) {
                fift.stack.push(value)
            }

            override fun toString(): kotlin.String = "\"$value\""
        }
    }

    @JvmInline
    value class WordDef(
        val value: ton.fift.WordDef,
    ) : StackEntry {
        override fun toString() = value.toString()

        override fun toWordDef(): ton.fift.WordDef = object : ton.fift.WordDef {
            override val isActive: Boolean = false
            override fun execute(fift: FiftInterpretator) {
                fift.stack.push(value)
            }

            override fun toString(): kotlin.String = value.toString()
        }
    }

    @JvmInline
    value class WordList(
        val value: ton.fift.WordList,
    ) : StackEntry {
        override fun toString() = value.toString()

        override fun toWordDef(): ton.fift.WordDef = value.toWordDef()
    }

    fun toWordDef(): ton.fift.WordDef
}

fun Int257.toStackEntry() = StackEntry.Integer(this)
fun String.toStackEntry() = StackEntry.String(this)
fun WordList.toStackEntry() = StackEntry.WordList(this)
fun WordDef.toStackEntry() = StackEntry.WordDef(this)