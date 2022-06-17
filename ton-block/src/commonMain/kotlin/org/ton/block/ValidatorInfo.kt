package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("validator_info")
data class ValidatorInfo(
    val validator_list_hash_short: Long,
    val catchain_seqno: Long,
    val nx_cc_updated: Boolean
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ValidatorInfo> = ValidatorInfoTlbConstructor
    }
}

private object ValidatorInfoTlbConstructor : TlbConstructor<ValidatorInfo>(
    schema = "validator_info\$_ " +
            "  validator_list_hash_short:uint32 " +
            "  catchain_seqno:uint32 " +
            "  nx_cc_updated:Bool " +
            "= ValidatorInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ValidatorInfo
    ) = cellBuilder {
        storeUInt(value.validator_list_hash_short, 32)
        storeUInt(value.catchain_seqno, 32)
        storeBit(value.nx_cc_updated)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ValidatorInfo = cellSlice {
        val validatorListHashShort = loadUInt(32).toLong()
        val catchainSeqno = loadUInt(32).toLong()
        val nxCcUpdated = loadBit()
        ValidatorInfo(validatorListHashShort, catchainSeqno, nxCcUpdated)
    }
}