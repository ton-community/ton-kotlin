package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AccStatusChange {
    @SerialName("acst_unchanged")
    UNCHANGED, // x -> x

    @SerialName("acst_frozen")
    FROZEN, // init -> frozen

    @SerialName("acst_deleted")
    DELETED // frozen -> deleted
}