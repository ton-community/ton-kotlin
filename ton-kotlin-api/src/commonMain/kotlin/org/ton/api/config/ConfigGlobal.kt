@file:Suppress("OPT_IN_USAGE")

package org.ton.api.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.adnl.config.AdnlConfigGlobal
import org.ton.api.dht.config.DhtConfigGlobal
import org.ton.api.validator.config.ValidatorConfigGlobal
import org.ton.tl.*

@Serializable
@SerialName("config.global")
@JsonClassDiscriminator("@type")
data class ConfigGlobal(
    val adnl: AdnlConfigGlobal = AdnlConfigGlobal(),
    val dht: DhtConfigGlobal = DhtConfigGlobal(),
    val validator: ValidatorConfigGlobal = ValidatorConfigGlobal()
) : TlObject<ConfigGlobal> {
    override fun tlCodec(): TlCodec<ConfigGlobal> = Companion

    companion object : TlConstructor<ConfigGlobal>(
        schema = "config.global adnl:adnl.config.global dht:dht.config.global validator:validator.config.global = config.Global"
    ) {
        override fun encode(output: Output, value: ConfigGlobal) {
            output.writeTl(AdnlConfigGlobal, value.adnl)
            output.writeTl(DhtConfigGlobal, value.dht)
            output.writeTl(ValidatorConfigGlobal, value.validator)
        }

        override fun decode(input: Input): ConfigGlobal {
            val adnl = input.readTl(AdnlConfigGlobal)
            val dht = input.readTl(DhtConfigGlobal)
            val validator = input.readTl(ValidatorConfigGlobal)
            return ConfigGlobal(adnl, dht, validator)
        }
    }
}
