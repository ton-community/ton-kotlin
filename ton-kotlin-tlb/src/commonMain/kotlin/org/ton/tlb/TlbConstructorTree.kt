package org.ton.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellSlice

class TlbConstructorTree<T>(
    var root: Node<T>? = null
) {
    fun add(key: BitString, value: T) {
        root?.add(key, value) ?: run {
            root = Node(key, value)
        }
    }

    fun find(bits: BitString): Pair<BitString,T>? {
        return root?.get(bits)
    }

    fun values() = root?.values() ?: emptySequence()

    class Node<T>(
        val key: BitString,
        val value: T,
        var left: Node<T>? = null,
        var right: Node<T>? = null
    ) {
        fun values(): Sequence<Pair<BitString, T>> = sequence {
            yield(key to value)
            yieldAll(left?.values() ?: emptySequence())
            yieldAll(right?.values() ?: emptySequence())
        }

        fun add(key: BitString, value: T) {
            if (key < this.key) {
                left?.add(key, value) ?: run {
                    left = Node(key, value)
                }
            } else {
                right?.add(key, value) ?: run {
                    right = Node(key, value)
                }
            }
        }

        operator fun get(key: BitString): Pair<BitString, T>? {
            val compare = key.compareTo(this.key)
            val result = when {
                compare == -1 && left != null -> {
                    left?.get(key)
                }

                compare >= 1 -> {
                    right?.get(key) ?: kotlin.run {
                        val slice = key.slice(0 until this.key.size)
                        if (slice == this.key) slice to value else null
                    }
                }

                else -> {
                    val slice = key.slice(0 until this.key.size)
                    if (slice == this.key) slice to value else null
                }
            }
            return result
        }
    }
}
