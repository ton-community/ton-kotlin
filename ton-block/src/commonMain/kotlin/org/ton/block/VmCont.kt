@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmCont {
    @SerialName("vmc_std")
    @Serializable
    data class Std(
        val cdata: VmControlData,
        val code: VmCellSlice
    ) : VmCont

    @SerialName("vmc_envelope")
    @Serializable
    data class Envelope(
        val cdata: VmControlData,
        val next: VmCont
    ) : VmCont

    @SerialName("vmc_quit")
    @Serializable
    data class Quit(
        @SerialName("exit_code")
        val exitCode: Int
    ) : VmCont

    @SerialName("vmc_quit_exc")
    @Serializable
    object QuitExc : VmCont

    @SerialName("vmc_repeat")
    @Serializable
    data class Repeat(
        val count: Long,
        val body: VmCont,
        val after: VmCont
    ) : VmCont

    @SerialName("vmc_until")
    @Serializable
    data class Until(
        val body: VmCont,
        val after: VmCont
    ) : VmCont

    @SerialName("vmc_again")
    @Serializable
    data class Again(
        val body: VmCont
    ) : VmCont

    @SerialName("vmc_while_cond")
    @Serializable
    data class WhileCond(
        val cond: VmCont,
        val body: VmCont,
        val after: VmCont
    ) : VmCont

    @SerialName("vmc_while_body")
    @Serializable
    data class WhileBody(
        val cond: VmCont,
        val body: VmCont,
        val after: VmCont
    ) : VmCont

    @SerialName("vmc_pushint")
    @Serializable
    data class PushInt(
        val value: Int,
        val next: VmCont
    ) : VmCont
}
