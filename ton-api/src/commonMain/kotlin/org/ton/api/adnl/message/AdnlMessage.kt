@file:Suppress("OPT_IN_USAGE")

package org.ton.api.adnl.message

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tl.TlCodec
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor

@JsonClassDiscriminator("@type")
sealed interface AdnlMessage {
    companion object :
        TlCodec<AdnlMessage> by AdnlMessageTlCombinator,
        List<TlConstructor<out AdnlMessage>> by AdnlMessageTlCombinator.constructors
}

private object AdnlMessageTlCombinator : TlCombinator<AdnlMessage>(
    AdnlMessageQuery,
    AdnlMessageAnswer,
    AdnlMessageCreateChannel,
    AdnlMessageConfirmChannel,
    AdnlMessageCustom,
    AdnlMessageNop,
    AdnlMessageReinit,
    AdnlMessagePart
)