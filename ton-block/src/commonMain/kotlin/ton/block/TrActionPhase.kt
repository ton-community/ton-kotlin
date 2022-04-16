@file:UseSerializers(HexByteArraySerializer::class)

package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ton.crypto.HexByteArraySerializer
import ton.crypto.hex

@SerialName("tr_phase_action")
@Serializable
data class TrActionPhase(
    val success: Boolean,
    val valid: Boolean,
    val no_funds: Boolean,
    val status_change: AccStatusChange,
    val total_fwd_fees: Grams?,
    val total_action_fees: Grams?,
    val result_code: Int,
    val result_arg: Int?,
    val tot_actions: Int,
    val spec_actions: Int,
    val skipped_actions: Int,
    val msgs_created: Int,
    val action_list_hash: ByteArray,
    val tot_msg_size: StorageUsedShort
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrActionPhase

        if (success != other.success) return false
        if (valid != other.valid) return false
        if (no_funds != other.no_funds) return false
        if (status_change != other.status_change) return false
        if (total_fwd_fees != other.total_fwd_fees) return false
        if (total_action_fees != other.total_action_fees) return false
        if (result_code != other.result_code) return false
        if (result_arg != other.result_arg) return false
        if (tot_actions != other.tot_actions) return false
        if (spec_actions != other.spec_actions) return false
        if (skipped_actions != other.skipped_actions) return false
        if (msgs_created != other.msgs_created) return false
        if (!action_list_hash.contentEquals(other.action_list_hash)) return false
        if (tot_msg_size != other.tot_msg_size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = success.hashCode()
        result = 31 * result + valid.hashCode()
        result = 31 * result + no_funds.hashCode()
        result = 31 * result + status_change.hashCode()
        result = 31 * result + (total_fwd_fees?.hashCode() ?: 0)
        result = 31 * result + (total_action_fees?.hashCode() ?: 0)
        result = 31 * result + result_code
        result = 31 * result + (result_arg ?: 0)
        result = 31 * result + tot_actions
        result = 31 * result + spec_actions
        result = 31 * result + skipped_actions
        result = 31 * result + msgs_created
        result = 31 * result + action_list_hash.contentHashCode()
        result = 31 * result + tot_msg_size.hashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("TrActionPhase(success=")
        append(success)
        append(", valid=")
        append(valid)
        append(", no_funds=")
        append(no_funds)
        append(", status_change=")
        append(status_change)
        append(", total_fwd_fees=")
        append(total_fwd_fees)
        append(", total_action_fees=")
        append(total_action_fees)
        append(", result_code=")
        append(result_code)
        append(", result_arg=")
        append(result_arg)
        append(", tot_actions=")
        append(tot_actions)
        append(", spec_actions=")
        append(spec_actions)
        append(", skipped_actions=")
        append(skipped_actions)
        append(", msgs_created=")
        append(msgs_created)
        append(", action_list_hash=")
        append(hex(action_list_hash))
        append(", tot_msg_size=")
        append(tot_msg_size)
        append(")")
    }
}