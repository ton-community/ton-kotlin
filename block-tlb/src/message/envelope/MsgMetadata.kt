package org.ton.kotlin.message.envelope

import org.ton.kotlin.message.address.IntAddr

/**
 * ```tlb
 * msg_metadata#0 depth:uint32 initiator_addr:MsgAddressInt initiator_lt:uint64
 *   = MsgMetadata;
 * ```
 */
public data class MsgMetadata(
    val depth: Int,
    val initiatorAddr: IntAddr,
    val initiatorLt: Long,
)