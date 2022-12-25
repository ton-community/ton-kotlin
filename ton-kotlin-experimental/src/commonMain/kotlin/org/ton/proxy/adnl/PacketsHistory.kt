package org.ton.proxy.adnl

import kotlinx.atomicfu.atomic

class PacketsHistory private constructor(
    val deliveredSet: ArrayDeque<Long>?,
    seqno: Long,
) {
    private val _seqno = atomic(seqno)
    var seqno
        get() = _seqno.value
        set(value) {
            _seqno.value = value
        }

    fun reset() {
        deliveredSet?.clear()
        seqno = 0
    }

    fun deliverPacket(seqno: Long): Boolean {
        if (deliveredSet == null) {
            val lastSeqno = this.seqno
            if (lastSeqno < seqno) {
                this.seqno = seqno
            }
            return true
        }
        // TODO: use mask for optimization
        if (deliveredSet.contains(seqno)) {
            return false
        }
        deliveredSet.addLast(seqno)
        if (deliveredSet.size == 512) {
            deliveredSet.removeFirst()
        }
        return true
    }

    companion object {
        fun sender() = PacketsHistory(null, 0L)
        fun receiver() = PacketsHistory(ArrayDeque(512), 0L)
    }
}
