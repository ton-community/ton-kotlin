package org.ton.block.shard

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec

/**
 * Brief validator info.
 */
public data class ValidatorInfo(
    /**
     * Last 4 bytes of the hash of the validator list.
     */
    val validatorListHashShort: UInt,

    /**
     * Seqno of the catchain session.
     */
    val catchainSeqno: UInt,

    /**
     * Whether the value of catchain seqno has been incremented and will it also be incremented in the next block.
     */
    val nxCcUpdated: Boolean
) {
    /**
     * TL-B Schema:
     * ```tlb
     * validator_info$_
     *   validator_list_hash_short:uint32
     *   catchain_seqno:uint32
     *   nx_cc_updated:Bool
     * = ValidatorInfo;
     * ```
     */
    public object Tlb : TlbCodec<ValidatorInfo> {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: ValidatorInfo
        ): Unit = cellBuilder {
            storeUInt32(value.validatorListHashShort)
            storeUInt32(value.catchainSeqno)
            storeBit(value.nxCcUpdated)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): ValidatorInfo = cellSlice {
            val validatorListHashShort = loadUInt()
            val catchainSeqno = loadUInt()
            val nxCcUpdated = loadBit()
            ValidatorInfo(validatorListHashShort, catchainSeqno, nxCcUpdated)
        }
    }
}