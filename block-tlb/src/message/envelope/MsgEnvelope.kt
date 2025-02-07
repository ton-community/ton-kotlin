package org.ton.kotlin.message.envelope

import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.currency.Coins
import org.ton.kotlin.message.Message
import org.ton.kotlin.message.info.MsgInfo

/**
 * Message with routing information.
 *
 * ```tlb
 * msg_envelope#4 cur_addr:IntermediateAddress
 *   next_addr:IntermediateAddress fwd_fee_remaining:Grams
 *   msg:^(Message Any) = MsgEnvelope;
 *
 * msg_envelope_v2#5 cur_addr:IntermediateAddress
 *   next_addr:IntermediateAddress fwd_fee_remaining:Grams
 *   msg:^(Message Any)
 *   emitted_lt:(Maybe uint64)
 *   metadata:(Maybe MsgMetadata) = MsgEnvelope;
 * ```
 */
public data class MsgEnvelope(
    val currentAddr: IntermediateAddr,
    val nextAddr: IntermediateAddr,
    val fwdFeeRemaining: Coins,
    val message: Message<MsgInfo, CellSlice>,
    val emittedLt: Long? = null,
    val metadata: MsgMetadata? = null,
    val version: Version
) {
    public enum class Version {
        Legacy,
        V2
    }
}