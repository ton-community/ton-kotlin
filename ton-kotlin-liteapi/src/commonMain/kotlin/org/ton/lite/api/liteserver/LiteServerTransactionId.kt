package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("liteServer.transactionId")
public data class LiteServerTransactionId internal constructor(
    val mode: Int,
    val account: ByteArray?,
    val lt: Long?,
    val hash: ByteArray?
) {
    public constructor(account: ByteArray?, lt: Long?, hash: ByteArray?) : this(
        mode(account != null, lt != null, hash != null), account, lt, hash
    )

    init {
        require(account == null || account.size == 32) { "account must be 32 bytes long" }
        require(hash == null || hash.size == 32) { "hash must be 32 bytes long" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerTransactionId) return false

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

    public companion object : TlCodec<LiteServerTransactionId> by LiteServerTransactionIdTlConstructor {
        @JvmStatic
        public fun mode(account: Boolean, lt: Boolean, hash: Boolean): Int {
            var mode = 0
            if (account) mode = mode or 1
            if (lt) mode = mode or 2
            if (hash) mode = mode or 4
            return mode
        }
    }
}

private object LiteServerTransactionIdTlConstructor : TlConstructor<LiteServerTransactionId>(
    schema = "liteServer.transactionId mode:# account:mode.0?int256 lt:mode.1?long hash:mode.2?int256 = liteServer.TransactionId"
) {
    override fun decode(reader: TlReader): LiteServerTransactionId {
        val mode = reader.readInt()
        val account = reader.readNullable(mode, 0) {
            readRaw(32)
        }
        val lt = reader.readNullable(mode, 1) {
            readLong()
        }
        val hash = reader.readNullable(mode, 2) {
            readRaw(32)
        }
        return LiteServerTransactionId(mode, account, lt, hash)
    }

    override fun encode(writer: TlWriter, value: LiteServerTransactionId) {
        writer.writeInt(value.mode)
        val mode = value.mode
        writer.writeNullable(mode, 0, value.account) {
            writeRaw(it)
        }
        writer.writeNullable(mode, 1, value.lt) {
            writeLong(it)
        }
        writer.writeNullable(mode, 2, value.hash) {
            writeRaw(it)
        }
    }
}
