package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ComputeSkipReason {
    @SerialName("cskip_no_state")
    NO_STATE,

    @SerialName("cskip_bad_state")
    BAD_STATE,

    @SerialName("cskip_no_gas")
    NO_GAS
}