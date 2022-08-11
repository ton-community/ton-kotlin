package org.ton.tlb

import org.ton.bitstring.BitString

class TlbConstructorTree(
    var root: Node? = null
) {
    fun add(value: AbstractTlbConstructor<*>) {
        root?.add(value) ?: run {
            root = Node(value)
        }
    }

    operator fun get(value: BitString): AbstractTlbConstructor<*>? = root?.get(value)

    class Node(
        val value: AbstractTlbConstructor<*>,
        var left: Node? = null,
        var right: Node? = null
    ) {
        fun add(value: AbstractTlbConstructor<*>) {
            if (value.id < this.value.id) {
                left?.add(value) ?: run {
                    left = Node(value)
                }
            } else {
                right?.add(value) ?: run {
                    right = Node(value)
                }
            }
        }

        operator fun get(key: BitString): AbstractTlbConstructor<*>? {
            val compare = key.compareTo(this.value.id)
            val result = when {
                compare == -1 && left != null -> {
                    left?.get(key)
                }

                compare >= 1 -> {
                    right?.get(key) ?: kotlin.run {
                        val slice = key.slice(0 until value.id.size)
                        if (slice == this.value.id) value else null
                    }
                }

                else -> {
                    val slice = key.slice(0 until value.id.size)
                    if (slice == this.value.id) value else null
                }
            }
            return result
        }
    }
}
