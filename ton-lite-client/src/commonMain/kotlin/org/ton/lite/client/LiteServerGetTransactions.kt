@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.serialization.UseSerializers
import org.ton.adnl.TLCodec
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.hex

data class LiteServerGetTransactions(
    val count: Int,
    val account: LiteServerAccountId,
    val lt: Long,
    val hash: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerGetTransactions

        if (count != other.count) return false
        if (account != other.account) return false
        if (lt != other.lt) return false
        if (!hash.contentEquals(other.hash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = count
        result = 31 * result + account.hashCode()
        result = 31 * result + lt.hashCode()
        result = 31 * result + hash.contentHashCode()
        return result
    }

    override fun toString() =
        "LiteServerGetTransactions(count=$count, account=$account, lt=$lt, hash=${hex(hash)})"

    companion object : TLCodec<LiteServerGetTransactions> {
        override val id: Int = 474015649

        override fun decode(input: Input): LiteServerGetTransactions {
            TODO("Not yet implemented")
        }

        override fun encode(output: Output, message: LiteServerGetTransactions) {
            TODO("Not yet implemented")
        }
    }
}