package org.ton.kotlin.message.info

import org.ton.kotlin.message.address.ExtAddr
import org.ton.kotlin.message.address.IntAddr

/**
 * Unfinished external outgoing message info.
 *
 * ```tlb
 * ext_out_msg_info$11 src:MsgAddress dest:MsgAddressExt
 *   created_lt:uint64 created_at:uint32 = CommonMsgInfoRelaxed;
 * ```
 */
public data class RelaxedExtOutMsgInfo(
    /**
     * Optional internal source address.
     */
    override val src: IntAddr? = null,

    /**
     * Optional external destination address.
     */
    override val dest: ExtAddr? = null,

    /**
     * Logical time when the message was created.
     */
    val createdLt: Long = 0,

    /**
     * Unix timestamp in seconds when the message was created.
     */
    val createdAt: Long = 0
) : RelaxedMsgInfo