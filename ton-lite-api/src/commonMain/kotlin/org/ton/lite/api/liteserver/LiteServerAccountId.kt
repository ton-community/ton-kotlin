@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.Workchain
import org.ton.block.AddrStd
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.encodeHex
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl

@Serializable
data class LiteServerAccountId(
    val workchain: Int,
    @Serializable(Base64ByteArraySerializer::class)
    val id: ByteArray
) {
    constructor() : this(Workchain.INVALID_WORKCHAIN, ByteArray(32))
    constructor(string: String) : this(AddrStd(string))

    init {
        check(id.size == 32)
    }

    constructor(addrStd: AddrStd) : this(addrStd.workchain_id, addrStd.address.toByteArray())

    fun toMsgAddressIntStd(): AddrStd = AddrStd(workchain, id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerAccountId

        if (workchain != other.workchain) return false
        if (!id.contentEquals(other.id)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workchain
        result = 31 * result + id.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerAccountId(workchain=")
        append(workchain)
        append(", id=")
        append(id.encodeHex().uppercase())
        append(")")
    }

    companion object : TlConstructor<LiteServerAccountId>(
        type = LiteServerAccountId::class,
        schema = "liteServer.accountId workchain:int id:int256 = liteServer.AccountId"
    ) {
        override fun decode(input: Input): LiteServerAccountId {
            val workchain = input.readIntTl()
            val id = input.readBytesTl()
            return LiteServerAccountId(workchain, id)
        }

        override fun encode(output: Output, value: LiteServerAccountId) {
            output.writeIntTl(value.workchain)
            output.writeInt256Tl(value.id)
        }
    }
}
