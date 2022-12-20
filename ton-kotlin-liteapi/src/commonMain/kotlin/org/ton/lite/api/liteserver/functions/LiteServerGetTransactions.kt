@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.crypto.HexByteArraySerializer
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerTransactionList
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
public data class LiteServerGetTransactions(
    val count: Int,
    val account: LiteServerAccountId,
    val lt: Long,
    val hash: Bits256
) : TLFunction<LiteServerGetTransactions, LiteServerTransactionList> {
    public constructor(
        count: Int,
        account: LiteServerAccountId,
        lt: Long,
        hash: ByteArray
    ) : this(count, account, lt, Bits256(hash))

    public companion object : TlConstructor<LiteServerGetTransactions>(
        schema = "liteServer.getTransactions count:# account:liteServer.accountId lt:long hash:int256 = liteServer.TransactionList"
    ) {
        override fun decode(reader: TlReader): LiteServerGetTransactions {
            val count = reader.readInt()
            val account = reader.read(LiteServerAccountId)
            val lt = reader.readLong()
            val hash = reader.readBits256()
            return LiteServerGetTransactions(count, account, lt, hash)
        }

        override fun encode(writer: TlWriter, value: LiteServerGetTransactions) {
            writer.writeInt(value.count)
            writer.write(LiteServerAccountId, value.account)
            writer.writeLong(value.lt)
            writer.writeBits256(value.hash)
        }
    }

    override fun tlCodec(): TlCodec<LiteServerGetTransactions> = LiteServerGetTransactions

    override fun resultTlCodec(): TlCodec<LiteServerTransactionList> = LiteServerTransactionList
}
