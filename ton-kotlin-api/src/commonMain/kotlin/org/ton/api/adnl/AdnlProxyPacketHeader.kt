package org.ton.api.adnl

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

data class AdnlProxyPacketHeader(
    val proxy_id: ByteArray,
    val flags: Int,
    val ip: Int?,
    val port: Int?,
    val adnl_start_time: Int?,
    val seqno: Long?,
    val date: Int?,
    val signature: ByteArray
) {
    constructor(
        proxy_id: ByteArray,
        ip: Int?,
        port: Int?,
        adnl_start_time: Int?,
        seqno: Long?,
        date: Int?,
        signature: ByteArray,
        isOutbound: Boolean = false,
        isControl: Boolean = false
    ) : this(
        proxy_id,
        flags = (if (ip != null) IP_MASK else 0) or
                (if (port != null) PORT_MASK else 0) or
                (if (adnl_start_time != null) ADNL_START_TIME_MASK else 0) or
                (if (seqno != null) SEQNO_MASK else 0) or
                (if (date != null) DATE_MASK else 0),
        ip,
        port,
        adnl_start_time,
        seqno,
        date,
        signature
    )

    val isOutbound: Boolean get() = flags and OUTBOUND_MASK != 0
    val isControl: Boolean get() = flags and CONTROL_MASK != 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlProxyPacketHeader

        if (!proxy_id.contentEquals(other.proxy_id)) return false
        if (flags != other.flags) return false
        if (ip != other.ip) return false
        if (port != other.port) return false
        if (adnl_start_time != other.adnl_start_time) return false
        if (seqno != other.seqno) return false
        if (date != other.date) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = proxy_id.contentHashCode()
        result = 31 * result + flags
        result = 31 * result + (ip ?: 0)
        result = 31 * result + (port ?: 0)
        result = 31 * result + (adnl_start_time ?: 0)
        result = 31 * result + (seqno?.hashCode() ?: 0)
        result = 31 * result + (date ?: 0)
        result = 31 * result + signature.contentHashCode()
        return result
    }

    companion object {
        const val IP_MASK = 0b0001
        const val PORT_MASK = 0b0001
        const val ADNL_START_TIME_MASK = 0b0010
        const val SEQNO_MASK = 0b0100
        const val DATE_MASK = 0b1000
        const val OUTBOUND_MASK = 0b0001_00000000_00000000
        const val CONTROL_MASK = 0b0010_00000000_00000000

        fun tlConstructor(): TlConstructor<AdnlProxyPacketHeader> = AdnlProxyPacketHeaderTlConstructor
    }
}

private object AdnlProxyPacketHeaderTlConstructor : TlConstructor<AdnlProxyPacketHeader>(
    type = AdnlProxyPacketHeader::class,
    schema = "adnl.proxyPacketHeader\n" +
            "  proxy_id:int256\n" +
            "  flags:# \n" +
            "  ip:flags.0?int\n" +
            "  port:flags.0?int\n" +
            "  adnl_start_time:flags.1?int\n" +
            "  seqno:flags.2?long\n" +
            "  date:flags.3?int\n" +
            "  signature:int256 = adnl.ProxyPacketHeader",
    id = 141114488
) {
    override fun encode(output: Output, value: AdnlProxyPacketHeader) {
        output.writeInt256Tl(value.proxy_id)
        output.writeIntTl(value.flags)
        if (value.ip != null && value.flags and AdnlProxyPacketHeader.IP_MASK != 0) {
            output.writeIntTl(value.ip)
        }
        if (value.port != null && value.flags and AdnlProxyPacketHeader.PORT_MASK != 0) {
            output.writeIntTl(value.port)
        }
        if (value.adnl_start_time != null && value.flags and AdnlProxyPacketHeader.ADNL_START_TIME_MASK != 0) {
            output.writeIntTl(value.adnl_start_time)
        }
        if (value.seqno != null && value.flags and AdnlProxyPacketHeader.SEQNO_MASK != 0) {
            output.writeLongTl(value.seqno)
        }
        if (value.date != null && value.flags and AdnlProxyPacketHeader.DATE_MASK != 0) {
            output.writeIntTl(value.date)
        }
        output.writeInt256Tl(value.signature)
    }

    override fun decode(input: Input): AdnlProxyPacketHeader {
        val proxyId = input.readInt256Tl()
        val flags = input.readIntTl()
        val ip = if (flags and AdnlProxyPacketHeader.IP_MASK != 0) {
            input.readIntTl()
        } else null
        val port = if (flags and AdnlProxyPacketHeader.PORT_MASK != 0) {
            input.readIntTl()
        } else null
        val adnlStartTime = if (flags and AdnlProxyPacketHeader.ADNL_START_TIME_MASK != 0) {
            input.readIntTl()
        } else null
        val seqno = if (flags and AdnlProxyPacketHeader.SEQNO_MASK != 0) {
            input.readLongTl()
        } else null
        val date = if (flags and AdnlProxyPacketHeader.DATE_MASK != 0) {
            input.readIntTl()
        } else null
        val signature = input.readInt256Tl()
        return AdnlProxyPacketHeader(
            proxyId, flags, ip, port, adnlStartTime, seqno, date, signature
        )
    }
}
