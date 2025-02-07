package org.ton.kotlin.shard

/**
 * Brief validator info.
 *
 * ```tlb
 * validator_info$_
 *   validator_list_hash_short:uint32
 *   catchain_seqno:uint32
 *   nx_cc_updated:Bool
 *   = ValidatorInfo;
 * ```
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
)