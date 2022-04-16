package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AccountStatus {
    @SerialName("acc_state_uninit")
    UNINIT,

    @SerialName("acc_state_frozen")
    FROZEN,

    @SerialName("acc_state_active")
    ACTIVE,

    @SerialName("acc_state_nonexist")
    NONEXIST,
}