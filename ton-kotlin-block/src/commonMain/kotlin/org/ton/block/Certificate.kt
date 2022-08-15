package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("certificate")
data class Certificate(
    val temp_key: SigPubKey,
    val valid_since: UInt,
    val valid_until: UInt
) {
    companion object : TlbConstructorProvider<Certificate> by CertificateTlbConstructor
}

private object CertificateTlbConstructor : TlbConstructor<Certificate>(
    schema = "certificate#4 temp_key:SigPubKey valid_since:uint32 valid_until:uint32 = Certificate;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: Certificate
    ) = cellBuilder {
        storeTlb(SigPubKey, value.temp_key)
        storeUInt32(value.valid_since)
        storeUInt32(value.valid_until)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Certificate = cellSlice {
        val tempKey = loadTlb(SigPubKey)
        val validSince = loadUInt32()
        val validUntil = loadUInt32()
        Certificate(tempKey, validSince, validUntil)
    }
}
