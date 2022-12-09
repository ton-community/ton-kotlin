package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@Serializable
data class LiteServerTransactionId(
    val mode: Int,
    val account: ByteArray?,
    val lt: Long?,
    val hash: ByteArray?
) {
    constructor(account: ByteArray?, lt: Long?, hash: ByteArray?) : this(
        mode(account, lt, hash), account, lt, hash
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerTransactionId

        if (mode != other.mode) return false
        if (account != null) {
            if (other.account == null) return false
            if (!account.contentEquals(other.account)) return false
        } else if (other.account != null) return false
        if (lt != other.lt) return false
        if (hash != null) {
            if (other.hash == null) return false
            if (!hash.contentEquals(other.hash)) return false
        } else if (other.hash != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + (account?.contentHashCode() ?: 0)
        result = 31 * result + (lt?.hashCode() ?: 0)
        result = 31 * result + (hash?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerTransactionId(mode=")
        append(mode)
        append(", account=")
        append(account?.encodeHex())
        append(", lt=")
        append(lt)
        append(", hash=")
        append(hash?.encodeHex())
        append(")")
    }

    companion object : TlCodec<LiteServerTransactionId> by LiteServerTransactionIdTlConstructor {
        @JvmStatic
        fun mode(account: ByteArray?, lt: Long?, hash: ByteArray?): Int {
            var mode = 0
            account?.let { mode = mode or 0b001 }
            lt?.let { mode = mode or 0b010 }
            hash?.let { mode = mode or 0b100 }
            return mode
        }
    }
}

private object LiteServerTransactionIdTlConstructor : TlConstructor<LiteServerTransactionId>(
    schema = "liteServer.transactionId mode:# account:mode.0?int256 lt:mode.1?long hash:mode.2?int256 = liteServer.TransactionId"
) {
    override fun decode(input: Input): LiteServerTransactionId {
        val mode = input.readIntTl()
        val account = if (mode and 0b001 != 0) input.readInt256Tl() else null
        val lt = if (mode and 0b010 != 0) input.readLongTl() else null
        val hash = if (mode and 0b100 != 0) input.readInt256Tl() else null
        return LiteServerTransactionId(mode, account, lt, hash)
    }

    override fun encode(output: Output, value: LiteServerTransactionId) {
        output.writeIntTl(value.mode)
        if (value.account != null) output.writeInt256Tl(value.account)
        if (value.lt != null) output.writeLongTl(value.lt)
        if (value.hash != null) output.writeInt256Tl(value.hash)
    }
}
