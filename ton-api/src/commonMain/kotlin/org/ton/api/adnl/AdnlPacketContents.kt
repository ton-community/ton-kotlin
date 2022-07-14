package org.ton.api.adnl

import io.ktor.utils.io.core.*
import org.ton.api.adnl.AdnlNodes.Companion.writeBoxedTl
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readFlagTl
import org.ton.tl.writeOptionalTl
import kotlin.random.Random

// total packet length:
//   for full packet:
//     32 (dst) + 64 (encryption overhead) + 4 (magic) + 36 (pubkey) + 4 + M (sum of messages) +
//              + A1 + A2 + 8 + 8 + 4 + 4 + 4 + 4 + 68 (signature) + 16 (r1) + 16 (r2) =
//              = 272 + M + A1 + A2
//   for channel:
//     32 (channel id) + 32 (encryption overhead) + 4 (magic) + 4 + M (sum of messages) +
//              + A1 + A2 + 8 + 8 + 4 + 4 + 16(r1) + 16(r2) = 128 + M + A1 + A2
data class AdnlPacketContents(
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
    val signature: ByteArray?,
    val rand2: ByteArray
) {
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
        (if (from != null) FLAG_FROM else 0) or
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
        if (address != null && address.addrs.isEmpty()) {
            throw IllegalArgumentException("`address` contains empty list")
        }
        if (priority_address != null && priority_address.addrs.isEmpty()) {
            throw IllegalArgumentException("`priority_address` contains empty list")
        }
    }

    fun messages(): List<AdnlMessage> = message?.let { listOf(it) } ?: messages ?: emptyList()

    fun toByteArray(): ByteArray = buildPacket {
        writeBoxedTl(AdnlPacketContents, this@AdnlPacketContents)
    }.readBytes()

    fun signed(privateKey: PrivateKey): AdnlPacketContents {
        return copy(
            flags = flags or FLAG_SIGNATURE,
            signature = privateKey.sign(toByteArray())
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlPacketContents

        if (!rand1.contentEquals(other.rand1)) return false
        if (flags != other.flags) return false
        if (from != other.from) return false
        if (from_short != other.from_short) return false
        if (message != other.message) return false
        if (messages != other.messages) return false
        if (address != other.address) return false
        if (priority_address != other.priority_address) return false
        if (seqno != other.seqno) return false
        if (confirm_seqno != other.confirm_seqno) return false
        if (recv_addr_list_version != other.recv_addr_list_version) return false
        if (recv_priority_addr_list_version != other.recv_priority_addr_list_version) return false
        if (reinit_date != other.reinit_date) return false
        if (dst_reinit_date != other.dst_reinit_date) return false
        if (signature != null) {
            if (other.signature == null) return false
            if (!signature.contentEquals(other.signature)) return false
        } else if (other.signature != null) return false
        if (!rand2.contentEquals(other.rand2)) return false

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
        const val FLAG_FROM = 0x1
        const val FLAG_FROM_SHORT = 0x2
        const val FLAG_MESSAGE = 0x4
        const val FLAG_MESSAGES = 0x8
        const val FLAG_ADDRESS = 0x10
        const val FLAG_PRIORITY_ADDRESS = 0x20
        const val FLAG_SEQNO = 0x40
        const val FLAG_CONFIRM_SEQNO = 0x80
        const val FLAG_RECV_ADDR_VERSION = 0x100
        const val FLAG_RECV_PRIORITY_ADDR_VERSION = 0x200
        const val FLAG_REINIT_DATE = 0x400
        const val FLAG_SIGNATURE = 0x800
        const val FLAG_PRIORITY = 0x1000
        const val FLAG_ALL = 0x1FFF
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
    val vectorAdnlMessage = VectorTlConstructor(AdnlMessage)

    override fun decode(input: Input): AdnlPacketContents {
        val rand1 = input.readBytesTl()
        val flags = input.readIntTl()
        val from = input.readFlagTl(flags, 0, PublicKey)
        val from_short = input.readFlagTl(flags, 1, AdnlIdShort)
        val message = input.readFlagTl(flags, 2, AdnlMessage)
        val messages = input.readFlagTl(flags, 3, vectorAdnlMessage)
        val address = input.readFlagTl(flags, 4, AdnlAddressList)
        val priority_address = input.readFlagTl(flags, 5, AdnlAddressList)
        val seqno = input.readFlagTl(flags, 6, LongTlConstructor)
        val confirm_seqno = input.readFlagTl(flags, 7, LongTlConstructor)
        val recv_addr_list_version = input.readFlagTl(flags, 8, IntTlConstructor)
        val recv_priority_addr_list_version = input.readFlagTl(flags, 9, IntTlConstructor)
        val reinit_date = input.readFlagTl(flags, 10, IntTlConstructor)
        val dst_reinit_date = input.readFlagTl(flags, 10, IntTlConstructor)
        val signature = input.readFlagTl(flags, 11, BytesTlConstructor)
        val rand2 = input.readStringTl().encodeToByteArray()
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
        val bad = -1946584576
        println("write: ${value.flags} - hex: ${value.flags.toString(16).padStart(8, '0')}")
        println("read : $bad - hex: ${bad.toUInt().toString(16).padStart(8, '0')}")

        // aaaaaaaa | 14 01 | 00 7a f9 8b
        // 8b f9 7a 00 - actual
        //

        output.writeOptionalTl(value.flags, 0, PublicKey, value.from)
        output.writeOptionalTl(value.flags, 1, AdnlIdShort, value.from_short)
        output.writeOptionalTl(value.flags, 2, AdnlMessage, value.message)
        output.writeOptionalTl(value.flags, 3, vectorAdnlMessage, value.messages)
        output.writeOptionalTl(value.flags, 4, AdnlAddressList, value.address)
        output.writeOptionalTl(value.flags, 5, AdnlAddressList, value.priority_address)
        output.writeOptionalTl(value.flags, 6, LongTlConstructor, value.seqno)
        output.writeOptionalTl(value.flags, 7, LongTlConstructor, value.confirm_seqno)
        output.writeOptionalTl(value.flags, 8, IntTlConstructor, value.recv_addr_list_version)
        output.writeOptionalTl(value.flags, 9, IntTlConstructor, value.recv_priority_addr_list_version)
        output.writeOptionalTl(value.flags, 10, IntTlConstructor, value.reinit_date)
        output.writeOptionalTl(value.flags, 10, IntTlConstructor, value.dst_reinit_date)
        output.writeOptionalTl(value.flags, 11, BytesTlConstructor, value.signature)
        output.writeBytesTl(value.rand2)
    }
}
