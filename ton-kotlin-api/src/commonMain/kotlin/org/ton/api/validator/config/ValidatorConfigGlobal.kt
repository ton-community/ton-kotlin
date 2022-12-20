@file:Suppress("OPT_IN_USAGE", "PropertyName")

package org.ton.api.validator.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("validator.config.global")
@JsonClassDiscriminator("@type")
public data class ValidatorConfigGlobal(
    val zeroState: TonNodeBlockIdExt = TonNodeBlockIdExt(),
    val initBlock: TonNodeBlockIdExt = TonNodeBlockIdExt(),
    val hardforks: Collection<TonNodeBlockIdExt> = listOf()
) {
    public companion object : TlCodec<ValidatorConfigGlobal> by ValidatorConfigGlobalTlConstructor
}

private object ValidatorConfigGlobalTlConstructor : TlConstructor<ValidatorConfigGlobal>(
    schema = "validator.config.global zero_state:tonNode.blockIdExt init_block:tonNode.blockIdExt hardforks:(vector tonNode.blockIdExt) = validator.config.Global"
) {
    override fun encode(writer: TlWriter, value: ValidatorConfigGlobal) = writer {
        writer.write(TonNodeBlockIdExt, value.zeroState)
        writer.write(TonNodeBlockIdExt, value.initBlock)
        writer.writeCollection(value.hardforks) {
            writer.write(TonNodeBlockIdExt, it)
        }
    }

    override fun decode(reader: TlReader): ValidatorConfigGlobal = reader {
        val zeroState = read(TonNodeBlockIdExt)
        val initBlock = read(TonNodeBlockIdExt)
        val hardforks = readCollection {
            read(TonNodeBlockIdExt)
        }
        ValidatorConfigGlobal(zeroState, initBlock, hardforks)
    }
}
