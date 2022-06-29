package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.toBigInt
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.IntTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("tr_phase_compute_vm")
data class TrPhaseComputeVm(
    val success: Boolean,
    val msg_state_used: Boolean,
    val account_activated: Boolean,
    val gas_fees: Coins,
    val gas_used: VarUInteger,
    val gas_limit: VarUInteger,
    val gas_credit: Maybe<VarUInteger>,
    val mode: Int,
    val exit_code: Int,
    val exit_arg: Maybe<Int>,
    val vm_steps: Long,
    val vm_init_state_hash: BitString,
    val vm_final_state_hash: BitString,
) : TrComputePhase {
    init {
        require(vm_init_state_hash.size == 256) { "required: vm_init_state_hash.size == 256, actual: ${vm_init_state_hash.size}" }
        require(vm_final_state_hash.size == 256) { "required: vm_final_state_hash.size == 256, actual: ${vm_final_state_hash.size}" }
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TrPhaseComputeVm> = TrPhaseComputeVmTlbConstructor
    }
}

private object TrPhaseComputeVmTlbConstructor : TlbConstructor<TrPhaseComputeVm>(
    schema = "tr_phase_compute_vm\$1 success:Bool msg_state_used:Bool " +
            "account_activated:Bool gas_fees:Coins " +
            "^[ gas_used:(VarUInteger 7) " +
            "gas_limit:(VarUInteger 7) gas_credit:(Maybe (VarUInteger 3)) " +
            "mode:int8 exit_code:int32 exit_arg:(Maybe int32) " +
            "vm_steps:uint32 " +
            "vm_init_state_hash:bits256 vm_final_state_hash:bits256 ] " +
            "= TrComputePhase;"
) {
    val varUInteger7 = VarUInteger.tlbCodec(7)
    val maybeVarUInteger3 = Maybe.tlbCodec(VarUInteger.tlbCodec(3))
    val maybeInt32 = Maybe.tlbCodec(IntTlbConstructor(32))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrPhaseComputeVm
    ) = cellBuilder {
        storeBit(value.success)
        storeBit(value.msg_state_used)
        storeBit(value.account_activated)
        storeTlb(Coins, value.gas_fees)
        storeRef {
            storeTlb(varUInteger7, value.gas_used)
            storeTlb(varUInteger7, value.gas_limit)
            storeTlb(maybeVarUInteger3, value.gas_credit)
            storeInt(value.mode, 8)
            storeInt(value.exit_code, 32)
            storeTlb(maybeInt32, value.exit_arg.value?.toBigInt().toMaybe())
            storeUInt(value.vm_steps, 32)
            storeBits(value.vm_init_state_hash)
            storeBits(value.vm_final_state_hash)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrPhaseComputeVm = cellSlice {
        val success = loadBit()
        val msgStateUsed = loadBit()
        val accountActivated = loadBit()
        val gasFees = loadTlb(Coins)
        loadRef {
            val gasUsed = loadTlb(varUInteger7)
            val gasLimit = loadTlb(varUInteger7)
            val gasCredit = loadTlb(maybeVarUInteger3)
            val mode = loadInt(8).toInt()
            val exitCode = loadInt(32).toInt()
            val exitArg = loadTlb(maybeInt32).value?.toInt().toMaybe()
            val vmSteps = loadUInt(32).toLong()
            val vmInitStateHash = loadBits(256)
            val vmFinalStateHash = loadBits(256)
            TrPhaseComputeVm(
                success,
                msgStateUsed,
                accountActivated,
                gasFees,
                gasUsed,
                gasLimit,
                gasCredit,
                mode,
                exitCode,
                exitArg,
                vmSteps,
                vmInitStateHash,
                vmFinalStateHash
            )
        }
    }
}