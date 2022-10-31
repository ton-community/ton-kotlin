package org.ton.proxy.adnl

import kotlinx.atomicfu.atomic
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class PeerState private constructor(
    val ordinaryHistory: PacketsHistory,
    val priorityHistory: PacketsHistory,
    reinitDate: Instant
) {
    var reinitDate by atomic(reinitDate)

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
