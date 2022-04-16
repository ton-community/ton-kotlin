@file:Suppress("NOTHING_TO_INLINE")

package org.ton.fift

import org.ton.types.Box
import org.ton.types.int257.Int257
import org.ton.types.int257.int257
import ton.types.cell.Cell
import ton.types.cell.CellBuilder
import ton.types.cell.CellSlice

class Stack(
    private val storage: ArrayDeque<Any> = ArrayDeque(),
) : Iterable<Any> {
    val depth: Int get() = storage.size
    val isEmpty: Boolean get() = depth == 0

    override fun iterator(): Iterator<Any> = storage.iterator()

    fun push(stackEntry: Any) {
        when (stackEntry) {
            is Int -> push(int257(stackEntry))
            is Long -> push(int257(stackEntry))
            is Boolean -> push(int257(stackEntry))
            else -> storage.addLast(stackEntry)
        }
    }

    fun pop(): Any = try {
        storage.removeLast()
    } catch (e: NoSuchElementException) {
        throw FiftStackOverflow()
    }

    fun pop(index: Int): Any = try {
        storage.removeAt(storage.lastIndex - index)
    } catch (e: NoSuchElementException) {
        throw FiftStackOverflow()
    }

    operator fun get(index: Int = 0): Any = try {
        storage[storage.lastIndex - index]
    } catch (e: NoSuchElementException) {
        throw FiftStackOverflow()
    }

    operator fun set(index: Int, stackEntry: Any) {
        storage[storage.lastIndex - index] = stackEntry
    }

    fun clear() = storage.clear()

    inline fun swap(firstIndex: Int, secondIndex: Int) {
        val swap = get(firstIndex)
        set(firstIndex, get(secondIndex))
        set(secondIndex, swap)
    }
}

fun Stack.pushArgCount(args: Int) {
    push(args)
    push(NopWordDef)
}

fun Stack.popInt257() = pop() as Int257
fun Stack.popString() = pop() as String
fun Stack.popWordList() = pop() as WordList
fun Stack.popWordDef() = pop() as WordDef
fun Stack.popBox() = pop() as Box
fun Stack.popCell() = pop() as Cell
fun Stack.popCellSlice() = pop() as CellSlice
fun Stack.popCellBuilder() = pop() as CellBuilder
