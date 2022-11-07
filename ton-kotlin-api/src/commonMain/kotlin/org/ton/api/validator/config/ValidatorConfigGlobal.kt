@file:Suppress("OPT_IN_USAGE", "PropertyName")

package org.ton.api.validator.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeVectorTl

@Serializable
@SerialName("validator.config.global")
@JsonClassDiscriminator("@type")
data class ValidatorConfigGlobal(
    val zero_state: TonNodeBlockIdExt = TonNodeBlockIdExt(),
    val init_block: TonNodeBlockIdExt = TonNodeBlockIdExt(),
    val hardforks: List<TonNodeBlockIdExt> = listOf()
) : TlObject<ValidatorConfigGlobal> {
    override fun tlCodec(): TlCodec<ValidatorConfigGlobal> = Companion

    companion object : TlConstructor<ValidatorConfigGlobal>(
        type = ValidatorConfigGlobal::class,
        schema = "validator.config.global zero_state:tonNode.blockIdExt init_block:tonNode.blockIdExt hardforks:(vector tonNode.blockIdExt) = validator.config.Global"
    ) {
        override fun encode(output: Output, value: ValidatorConfigGlobal) {
            output.writeTl(TonNodeBlockIdExt, value.zero_state)
            output.writeTl(TonNodeBlockIdExt, value.init_block)
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
