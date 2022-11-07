package org.ton.proxy.adnl

import org.ton.api.adnl.message.*

interface AdnlMessageReceiver {
    fun receiveMessage(message: AdnlMessage) = when (message) {
        is AdnlMessageAnswer -> receiveAnswer(message)
        is AdnlMessageConfirmChannel -> receiveConfirmChannel(message)
        is AdnlMessageCreateChannel -> receiveCreateChannel(message)
        is AdnlMessageCustom -> receiveCustom(message)
        is AdnlMessageNop -> receiveNop(message)
        is AdnlMessagePart -> receivePart(message)
        is AdnlMessageQuery -> receiveQuery(message)
        is AdnlMessageReinit -> receiveReinit(message)
    }

    fun receiveConfirmChannel(message: AdnlMessageConfirmChannel) {

    }

    fun receiveCreateChannel(message: AdnlMessageCreateChannel) {

    }

    fun receiveCustom(message: AdnlMessageCustom) {

    }

    fun receiveAnswer(message: AdnlMessageAnswer) {

    }

    fun receiveQuery(message: AdnlMessageQuery) {

    }

    fun receiveNop(message: AdnlMessageNop) {

    }

    fun receivePart(message: AdnlMessagePart) {

    }

    fun receiveReinit(message: AdnlMessageReinit) {

    }
}
