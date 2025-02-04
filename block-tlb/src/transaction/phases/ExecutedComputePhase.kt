package org.ton.block.transaction.phases

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.block.Maybe
import org.ton.block.VarUInteger
import org.ton.block.currency.Coins
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import org.ton.tlb.constructor.IntTlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class ExecutedComputePhase(
    val success: Boolean,
    @SerialName("msg_state_used") val msgStateUsed: Boolean,
    @SerialName("account_activated") val accountActivated: Boolean,
    @SerialName("gas_fees") val gasFees: Coins,
    val r1: CellRef<TrComputePhaseAux>
) : ComputePhase {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_compute_vm") {
            field("success", success)
            field("msg_state_used", msgStateUsed)
            field("account_activated", accountActivated)
            field("gas_fees", gasFees)
            field(r1)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ExecutedComputePhase> by TrPhaseComputeVmTlbConstructor
}

public object TrPhaseComputeVmTlbConstructor : TlbConstructor<ExecutedComputePhase>(
    schema = "tr_phase_compute_vm\$1 success:Bool msg_state_used:Bool account_activated:Bool gas_fees:Coins  ^[\$_ gas_used:(VarUInteger 7) gas_limit:(VarUInteger 7) gas_credit:(Maybe (VarUInteger 3)) mode:int8 exit_code:int32 exit_arg:(Maybe int32) vm_steps:uint32 vm_init_state_hash:bits256 vm_final_state_hash:bits256 ] = TrPhaseComputeVm;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ExecutedComputePhase
    ): Unit = cellBuilder {
        storeBoolean(value.success)
        storeBoolean(value.msgStateUsed)
        storeBoolean(value.accountActivated)
        storeTlb(Coins.Tlb, value.gasFees)
        storeTlb(CellRef(TrComputePhaseAux), value.r1)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExecutedComputePhase = cellSlice {
        val success = loadBit()
        val msgStateUsed = loadBit()
        val accountActivated = loadBit()
        val gasFees = loadTlb(Coins.Tlb)
        val r1 = loadTlb(CellRef(TrComputePhaseAux))
        ExecutedComputePhase(success, msgStateUsed, accountActivated, gasFees, r1)
    }
}

@Serializable
public data class TrComputePhaseAux(
    @SerialName("gas_used") val gasUsed: VarUInteger,
    @SerialName("gas_limit") val gasLimit: VarUInteger,
    @SerialName("gas_credit") val gasCredit: Maybe<VarUInteger>,
    val mode: Int,
    @SerialName("exit_code") val exitCode: Int,
    @SerialName("exit_arg") val exitArg: Maybe<Int>,
    @SerialName("vm_steps") val vmSteps: UInt,
    @SerialName("vm_init_state_hash") val vmInitStateHash: BitString,
    @SerialName("vm_final_state_hash") val vmFinalStateHash: BitString,
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer {
            type {
                field("gas_used", gasUsed)
                field("gas_limit", gasLimit)
                field("gas_credit", gasCredit)
                field("mode", mode)
                field("exit_code", exitCode)
                field("exit_arg", exitArg)
                field("vm_steps", vmSteps)
                field("vm_init_state_hash", vmInitStateHash)
                field("vm_final_state_hash", vmFinalStateHash)
            }
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TrComputePhaseAux> by TrComputePhaseAuxTlbConstructor
}

private object TrComputePhaseAuxTlbConstructor : TlbConstructor<TrComputePhaseAux>(
    schema = "\$_" +
            " gas_used:(VarUInteger 7) " +
            "gas_limit:(VarUInteger 7) " +
            "gas_credit:(Maybe (VarUInteger 3)) " +
            "mode:int8 " +
            "exit_code:int32 " +
            "exit_arg:(Maybe int32) " +
            "vm_steps:uint32 " +
            "vm_init_state_hash:bits256 " +
            "vm_final_state_hash:bits256"
) {
    val VarUInteger7 = VarUInteger.Companion.tlbCodec(7)
    val MaybeVarUInteger3 = Maybe.Companion.tlbCodec(VarUInteger.Companion.tlbCodec(3))
    val MaybeInt32 = Maybe.Companion.tlbCodec(IntTlbConstructor.int(32))

    override fun loadTlb(cellSlice: CellSlice): TrComputePhaseAux {
        return TrComputePhaseAux(
            gasUsed = cellSlice.loadTlb(VarUInteger7),
            gasLimit = cellSlice.loadTlb(VarUInteger7),
            gasCredit = cellSlice.loadTlb(MaybeVarUInteger3),
            mode = cellSlice.loadUInt(8).toInt(),
            exitCode = cellSlice.loadUInt(32).toInt(),
            exitArg = cellSlice.loadTlb(MaybeInt32),
            vmSteps = cellSlice.loadUInt(),
            vmInitStateHash = cellSlice.loadBits(256),
            vmFinalStateHash = cellSlice.loadBits(256)
        )
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: TrComputePhaseAux) {
        cellBuilder {
            storeTlb(VarUInteger7, value.gasUsed)
            storeTlb(VarUInteger7, value.gasLimit)
            storeTlb(MaybeVarUInteger3, value.gasCredit)
            storeInt(value.mode, 8)
            storeInt(value.exitCode, 32)
            storeTlb(MaybeInt32, value.exitArg)
            storeUInt32(value.vmSteps)
            storeBits(value.vmInitStateHash)
            storeBits(value.vmFinalStateHash)
        }
    }
}
