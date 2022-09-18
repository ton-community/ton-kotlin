package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("validator_info")
data class ValidatorInfo(
    val validator_list_hash_short: UInt,
    val catchain_seqno: UInt,
    val nx_cc_updated: Boolean
) {
    companion object : TlbConstructorProvider<ValidatorInfo> by ValidatorInfoTlbConstructor
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
        storeUInt32(value.validator_list_hash_short)
        storeUInt32(value.catchain_seqno)
        storeBit(value.nx_cc_updated)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ValidatorInfo = cellSlice {
        val validatorListHashShort = loadUInt32()
        val catchainSeqno = loadUInt32()
        val nxCcUpdated = loadBit()
        ValidatorInfo(validatorListHashShort, catchainSeqno, nxCcUpdated)
    }
}
