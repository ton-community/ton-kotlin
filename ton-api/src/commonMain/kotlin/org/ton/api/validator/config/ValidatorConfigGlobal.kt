package org.ton.api.validator.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlConstructor

@SerialName("validator.config.global")
@Serializable
data class ValidatorConfigGlobal(
    @SerialName("zero_state")
    val zeroState: TonNodeBlockIdExt,
    @SerialName("init_block")
    val initBlock: TonNodeBlockIdExt,
    val hardforks: List<TonNodeBlockIdExt>
) {
    companion object : TlConstructor<ValidatorConfigGlobal>(
        type = ValidatorConfigGlobal::class,
        schema = "validator.config.global zero_state:tonNode.blockIdExt init_block:tonNode.blockIdExt hardforks:(vector tonNode.blockIdExt) = validator.config.Global"
    ) {
        override fun encode(output: Output, message: ValidatorConfigGlobal) {
            output.writeTl(message.zeroState, TonNodeBlockIdExt)
            output.writeTl(message.initBlock, TonNodeBlockIdExt)
            output.writeVector(message.hardforks, TonNodeBlockIdExt)
        }

        override fun decode(input: Input): ValidatorConfigGlobal {
            val zeroState = input.readTl(TonNodeBlockIdExt)
            val initBlock = input.readTl(TonNodeBlockIdExt)
            val hardforks = input.readVector(TonNodeBlockIdExt)
            return ValidatorConfigGlobal(zeroState, initBlock, hardforks)
        }
    }
}