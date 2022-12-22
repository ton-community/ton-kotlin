package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
public data class LiteServerTransactionId internal constructor(
    val mode: Int,
    val account: Bits256?,
    val lt: Long?,
    val hash: Bits256?
) {
    public constructor(account: ByteArray?, lt: Long?, hash: ByteArray?) : this(
        account?.let { Bits256(it) }, lt, hash?.let { Bits256(it) }
    )

    public constructor(account: Bits256?, lt: Long?, hash: Bits256?) : this(
        mode(account != null, lt != null, hash != null), account, lt, hash
    )

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
            readBits256()
        }
        val lt = reader.readNullable(mode, 1) {
            readLong()
        }
        val hash = reader.readNullable(mode, 2) {
            readBits256()
        }
        return LiteServerTransactionId(mode, account, lt, hash)
    }

    override fun encode(writer: TlWriter, value: LiteServerTransactionId) {
        writer.writeInt(value.mode)
        val mode = value.mode
        writer.writeNullable(mode, 0, value.account) {
            writeBits256(it)
        }
        writer.writeNullable(mode, 1, value.lt) {
            writeLong(it)
        }
        writer.writeNullable(mode, 2, value.hash) {
            writeBits256(it)
        }
    }
}
