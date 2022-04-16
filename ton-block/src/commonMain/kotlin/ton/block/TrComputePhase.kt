@file:Suppress("OPT_IN_USAGE")
@file:UseSerializers(HexByteArraySerializer::class)

package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.JsonClassDiscriminator
import ton.crypto.HexByteArraySerializer
import ton.crypto.hex

@JsonClassDiscriminator("@type")
@Serializable
sealed interface TrComputePhase {
    @SerialName("tr_phase_compute_skipped")
    @Serializable
    data class TrPhaseComputeSkipped(
        val reason: ComputeSkipReason
    ) : TrComputePhase

    @SerialName("tr_phase_compute_vm")
    @Serializable
    data class TrPhaseComputeVm(
        val success: Boolean,
        val msg_state_used: Boolean,
        val account_activated: Boolean,
        val gas_fees: Grams,
        val gas_used: VarUInteger,
        val gas_limit: VarUInteger,
        val gas_credit: VarUInteger?,
        val mode: Int,
        val exit_code: Int,
        val exit_arg: Int?,
        val vm_steps: Long,
        val vm_init_state_hash: ByteArray,
        val vm_final_state_hash: ByteArray,
    ) : TrComputePhase {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TrPhaseComputeVm

            if (success != other.success) return false
            if (msg_state_used != other.msg_state_used) return false
            if (account_activated != other.account_activated) return false
            if (gas_fees != other.gas_fees) return false
            if (gas_used != other.gas_used) return false
            if (gas_limit != other.gas_limit) return false
            if (gas_credit != other.gas_credit) return false
            if (mode != other.mode) return false
            if (exit_code != other.exit_code) return false
            if (exit_arg != other.exit_arg) return false
            if (vm_steps != other.vm_steps) return false
            if (!vm_init_state_hash.contentEquals(other.vm_init_state_hash)) return false
            if (!vm_final_state_hash.contentEquals(other.vm_final_state_hash)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = success.hashCode()
            result = 31 * result + msg_state_used.hashCode()
            result = 31 * result + account_activated.hashCode()
            result = 31 * result + gas_fees.hashCode()
            result = 31 * result + gas_used.hashCode()
            result = 31 * result + gas_limit.hashCode()
            result = 31 * result + (gas_credit?.hashCode() ?: 0)
            result = 31 * result + mode
            result = 31 * result + exit_code
            result = 31 * result + (exit_arg ?: 0)
            result = 31 * result + vm_steps.hashCode()
            result = 31 * result + vm_init_state_hash.contentHashCode()
            result = 31 * result + vm_final_state_hash.contentHashCode()
            return result
        }

        override fun toString(): String = buildString {
            append("TrPhaseComputeVm(success=")
            append(success)
            append(", msg_state_used=")
            append(msg_state_used)
            append(", account_activated=")
            append(account_activated)
            append(", gas_fees=")
            append(gas_fees)
            append(", gas_used=")
            append(gas_used)
            append(", gas_limit=")
            append(gas_limit)
            append(", gas_credit=")
            append(gas_credit)
            append(", mode=")
            append(mode)
            append(", exit_code=")
            append(exit_code)
            append(", exit_arg=")
            append(exit_arg)
            append(", vm_steps=")
            append(vm_steps)
            append(", vm_init_state_hash=")
            append(hex(vm_init_state_hash))
            append(", vm_final_state_hash=")
            append(hex(vm_final_state_hash))
            append(")")
        }
    }
}