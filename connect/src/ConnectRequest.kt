package org.ton.connect

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
public data class Message(
    public val from: String,
    public val message: String,
)

@Serializable
public data class ConnectRequest(
    public val manifestUrl: String,
    public val items: List<ConnectItem>
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("name")
public sealed interface ConnectItem

@Serializable
@SerialName("ton_addr")
public data object TonAddressItem : ConnectItem

@Serializable
@SerialName("ton_proof")
public data class TonProofItem(
    val payload: String
) : ConnectItem

public sealed interface ConnectEvent

@Serializable
@SerialName("connect")
public data class ConnectEventSuccess(
    val id: Int,
    val payload: Payload
) {
    @Serializable
    public data class Payload(
        val items: List<ConnectItemReply>,
        val deviceInfo: DeviceInfo
    )
}

@Serializable
public data class DeviceInfo(
    val platform: String,
    val appName: String,
    val appVersion: String,
    val maxProtocolVersion: Int,
    val features: List<Feature>
)

@Serializable
@JsonClassDiscriminator("name")
public sealed interface Feature {
    @Serializable
    @SerialName("SendTransaction")
    public data class SendTransaction(
        val maxMessages: Int
    )

    @SerialName("SignData")
    @Serializable
    public data object SignData
}

public sealed interface ConnectItemReply

@Serializable
public class TonAddressItemReply(
    public val address: String,
    public val network: Network,
    public val publicKey: String,
    public val walletStateInit: String
) : ConnectItemReply

public sealed interface TonProofItemReply : ConnectItemReply

@Serializable
@SerialName("ton_proof")
public data class TonProofItemReplySuccess(
    val proof: Proof
) : TonProofItemReply {
    @Serializable
    public data class Proof(
        val timestamp: String,
        @Serializable
        val domain: Domain,
        val signature: String,
        val payload: String
    ) {
        @Serializable
        public data class Domain(
            val lengthBytes: Int,
            val value: String
        )
    }
}

@SerialName("ton_addr") // WTF!?!?
public class TonProofItemReplyError(
    public val error: ReplyError
) : TonProofItemReply {
    @Serializable
    public data class ReplyError(
        val code: Int,
        val message: String?
    )
}

@Serializable(Network.Companion::class)
public enum class Network(
    public val value: Int
) {
    MAINNET(-239),
    TESTNET(-3);

    public companion object : KSerializer<Network> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Network", PrimitiveKind.INT)

        override fun deserialize(decoder: Decoder): Network {
            return when (decoder.decodeInt()) {
                MAINNET.value -> MAINNET
                TESTNET.value -> TESTNET
                else -> throw IllegalArgumentException("Unknown network")
            }
        }

        override fun serialize(encoder: Encoder, value: Network) {
            encoder.encodeInt(value.value)
        }
    }
}
