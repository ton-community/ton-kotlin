@file:Suppress("OPT_IN_USAGE")

package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@JsonClassDiscriminator("@type")
sealed interface AdnlMessage {
    companion object : TlCombinator<AdnlMessage>(
        AdnlMessageQuery,
        AdnlMessageAnswer,
        AdnlMessageCreateChannel,
        AdnlMessageConfirmChannel,
        AdnlMessageCustom,
        AdnlMessageNop,
        AdnlMessageReinit,
        AdnlMessagePart
    )
}

@SerialName("adnl.message.createChannel")
@Serializable
data class AdnlMessageCreateChannel(
    @Serializable(Base64ByteArraySerializer::class)
    val key: ByteArray,
    val date: Int
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageCreateChannel

        if (!key.contentEquals(other.key)) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.contentHashCode()
        result = 31 * result + date
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlMessageCreateChannel(key=")
        append(base64(key))
        append(", date=")
        append(date)
        append(")")
    }

    companion object : TlConstructor<AdnlMessageCreateChannel>(
        type = AdnlMessageCreateChannel::class,
        schema = "adnl.message.createChannel key:int256 date:int = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageCreateChannel) {
            output.writeInt256Tl(value.key)
            output.writeIntTl(value.date)
        }

        override fun decode(input: Input): AdnlMessageCreateChannel {
            val key = input.readInt256Tl()
            val date = input.readIntTl()
            return AdnlMessageCreateChannel(key, date)
        }
    }
}

@SerialName("adnl.message.confirmChannel")
@Serializable
data class AdnlMessageConfirmChannel(
    @Serializable(Base64ByteArraySerializer::class)
    val key: ByteArray,
    @SerialName("peer_key")
    @Serializable(Base64ByteArraySerializer::class)
    val peerKey: ByteArray,
    val date: Int
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageConfirmChannel

        if (!key.contentEquals(other.key)) return false
        if (!peerKey.contentEquals(other.peerKey)) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.contentHashCode()
        result = 31 * result + peerKey.contentHashCode()
        result = 31 * result + date
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlMessageConfirmChannel(key=")
        append(base64(key))
        append(", peerKey=")
        append(base64(peerKey))
        append(", date=")
        append(date)
        append(")")
    }

    companion object : TlConstructor<AdnlMessageConfirmChannel>(
        type = AdnlMessageConfirmChannel::class,
        schema = "adnl.message.confirmChannel key:int256 peer_key:int256 date:int = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageConfirmChannel) {
            output.writeInt256Tl(value.key)
            output.writeInt256Tl(value.peerKey)
            output.writeIntTl(value.date)
        }

        override fun decode(input: Input): AdnlMessageConfirmChannel {
            val key = input.readInt256Tl()
            val peerKey = input.readInt256Tl()
            val date = input.readIntTl()
            return AdnlMessageConfirmChannel(key, peerKey, date)
        }
    }
}

@SerialName("adnl.message.custom")
@Serializable
data class AdnlMessageCustom(
    @Serializable(Base64ByteArraySerializer::class)
    val data: ByteArray
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageCustom

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String = buildString {
        append("AdnlMessageCustom(data=")
        append(base64(data))
        append(")")
    }

    companion object : TlConstructor<AdnlMessageCustom>(
        type = AdnlMessageCustom::class,
        schema = "adnl.message.custom data:bytes = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageCustom) {
            output.writeBytesTl(value.data)
        }

        override fun decode(input: Input): AdnlMessageCustom {
            val data = input.readBytesTl()
            return AdnlMessageCustom(data)
        }
    }
}

@SerialName("adnl.message.nop")
@Serializable
class AdnlMessageNop : AdnlMessage {
    companion object : TlConstructor<AdnlMessageNop>(
        type = AdnlMessageNop::class,
        schema = "adnl.message.nop = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageNop) {
        }

        override fun decode(input: Input): AdnlMessageNop = AdnlMessageNop()
    }
}

@SerialName("adnl.message.reinit")
@Serializable
data class AdnlMessageReinit(
    val date: Int
) : AdnlMessage {
    companion object : TlConstructor<AdnlMessageReinit>(
        type = AdnlMessageReinit::class,
        schema = "adnl.message.reinit date:int = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageReinit) {
            output.writeIntTl(value.date)
        }

        override fun decode(input: Input): AdnlMessageReinit {
            val date = input.readIntTl()
            return AdnlMessageReinit(date)
        }
    }
}

@SerialName("adnl.message.query")
@Serializable
data class AdnlMessageQuery(
    @SerialName("query_id")
    @Serializable(Base64ByteArraySerializer::class)
    val queryId: ByteArray,
    @Serializable(Base64ByteArraySerializer::class)
    val query: ByteArray
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageQuery

        if (!queryId.contentEquals(other.queryId)) return false
        if (!query.contentEquals(other.query)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = queryId.contentHashCode()
        result = 31 * result + query.contentHashCode()
        return result
    }

    override fun toString() = buildString {
        append("AdnlMessageQuery(queryId=")
        append(base64(queryId))
        append(", query=")
        append(base64(query))
        append(")")
    }

    companion object : TlConstructor<AdnlMessageQuery>(
        type = AdnlMessageQuery::class,
        schema = "adnl.message.query query_id:int256 query:bytes = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageQuery) {
            output.writeInt256Tl(value.queryId)
            output.writeBytesTl(value.query)
        }

        override fun decode(input: Input): AdnlMessageQuery {
            val queryId = input.readInt256Tl()
            val query = input.readBytesTl()
            return AdnlMessageQuery(queryId, query)
        }
    }
}

@SerialName("adnl.message.answer")
@Serializable
data class AdnlMessageAnswer(
    @SerialName("query_id")
    @Serializable(Base64ByteArraySerializer::class)
    val queryId: ByteArray,
    @Serializable(Base64ByteArraySerializer::class)
    val answer: ByteArray
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageAnswer

        if (!queryId.contentEquals(other.queryId)) return false
        if (!answer.contentEquals(other.answer)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = queryId.contentHashCode()
        result = 31 * result + answer.contentHashCode()
        return result
    }

    override fun toString() = buildString {
        append("AdnlMessageAnswer(queryId=")
        append(base64(queryId))
        append(", answer=")
        append(base64(answer))
        append(")")
    }

    companion object : TlConstructor<AdnlMessageAnswer>(
        type = AdnlMessageAnswer::class,
        schema = "adnl.message.answer query_id:int256 answer:bytes = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageAnswer) {
            output.writeInt256Tl(value.queryId)
            output.writeBytesTl(value.answer)
        }

        override fun decode(input: Input): AdnlMessageAnswer {
            val queryId = input.readInt256Tl()
            val answer = input.readBytesTl()
            return AdnlMessageAnswer(queryId, answer)
        }
    }
}

@SerialName("adnl.message.part")
@Serializable
data class AdnlMessagePart(
    @Serializable(Base64ByteArraySerializer::class)
    val hash: ByteArray,
    @SerialName("total_size")
    val totalSize: Int,
    val offset: Int,
    @Serializable(Base64ByteArraySerializer::class)
    val data: ByteArray
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessagePart

        if (!hash.contentEquals(other.hash)) return false
        if (totalSize != other.totalSize) return false
        if (offset != other.offset) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hash.contentHashCode()
        result = 31 * result + totalSize
        result = 31 * result + offset
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlMessagePart(hash=")
        append(base64(hash))
        append(", totalSize=")
        append(totalSize)
        append(", offset=")
        append(offset)
        append(", data=")
        append(base64(data))
        append(")")
    }

    companion object : TlConstructor<AdnlMessagePart>(
        AdnlMessagePart::class,
        "adnl.message.part hash:int256 total_size:int offset:int data:bytes = adnl.Message;"
    ) {
        override fun decode(
            input: Input
        ): AdnlMessagePart {
            val hash = input.readInt256Tl()
            val totalSize = input.readIntTl()
            val offset = input.readIntTl()
            val data = input.readBytesTl()
            return AdnlMessagePart(hash, totalSize, offset, data)
        }

        override fun encode(output: Output, value: AdnlMessagePart) {
            output.writeInt256Tl(value.hash)
            output.writeIntTl(value.totalSize)
            output.writeIntTl(value.offset)
            output.writeBytesTl(value.data)
        }
    }
}