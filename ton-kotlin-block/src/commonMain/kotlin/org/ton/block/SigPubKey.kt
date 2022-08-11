package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("ed25519_pubkey")
data class SigPubKey(
    val pubkey: BitString
) {
    init {
        require(pubkey.size == 256) { "required: pubkey.size == 256, actual: ${pubkey.size}" }
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<SigPubKey> = SigPubKeyTlbConstructor
    }
}

private object SigPubKeyTlbConstructor : TlbConstructor<SigPubKey>(
    schema = "ed25519_pubkey#8e81278a pubkey:bits256 = SigPubKey;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: SigPubKey
    ) = cellBuilder {
        storeBits(value.pubkey)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): SigPubKey = cellSlice {
        val pubkey = loadBits(256)
        SigPubKey(pubkey)
    }
}