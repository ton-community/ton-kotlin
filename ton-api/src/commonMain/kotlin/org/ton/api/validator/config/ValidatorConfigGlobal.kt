package org.ton.api.validator.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeVectorTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

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
        override fun encode(output: Output, value: ValidatorConfigGlobal) {
            output.writeTl(TonNodeBlockIdExt, value.zeroState)
            output.writeTl(TonNodeBlockIdExt, value.initBlock)
            output.writeVectorTl(value.hardforks, TonNodeBlockIdExt)
        }

        override fun decode(input: Input): ValidatorConfigGlobal {
            val zeroState = input.readTl(TonNodeBlockIdExt)
            val initBlock = input.readTl(TonNodeBlockIdExt)
            val hardforks = input.readVectorTl(TonNodeBlockIdExt)
            return ValidatorConfigGlobal(zeroState, initBlock, hardforks)
        }
    }
}
