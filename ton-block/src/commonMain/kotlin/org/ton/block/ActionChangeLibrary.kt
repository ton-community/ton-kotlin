package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("action_change_library")
data class ActionChangeLibrary(
    val mode: Int,
    @Suppress("SpellCheckingInspection")
    val libref: LibRef
) : OutAction
