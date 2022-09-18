@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.crypto.HexByteArraySerializer
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.IntTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("tr_phase_action")
@Serializable
data class TrActionPhase(
    val success: Boolean,
    val valid: Boolean,
    val no_funds: Boolean,
    val status_change: AccStatusChange,
    val total_fwd_fees: Maybe<Coins>,
    val total_action_fees: Maybe<Coins>,
    val result_code: Int,
    val result_arg: Maybe<Int>,
    val tot_actions: Int,
    val spec_actions: Int,
    val skipped_actions: Int,
    val msgs_created: Int,
    val action_list_hash: BitString,
    val tot_msg_size: StorageUsedShort
) {
    init {
        require(action_list_hash.size == 256) { "required: action_list_hash.size == 256, actual: ${action_list_hash.size}" }
    }


    companion object : TlbCodec<TrActionPhase> by TrActionPhaseTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TrActionPhase> = TrActionPhaseTlbConstructor
    }

    override fun toString(): String = buildString {
        append("(tr_phase_action\nsuccess:")
        append(success)
        append(" valid:")
        append(valid)
        append(" no_funds:")
        append(no_funds)
        append(" status_change:")
        append(status_change)
        append(" total_fwd_fees:")
        append(total_fwd_fees)
        append(" total_action_fees:")
        append(total_action_fees)
        append(" result_code:")
        append(result_code)
        append(" result_arg:")
        append(result_arg)
        append(" tot_actions:")
        append(tot_actions)
        append(" spec_actions:")
        append(spec_actions)
        append(" skipped_actions:")
        append(skipped_actions)
        append(" msgs_created:")
        append(msgs_created)
        append(" action_list_hash:")
        append(action_list_hash)
        append(" tot_msg_size:")
        append(tot_msg_size)
        append(")")
    }
}

private object TrActionPhaseTlbConstructor : TlbConstructor<TrActionPhase>(
    schema = "tr_phase_action\$_ success:Bool valid:Bool no_funds:Bool " +
        "status_change:AccStatusChange " +
        "total_fwd_fees:(Maybe Coins) total_action_fees:(Maybe Coins) " +
        "result_code:int32 result_arg:(Maybe int32) tot_actions:uint16 " +
        "spec_actions:uint16 skipped_actions:uint16 msgs_created:uint16 " +
        "action_list_hash:bits256 tot_msg_size:StorageUsedShort " +
        "= TrActionPhase;"
) {
    val maybeCoins = Maybe.tlbCodec(Coins)
    val maybeInt32 = Maybe.tlbCodec(IntTlbConstructor.int(32))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrActionPhase
    ) = cellBuilder {
        storeBit(value.success)
        storeBit(value.valid)
        storeBit(value.no_funds)
        storeTlb(AccStatusChange, value.status_change)
        storeTlb(maybeCoins, value.total_fwd_fees)
        storeTlb(maybeCoins, value.total_action_fees)
        storeInt(value.result_code, 32)
        storeTlb(maybeInt32, value.result_arg)
        storeUInt(value.tot_actions, 16)
        storeUInt(value.spec_actions, 16)
        storeUInt(value.skipped_actions, 16)
        storeUInt(value.msgs_created, 16)
        storeBits(value.action_list_hash)
        storeTlb(StorageUsedShort, value.tot_msg_size)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrActionPhase = cellSlice {
        val success = loadBit()
        val valid = loadBit()
        val noFunds = loadBit()
        val statusChange = loadTlb(AccStatusChange)
        val totalFwdFees = loadTlb(maybeCoins)
        val totalActionFees = loadTlb(maybeCoins)
        val resultCode = loadInt(32).toInt()
        val resultArg = loadTlb(maybeInt32)
        val totActions = loadUInt(16).toInt()
        val specActions = loadUInt(16).toInt()
        val skippedActions = loadUInt(16).toInt()
        val msgCreated = loadUInt(16).toInt()
        val actionListHash = loadBits(256)
        val totMsgSize = loadTlb(StorageUsedShort)
        TrActionPhase(
            success,
            valid,
            noFunds,
            statusChange,
            totalFwdFees,
            totalActionFees,
            resultCode,
            resultArg,
            totActions,
            specActions,
            skippedActions,
            msgCreated,
            actionListHash,
            totMsgSize
        )
    }
}
