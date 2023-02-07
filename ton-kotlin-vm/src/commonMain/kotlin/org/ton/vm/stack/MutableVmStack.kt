package org.ton.vm.stack

import org.ton.bigint.BigInt
import org.ton.vm.VmStackList
import org.ton.vm.VmStackValue
import org.ton.vm.exception.TvmTypeCheckException
import org.ton.vm.toVmStackList
import kotlin.jvm.JvmStatic

/**
 * Mutable version of [VmStack]
 */
public interface MutableVmStack : VmStack {
    /**
     * Pushes [value] to the stack
     */
    public fun push(value: VmStackValue)

    /**
     * Pop [VmStackValue] from the stack at [index]
     */
    public fun popAt(index: Int): VmStackValue

    /**
     * Pops [VmStackValue] from the stack
     */
    public fun pop(): VmStackValue = popAt(0)

    /**
     * Swaps [first] and [second] elements in the stack
     */
    public fun swap(first: Int = 0, second: Int = 1)

    /**
     * Pops [VmStackValue.VmStkNumber] from the stack
     */
    public fun popNumber(): VmStackValue.VmStkNumber

    /**
     * Pops [BigInt] from the stack
     */
    public fun popBigInt(): BigInt = popNumber().toBigInt()

    /**
     * Pops [Int] from the stack
     */
    public fun popInt(): Int = popNumber().toInt()

    /**
     * Pops [Long] from the stack
     */
    public fun popLong(): Long = popNumber().toLong()

    /**
     * Pops [Boolean] from the stack
     */
    public fun popBoolean(): Boolean = popNumber().toBoolean()

    public companion object {
        /**
         * Creates [MutableVmStack] from [iterable]
         */
        @JvmStatic
        public fun fromIterable(iterable: Iterable<VmStackValue>): MutableVmStack =
            MutableVmStackImpl(iterable)

        /**
         * Creates [MutableVmStack] from [collection]
         */
        @JvmStatic
        public fun fromCollection(collection: Collection<VmStackValue>): MutableVmStack =
            MutableVmStackImpl(collection)

        @JvmStatic
        public fun fromArray(array: Array<out VmStackValue>): MutableVmStack =
            MutableVmStackImpl(array.toList())

        /**
         * Creates [MutableVmStack] with [capacity]
         */
        @JvmStatic
        public fun withCapacity(capacity: Int): MutableVmStack = MutableVmStackImpl(capacity)
    }
}

public inline fun MutableVmStack(iterable: Iterable<VmStackValue>): MutableVmStack =
    MutableVmStack.fromIterable(iterable)

public inline fun MutableVmStack(collection: Collection<VmStackValue>): MutableVmStack =
    MutableVmStack.fromCollection(collection)

public inline fun MutableVmStack(array: Array<out VmStackValue>): MutableVmStack =
    MutableVmStack.fromArray(array)

public inline fun MutableVmStack(capacity: Int): MutableVmStack =
    MutableVmStack.withCapacity(capacity)

public inline fun mutableVmStackOf(vararg elements: VmStackValue): MutableVmStack =
    MutableVmStack.fromArray(elements)

public inline fun Iterable<VmStackValue>.toMutableVmStack(): MutableVmStack =
    MutableVmStack.fromIterable(this)

public inline fun Collection<VmStackValue>.toMutableVmStack(): MutableVmStack =
    MutableVmStack.fromCollection(this)

public inline fun Array<out VmStackValue>.toMutableVmStack(): MutableVmStack =
    MutableVmStack.fromArray(this)

private class MutableVmStackImpl(
    private val _stack: ArrayDeque<VmStackValue>
) : MutableVmStack {
    constructor(initialCapacity: Int) : this(ArrayDeque(initialCapacity))
    constructor(iterable: Iterable<VmStackValue>) : this(16) {
        _stack.addAll(iterable)
    }

    constructor(collection: Collection<VmStackValue>) : this(collection.size) {
        _stack.addAll(collection)
    }

    override val depth: Int get() = _stack.size

    override val stack: VmStackList get() = _stack.toVmStackList()

    override fun get(index: Int): VmStackValue = _stack[_stack.lastIndex - index]

    override fun iterator(): Iterator<VmStackValue> = _stack.iterator()

    override fun swap(first: Int, second: Int) {
        val lastIndex = _stack.lastIndex
        val firstIndex = lastIndex - first
        val secondIndex = lastIndex - second

        println(_stack)

        val tmp = _stack[firstIndex]
        _stack[firstIndex] = _stack[secondIndex]
        _stack[secondIndex] = tmp

        println(_stack)
    }

    override fun popNumber(): VmStackValue.VmStkNumber {
        val value = pop()
        if (value is VmStackValue.VmStkNumber) {
            return value
        } else {
            throw TvmTypeCheckException("Expected number, got $value")
        }
    }

    override fun push(value: VmStackValue) {
        _stack.addLast(value)
    }

    override fun popAt(index: Int): VmStackValue {
        return _stack.removeAt(_stack.lastIndex - index)
    }
}
