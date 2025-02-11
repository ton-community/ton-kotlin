@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.crypto.HexByteArraySerializer
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.IntTlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@SerialName("tr_phase_action")

public data class ActionPhase(
    val success: Boolean,
    val valid: Boolean,
    @SerialName("no_funds") val noFunds: Boolean,
    @SerialName("status_change") val statusChange: AccStatusChange,
    @SerialName("total_fwd_fees") val totalFwdFees: Maybe<Coins>,
    @SerialName("total_action_fees") val totalActionFees: Maybe<Coins>,
    @SerialName("result_code") val resultCode: Int,
    @SerialName("result_arg") val resultArg: Maybe<Int>,
    @SerialName("tot_actions") val totActions: Int,
    @SerialName("spec_actions") val specActions: Int,
    @SerialName("skipped_actions") val skippedActions: Int,
    @SerialName("msgs_created") val msgsCreated: Int,
    @SerialName("action_list_hash") val actionListHash: BitString,
    @SerialName("tot_msg_size") val totMsgSize: StorageUsedShort
) : TlbObject {
    init {
        require(actionListHash.size == 256) { "expected actionListHash.size == 256, actual: ${actionListHash.size}" }
    }

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_action") {
            field("success", success)
            field("valid", valid)
            field("no_funds", noFunds)
            field("status_change", statusChange)
            field("total_fwd_fees", totalFwdFees)
            field("total_action_fees", totalActionFees)
            field("result_code", resultCode)
            field("result_arg", resultArg)
            field("tot_actions", totActions)
            field("spec_actions", specActions)
            field("skipped_actions", skippedActions)
            field("msgs_created", msgsCreated)
            field("action_list_hash", actionListHash)
            field("tot_msg_size", totMsgSize)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ActionPhase> by TrActionPhaseTlbConstructor
}

private object TrActionPhaseTlbConstructor : TlbConstructor<ActionPhase>(
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
        value: ActionPhase
    ) = cellBuilder {
        storeBit(value.success)
        storeBit(value.valid)
        storeBit(value.noFunds)
        storeTlb(AccStatusChange, value.statusChange)
        storeTlb(maybeCoins, value.totalFwdFees)
        storeTlb(maybeCoins, value.totalActionFees)
        storeInt(value.resultCode, 32)
        storeTlb(maybeInt32, value.resultArg)
        storeUInt(value.totActions, 16)
        storeUInt(value.specActions, 16)
        storeUInt(value.skippedActions, 16)
        storeUInt(value.msgsCreated, 16)
        storeBits(value.actionListHash)
        storeTlb(StorageUsedShort, value.totMsgSize)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ActionPhase = cellSlice {
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
        ActionPhase(
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
