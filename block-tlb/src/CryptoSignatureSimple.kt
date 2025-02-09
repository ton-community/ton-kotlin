package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider


@SerialName("ed25519_signature")
public data class CryptoSignatureSimple(
    val r: BitString,
    val s: BitString
) : CryptoSignature {
    public companion object : TlbConstructorProvider<CryptoSignatureSimple> by CryptoSignatureSimpleTlbConstructor
}

private object CryptoSignatureSimpleTlbConstructor : TlbConstructor<CryptoSignatureSimple>(
    schema = "ed25519_signature#5 R:bits256 s:bits256 = CryptoSignatureSimple;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: CryptoSignatureSimple
    ) = cellBuilder {
        storeBits(value.r)
        storeBits(value.s)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): CryptoSignatureSimple = cellSlice {
        val r = loadBits(256)
        val s = loadBits(256)
        CryptoSignatureSimple(r, s)
    }
}
