package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("action_change_library")
public data class ActionChangeLibrary(
    val mode: Int,
    @Suppress("SpellCheckingInspection")
    val libref: LibRef
) : OutAction {
    override fun toString(): String = buildString {
        append("(action_change_library\n")
        append("mode:")
        append(mode)
        append(" libref:")
        append(libref)
        append(")")
    }
}
