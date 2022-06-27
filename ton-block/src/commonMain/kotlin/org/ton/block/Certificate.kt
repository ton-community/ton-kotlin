package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("certificate")
data class Certificate(
    val temp_key: SigPubKey,
    val valid_since: Long,
    val valid_until: Long
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<Certificate> = CertificateTlbConstructor
    }
}

private object CertificateTlbConstructor : TlbConstructor<Certificate>(
    schema = "certificate#4 temp_key:SigPubKey valid_since:uint32 valid_until:uint32 = Certificate;"
) {
    val sigPubKey by lazy { SigPubKey.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: Certificate
    ) = cellBuilder {
        storeTlb(sigPubKey, value.temp_key)
        storeUInt(value.valid_since, 32)
        storeUInt(value.valid_until, 32)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Certificate = cellSlice {
        val tempKey = loadTlb(sigPubKey)
        val validSince = loadUInt(32).toLong()
        val validUntil = loadUInt(32).toLong()
        Certificate(tempKey, validSince, validUntil)
    }
}