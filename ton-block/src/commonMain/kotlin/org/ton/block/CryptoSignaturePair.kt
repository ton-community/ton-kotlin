package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("sig_pair")
data class CryptoSignaturePair(
    val node_id_short: BitString,
    val sign: CryptoSignature
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<CryptoSignaturePair> = CryptoSignaturePairTlbConstructor
    }
}

private object CryptoSignaturePairTlbConstructor : TlbConstructor<CryptoSignaturePair>(
    schema = "sig_pair\$_ node_id_short:bits256 sign:CryptoSignature = CryptoSignaturePair;  // 256+x ~ 772 bits\n"
) {
    val cryptoSignature by lazy { CryptoSignature.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: CryptoSignaturePair
    ) = cellBuilder {
        storeBits(value.node_id_short)
        storeTlb(cryptoSignature, value.sign)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): CryptoSignaturePair = cellSlice {
        val nodeIdShort = loadBitString(256)
        val sign = loadTlb(cryptoSignature)
        CryptoSignaturePair(nodeIdShort, sign)
    }
}