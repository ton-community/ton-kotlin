package org.ton.proxy.adnl

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.concurrent.atomic.AtomicReference

class PeerState private constructor(
    val ordinaryHistory: PacketsHistory,
    val priorityHistory: PacketsHistory,
    reinitDate: Instant
) {
    private val _reinitDate = AtomicReference(reinitDate)
    var reinitDate
        get() = _reinitDate.get()
        set(value) {
            _reinitDate.set(value)
        }

    companion object {
        fun receiver(reinitDate: Instant = Clock.System.now()) =
            PeerState(
                ordinaryHistory = PacketsHistory.receiver(),
                priorityHistory = PacketsHistory.receiver(),
                reinitDate = reinitDate
            )

        fun sender() = PeerState(
            ordinaryHistory = PacketsHistory.sender(),
            priorityHistory = PacketsHistory.sender(),
            reinitDate = Instant.DISTANT_PAST
        )
    }
}
