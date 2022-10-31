@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.providers.TlbConstructorProvider

inline fun SmcCapList(capabilities: Iterable<SmcCapability>) = SmcCapList.of(capabilities)
inline fun SmcCapList(vararg capabilities: SmcCapability) = SmcCapList.of(capabilities.toList())

inline fun Iterable<SmcCapability>.toSmcCapList() = SmcCapList.of(this)
inline fun Array<SmcCapability>.toSmcCapList() = SmcCapList.of(*this)

sealed interface SmcCapList : Iterable<SmcCapability> {
    object Nil : SmcCapList {
        override fun iterator(): Iterator<SmcCapability> = iterator {}
    }

    data class Next(
        val head: SmcCapability,
        val tail: SmcCapList
    ) : SmcCapList {
        override fun iterator(): Iterator<SmcCapability> = iterator {
            yield(head)
            yieldAll(tail)
        }
    }

    companion object : TlbCombinatorProvider<SmcCapList> by SmcCapListTlbCombinator {
        @JvmStatic
        fun of(capability: Iterable<SmcCapability>): SmcCapList =
            capability.reversed().fold(Nil as SmcCapList) { acc, cap ->
                Next(cap, acc)
            }

        @JvmStatic
        fun of(vararg capability: SmcCapability) = of(capability.asIterable())
    }
}

private object SmcCapListTlbCombinator : TlbCombinator<SmcCapList>() {
    private val next by lazy {
        object : TlbConstructor<SmcCapList.Next>(
            schema = "cap_list_next\$1 head:SmcCapability tail:SmcCapList = SmcCapList;"
        ) {
            override fun storeTlb(cellBuilder: CellBuilder, value: SmcCapList.Next) {
                cellBuilder.storeTlb(SmcCapability, value.head)
                cellBuilder.storeTlb(this@SmcCapListTlbCombinator, value.tail)
            }

            override fun loadTlb(cellSlice: CellSlice): SmcCapList.Next {
                val head = cellSlice.loadTlb(SmcCapability)
                val tail = cellSlice.loadTlb(this@SmcCapListTlbCombinator)
                return SmcCapList.Next(head, tail)
            }
        }
    }

    override val constructors: List<TlbConstructor<out SmcCapList>> by lazy {
        listOf(
            ObjectTlbConstructor(
                schema = "cap_list_nil\$0 = SmcCapList;",
                SmcCapList.Nil
            ),
            next
        )
    }
}
