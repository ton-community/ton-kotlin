package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_ADDRESS
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_CONFIRM_SEQNO
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_FROM
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_FROM_SHORT
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_MESSAGE
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_MESSAGES
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_PRIORITY_ADDRESS
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_RECV_ADDR_VERSION
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_RECV_PRIORITY_ADDR_VERSION
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_REINIT_DATE
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_SEQNO
import org.ton.api.adnl.AdnlPacketContents.Companion.FLAG_SIGNATURE
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl
import kotlin.random.Random

// total packet length:
//   for full packet:
//     32 (dst) + 64 (encryption overhead) + 4 (magic) + 36 (pubkey) + 4 + M (sum of messages) +
//              + A1 + A2 + 8 + 8 + 4 + 4 + 4 + 4 + 68 (signature) + 16 (r1) + 16 (r2) =
//              = 272 + M + A1 + A2
//   for channel:
//     32 (channel id) + 32 (encryption overhead) + 4 (magic) + 4 + M (sum of messages) +
//              + A1 + A2 + 8 + 8 + 4 + 4 + 16(r1) + 16(r2) = 128 + M + A1 + A2
@Serializable
data class AdnlPacketContents(
    @Serializable(HexByteArraySerializer::class)
    val rand1: ByteArray,
    val flags: Int,
    val from: PublicKey?,
    val from_short: AdnlIdShort?,
    val message: AdnlMessage?,
    val messages: List<AdnlMessage>?,
    val address: AdnlAddressList?,
    val priority_address: AdnlAddressList?,
    val seqno: Long?,
    val confirm_seqno: Long?,
    val recv_addr_list_version: Int?,
    val recv_priority_addr_list_version: Int?,
    val reinit_date: Int?,
    val dst_reinit_date: Int?,
    @Serializable(HexByteArraySerializer::class)
    override val signature: ByteArray?,
    @Serializable(HexByteArraySerializer::class)
    val rand2: ByteArray
) : SignedTlObject<AdnlPacketContents> {
    constructor(
        from: PublicKey? = null,
        from_short: AdnlIdShort? = null,
        message: AdnlMessage? = null,
        messages: List<AdnlMessage>? = null,
        address: AdnlAddressList? = null,
        priority_address: AdnlAddressList? = null,
        seqno: Long? = null,
        confirm_seqno: Long? = null,
        recv_addr_list_version: Int? = null,
        recv_priority_addr_list_version: Int? = null,
        reinit_date: Int? = null,
        dst_reinit_date: Int? = null,
        signature: ByteArray? = null,
    ) : this(
        rand1 = Random.Default.nextBytes(if (Random.nextBoolean()) 7 else 15),
        flags = (if (from != null) FLAG_FROM else 0) or
                (if (from_short != null) FLAG_FROM_SHORT else 0) or
                (if (message != null) FLAG_MESSAGE else 0) or
                (if (messages != null) FLAG_MESSAGES else 0) or
                (if (address != null) FLAG_ADDRESS else 0) or
                (if (priority_address != null) FLAG_PRIORITY_ADDRESS else 0) or
                (if (seqno != null) FLAG_SEQNO else 0) or
                (if (confirm_seqno != null) FLAG_CONFIRM_SEQNO else 0) or
                (if (recv_addr_list_version != null) FLAG_RECV_ADDR_VERSION else 0) or
                (if (recv_priority_addr_list_version != null) FLAG_RECV_PRIORITY_ADDR_VERSION else 0) or
                (if (reinit_date != null) FLAG_REINIT_DATE else 0) or
                (if (signature != null) FLAG_SIGNATURE else 0),
        from,
        from_short,
        message,
        messages,
        address,
        priority_address,
        seqno,
        confirm_seqno,
        recv_addr_list_version,
        recv_priority_addr_list_version,
        reinit_date,
        dst_reinit_date,
        signature,
        rand2 = Random.Default.nextBytes(if (Random.nextBoolean()) 7 else 15)
    )

    init {
        if (message != null && messages != null) {
            throw IllegalArgumentException("both fields `message` and `messages` set")
        }
        if (from != null && from_short != null && from.toAdnlIdShort() != from_short) {
            throw IllegalArgumentException("`from` and `from_short` mismatch")
        }
//        if (address != null && address.addrs.isEmpty()) {
//            throw IllegalArgumentException("`address` contains empty list")
//        }
        if (priority_address != null && priority_address.addrs.isEmpty()) {
            throw IllegalArgumentException("`priority_address` contains empty list")
        }
    }

    fun messages(): List<AdnlMessage> = message?.let { listOf(it) } ?: messages ?: emptyList()

    override fun signed(privateKey: PrivateKey) =
        copy(
            flags = flags or FLAG_SIGNATURE,
            signature = privateKey.sign(tlCodec().encodeBoxed(this))
        )

    override fun verify(publicKey: PublicKey): Boolean =
        publicKey.verify(
            tlCodec().encodeBoxed(
                copy(
                    flags = flags and FLAG_SIGNATURE.inv(),
                    signature = null
                )
            ), signature
        )

    override fun tlCodec(): TlCodec<AdnlPacketContents> = AdnlPacketContentsTlConstructor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is AdnlPacketContents) return false

        if (reinit_date != other.reinit_date) return false
        if (dst_reinit_date != other.dst_reinit_date) return false
        if (seqno != other.seqno) return false
        if (confirm_seqno != other.confirm_seqno) return false
        if (flags != other.flags) return false
        if (recv_addr_list_version != other.recv_addr_list_version) return false
        if (recv_priority_addr_list_version != other.recv_priority_addr_list_version) return false
        if (signature != null) {
            if (other.signature == null) return false
            if (!signature.contentEquals(other.signature)) return false
        } else if (other.signature != null) return false
        if (!rand1.contentEquals(other.rand1)) return false
        if (!rand2.contentEquals(other.rand2)) return false
        if (from != other.from) return false
        if (from_short != other.from_short) return false
        if (message != other.message) return false
        if (messages != other.messages) return false
        if (address != other.address) return false
        if (priority_address != other.priority_address) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rand1.contentHashCode()
        result = 31 * result + flags
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (from_short?.hashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (messages?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (priority_address?.hashCode() ?: 0)
        result = 31 * result + (seqno?.hashCode() ?: 0)
        result = 31 * result + (confirm_seqno?.hashCode() ?: 0)
        result = 31 * result + (recv_addr_list_version ?: 0)
        result = 31 * result + (recv_priority_addr_list_version ?: 0)
        result = 31 * result + (reinit_date ?: 0)
        result = 31 * result + (dst_reinit_date ?: 0)
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        result = 31 * result + rand2.contentHashCode()
        return result
    }

    companion object : TlCodec<AdnlPacketContents> by AdnlPacketContentsTlConstructor {
        const val FLAG_FROM = 1 shl 0
        const val FLAG_FROM_SHORT = 1 shl 1
        const val FLAG_MESSAGE = 1 shl 2
        const val FLAG_MESSAGES = 1 shl 3
        const val FLAG_ADDRESS = 1 shl 4
        const val FLAG_PRIORITY_ADDRESS = 1 shl 5
        const val FLAG_SEQNO = 1 shl 6
        const val FLAG_CONFIRM_SEQNO = 1 shl 7
        const val FLAG_RECV_ADDR_VERSION = 1 shl 8
        const val FLAG_RECV_PRIORITY_ADDR_VERSION = 1 shl 9
        const val FLAG_REINIT_DATE = 1 shl 10
        const val FLAG_SIGNATURE = 1 shl 11
    }
}

private object AdnlPacketContentsTlConstructor : TlConstructor<AdnlPacketContents>(
    type = AdnlPacketContents::class,
    schema = "adnl.packetContents" +
            " rand1:bytes" +
            " flags:#" +
            " from:flags.0?PublicKey" +
            " from_short:flags.1?adnl.id.short" +
            " message:flags.2?adnl.Message" +
            " messages:flags.3?(vector adnl.Message)" +
            " address:flags.4?adnl.addressList" +
            " priority_address:flags.5?adnl.addressList" +
            " seqno:flags.6?long" +
            " confirm_seqno:flags.7?long" +
            " recv_addr_list_version:flags.8?int" +
            " recv_priority_addr_list_version:flags.9?int" +
            " reinit_date:flags.10?int" +
            " dst_reinit_date:flags.10?int" +
            " signature:flags.11?bytes" +
            " rand2:bytes" +
            " = adnl.PacketContents",
    id = -784151159
) {
    override fun decode(input: Input): AdnlPacketContents {
        val rand1 = input.readBytesTl()
        val flags = input.readIntTl()
        val from = if (flags and FLAG_FROM != 0) input.readTl(PublicKey) else null
        val from_short = if (flags and FLAG_FROM_SHORT != 0) input.readTl(AdnlIdShort) else null
        val message = if (flags and FLAG_MESSAGE != 0) input.readTl(AdnlMessage) else null
        val messages = if (flags and FLAG_MESSAGES != 0) input.readVectorTl(AdnlMessage) else null
        val address = if (flags and FLAG_ADDRESS != 0) input.readTl(AdnlAddressList) else null
        val priority_address = if (flags and FLAG_PRIORITY_ADDRESS != 0) input.readTl(AdnlAddressList) else null
        val seqno = if (flags and FLAG_SEQNO != 0) input.readLongTl() else null
        val confirm_seqno = if (flags and FLAG_CONFIRM_SEQNO != 0) input.readLongTl() else null
        val recv_addr_list_version = if (flags and FLAG_RECV_ADDR_VERSION != 0) input.readIntTl() else null
        val recv_priority_addr_list_version =
            if (flags and FLAG_RECV_PRIORITY_ADDR_VERSION != 0) input.readIntTl() else null
        val reinit_date = if (flags and FLAG_REINIT_DATE != 0) input.readIntTl() else null
        val dst_reinit_date = if (flags and FLAG_REINIT_DATE != 0) input.readIntTl() else null
        val signature = if (flags and FLAG_SIGNATURE != 0) input.readBytesTl() else null
        val rand2 = input.readBytesTl()
        return AdnlPacketContents(
            rand1,
            flags,
            from,
            from_short,
            message,
            messages,
            address,
            priority_address,
            seqno,
            confirm_seqno,
            recv_addr_list_version,
            recv_priority_addr_list_version,
            reinit_date,
            dst_reinit_date,
            signature,
            rand2
        )
    }

    override fun encode(output: Output, value: AdnlPacketContents) {
        output.writeBytesTl(value.rand1)
        output.writeIntTl(value.flags)
        if (value.flags and FLAG_FROM != 0) output.writeTl(PublicKey, value.from!!)
        if (value.flags and FLAG_FROM_SHORT != 0) output.writeTl(AdnlIdShort, value.from_short!!)
        if (value.flags and FLAG_MESSAGE != 0) output.writeTl(AdnlMessage, value.message!!)
        if (value.flags and FLAG_MESSAGES != 0) output.writeVectorTl(value.messages!!, AdnlMessage)
        if (value.flags and FLAG_ADDRESS != 0) output.writeTl(AdnlAddressList, value.address!!)
        if (value.flags and FLAG_PRIORITY_ADDRESS != 0) output.writeTl(AdnlAddressList, value.priority_address!!)
        if (value.flags and FLAG_SEQNO != 0) output.writeLongTl(value.seqno!!)
        if (value.flags and FLAG_CONFIRM_SEQNO != 0) output.writeLongTl(value.confirm_seqno!!)
        if (value.flags and FLAG_RECV_ADDR_VERSION != 0) output.writeIntTl(value.recv_addr_list_version!!)
        if (value.flags and FLAG_RECV_PRIORITY_ADDR_VERSION != 0) output.writeIntTl(value.recv_priority_addr_list_version!!)
        if (value.flags and FLAG_REINIT_DATE != 0) output.writeIntTl(value.reinit_date!!)
        if (value.flags and FLAG_REINIT_DATE != 0) output.writeIntTl(value.dst_reinit_date!!)
        if (value.flags and FLAG_SIGNATURE != 0) output.writeBytesTl(value.signature!!)
        output.writeBytesTl(value.rand2)
    }
}
