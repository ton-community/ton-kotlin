package org.ton.kotlin.message.outmsg

import kotlinx.io.bytestring.ByteString

/**
 * A message that was dequeued from the outbound queue.
 *
 * ```tlb
 * msg_export_deq_short$1101 msg_env_hash:bits256
 *     next_workchain:int32 next_addr_pfx:uint64
 *     import_block_lt:uint64 = OutMsg;
 * ```
 */
public data class OutMsgDequeShort(
    /**
     * Message envelope hash.
     */
    val msgEnvelopeHash: ByteString,

    /**
     * Next workchain.
     */
    val nextWorkchain: Int,

    /**
     * Next address prefix.
     */
    val nextAddrPrefix: ULong,

    /**
     * Import block logical time.
     */
    val importBlockLt: Long
) : OutMsg