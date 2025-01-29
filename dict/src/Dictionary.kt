package org.ton.dict

import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellContext
import org.ton.cell.CellSlice
import org.ton.cell.exception.CellUnderflowException

/**
 * Typed dictionary with fixed length keys.
 */
public class RawDictionary(
    root: Cell?,
    public val keySize: Int,
) {
    public var root: Cell? = root
        private set

    public constructor(
        keySize: Int,
    ) : this(null, keySize)

    public val isEmpty: Boolean get() = root == null

    public fun set(key: BitString, value: CellSlice) {
        dictSet(
            key,
            0,
            key.size,
            value,
            SetMode.Set,
            CellContext.EMPTY,
        )
    }

    private fun dictSet(
        key: BitString,
        startIndex: Int,
        endIndex: Int,
        value: CellSlice,
        mode: SetMode,
        context: CellContext
    ) {
        val root = root
        if (root == null) {
            if (mode == SetMode.Replace) {
                return
            }
            val builder = CellBuilder()
            builder.storeLabel(keySize, key, startIndex, endIndex)
            builder.storeSlice(value)
            this.root = context.finalizeCell(builder)
            return
        }
        var keyOffset: Int = startIndex
        var data = context.loadCell(root)

        val stack = ArrayDeque<Segment>()
        var leaf: Cell
        while (true) {
            var keyLength = endIndex - keyOffset
            val remainingData = data.beginParse()

            val label = readLabel(remainingData, keyLength)
//            println("label: ${label.toBinary()}")
//            println("after label: ${remainingData.data.toBinary()}")
            // todo: WARNING VERY BAD PERFORMANCE, FIX IN FEATURE
            val prefix = key.slice(keyOffset, endIndex).commonPrefixWith(label)

            if (prefix.size == keyLength) {
                if (mode == SetMode.Add) {
                    return
                }
                val builder = CellBuilder()
                builder.storeLabel(keySize, key, startIndex, endIndex)
                builder.storeSlice(value)
                leaf = context.finalizeCell(builder)
                break
            } else if (prefix.size < keyLength) {
                if (prefix.size < label.size) {
                    // have to insert a new node (fork) inside the current edge
                    if (mode == SetMode.Replace) {
                        return
                    }
                    val prevKeyLength = keyLength
                    keyOffset += prefix.size + 1
                    keyLength = endIndex - keyOffset
                    val oldToRight = label[prefix.size]
//                    println("label: ${label.toBinary().substring(prefix.size + 1, label.size)}")
//                    println("key length: $keyLength")
//                    println("rem data: ${remainingData.data.toBinary()}")
                    val left = run {
                        val builder = CellBuilder()
                        builder.storeLabel(keyLength, label, prefix.size + 1, label.size)
                        builder.storeSlice(remainingData)
                        context.finalizeCell(builder)
                    }
                    val right = run {
                        val builder = CellBuilder()
                        builder.storeLabel(keyLength, key, keyOffset, endIndex)
                        builder.storeSlice(value)
                        context.finalizeCell(builder)
                    }
//                    println("tree left: ${left}")
//                    println("data left: ${left.bits.toBinary()}")
//                    println("leaf right: ${right.hash()}")
                    val builder = CellBuilder()
                    builder.storeLabel(prevKeyLength, prefix, 0, prefix.size)
                    if (oldToRight) {
                        builder.storeRef(right)
                        builder.storeRef(left)
                    } else {
                        builder.storeRef(left)
                        builder.storeRef(right)
                    }

                    leaf = context.finalizeCell(builder)
                    break
                }

                if (data.refs.size != 2) {
                    throw CellUnderflowException("Not enough references in fork")
                }
                keyOffset += prefix.size
                val nextBranch = key[keyOffset++]
                val child: Cell = context.loadCell(data.refs[if (nextBranch) 1 else 0])
//                println("child: ${child.hash()}")
                stack.addLast(
                    Segment(
                        data = data,
                        isRightNext = nextBranch,
                        keyBitLength = keyLength
                    )
                )
                data = child
            } else {
                throw RuntimeException("LCP of prefix and key can't be greater than key")
            }
        }

//        println("rebuild by leaf: ${leaf.hash()}")
        // rebuild the tree starting from leaves
        while (stack.isNotEmpty()) {
            val last = stack.removeLast()
            val left: Cell
            val right: Cell
            if (last.isRightNext) {
                left = last.data.refs[0]
                right = leaf
            } else {
                left = leaf
                right = last.data.refs[1]
            }
            val builder = CellBuilder()
            builder.storeBitString(last.data.bits)
            builder.storeRef(left)
            builder.storeRef(right)
            leaf = context.finalizeCell(builder)
        }

        this.root = leaf
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RawDictionary) return false
        return root == other.root
    }

    override fun hashCode(): Int = root.hashCode()

    override fun toString(): String = "RawDictionary(${root?.hash()})"

    private class Segment(
        val data: Cell,
        /**
         * Which branch to take when traversing the tree,
         * `false` - left, `true` - right
         */
        val isRightNext: Boolean,
        val keyBitLength: Int,
    )

//    public class Tlb(
//        public val keySize: Int
//    ) : TlbCodec<Dictionary<*, *>> {
//        override fun loadTlb(slice: CellSlice): Dictionary<*, *> {
//            val root = if (slice.loadBit()) {
//                slice.loadRef()
//            } else {
//                null
//            }
//            return Dictionary<Comparable<Any>, Any>(root, keySize)
//        }
//
//        override fun storeTlb(builder: CellBuilder, value: Dictionary<*, *>) {
//            val root = value.root
//            if (root != null) {
//                builder.storeBit(true)
//                builder.storeRef(root)
//            } else {
//                builder.storeBit(false)
//            }
//        }
//    }
//
//    public companion object {
//        @Suppress("UNCHECKED_CAST")
//        public fun <K : Comparable<K>, V> tlbCodec(keySize: Int): TlbCodec<Dictionary<K, V>> =
//            Tlb(keySize) as TlbCodec<Dictionary<K, V>>
//    }
}

internal fun readLabel(label: CellSlice, keyBitLength: Int): BitString {
    val labelType = label.preloadUInt(2).toInt()
    when (labelType) {
        // hml_short$0 unary_zero$0
        0b00 -> {
            label.skipBits(2)
            return BitString.empty()
        }
        // hml_short$0 unary_succ$1
        0b01 -> {
            label.skipBits(1)
            val len = label.bits.countLeadingBits(fromIndex = label.bitsPosition, bit = true)
            label.skipBits(len + 1)
            return label.loadBits(len)
        }
        // hml_long$10
        0b10 -> {
            label.skipBits(2)
            val len = label.loadUIntLeq(keyBitLength).toInt()
            return label.loadBits(len)
        }
        // hml_same$11
        0b11 -> {
            label.skipBits(2)
            val bits = when (label.loadBit()) {
                true -> BitString.ALL_ONE
                false -> BitString.ALL_ZERO
            }
            val len = label.loadUIntLeq(keyBitLength).toInt()
            return bits.slice(0, len)
        }

        else -> throw IllegalArgumentException("Invalid label type: $labelType")
    }
}

//class RawDictIterator : Iterator<Pair<CellBuilder, CellSlice>> {
//    private val path = ArrayDeque<Fork>()
//    private val cellContext: CellContext
//
//    constructor(root: DataCell, cellContext: CellContext = CellContext.EMPTY) {
//        path.add(Fork(root.asSlice(), root.bits.size, CellBuilder()))
//        this.cellContext = cellContext
//    }
//
//    override fun hasNext(): Boolean = path.isNotEmpty()
//
//    override fun next(): Pair<CellBuilder, CellSlice> {
//        while (true) {
//            val fork = path.removeLastOrNull() ?: throw NoSuchElementException()
//            val label = readLabel(fork.label, fork.bits, fork.key)
//            if (label == fork.bits) {
//                return fork.key to fork.label
//            }
//            for (index in 0 until 2) {
//                val key = fork.key.copy()
//                key.storeBoolean(index == 0)
//                val nextLabel = cellContext.loadCell(fork.label.getReference(index)).asSlice()
//                path.add(Fork(nextLabel, fork.bits - label - 1, key))
//            }
//        }
//    }
//
//    private class Fork(
//        val label: CellSlice,
//        val bits: Int,
//        val key: CellBuilder
//    )
//}
//
//internal class DictIterator(
//    val root: Cell?,
//    val keyBits: Int,
//) : Iterator<Pair<CellBuilder, CellSlice>> {
//    constructor(dictionary: Dictionary<*, *>) : this(dictionary.root, dictionary.keySize)
//
//    val path = ArrayDeque<Fork>()
//    var isValid: Boolean = true
//
//    override fun hasNext(): Boolean = isValid
//
//
//    override fun next(): Pair<CellBuilder, CellSlice> {
//
//        TODO()
//    }
//
//    /**
//     * forkRoute is a bit string of length keyBits that represents the path to the next key.
//     */
//    private fun nextKey(forkRoute: Int): CellBuilder {
////        var n = keyBits
////        var m = 0
////        var route = forkRoute
////        val node = path.lastOrNull()?.let {
////            m = it.position + 1
////            n -= m
////            route = route ushr 1
////            it.next
////        } ?: root ?: throw NoSuchElementException()
////
////        val builder = CellBuilder()
////        while (true) {
////            val nodeSlice = cellContext.loadCell(node).asSlice()
////            val label = readLabel(nodeSlice, n)
////            builder.storeBitString(label)
////            val labelSize = label.size
////            m += labelSize
////            n -= labelSize
////            if (n == 0) {
////                return builder
////            }
////            if (labelSize != 0) {
////                route = route ushr 1
////            }
////            val bit = route and 1
////            val next = nodeSlice.getReference(bit)
////            val alt = nodeSlice.getReference(1 - bit)
////            path.add(Fork(next, alt, bit))
////            builder.storeBoolean(bit != 0)
////            n--
////            route = route ushr 1
////        }
//        TODO()
//    }
//
//    data class Fork(
//        val next: Cell,
//        val alt: Cell,
//        val position: Int
//    )
//
//    enum class Status {
//        Valid,
//        Pruned,
//        NoElements
//    }
//}

private val CellSlice.data get() = bits.slice(bitsPosition, bits.size)