@file:Suppress("OPT_IN_USAGE")

package org.ton.api.config

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.control.ControlConfigLocal
import org.ton.api.dht.config.DhtConfigLocal
import org.ton.api.id.config.IdConfigLocal
import org.ton.api.liteserver.config.LiteServerConfigLocal
import org.ton.api.validator.config.ValidatorConfigLocal

@Serializable
@Polymorphic
@SerialName("config.local")
@JsonClassDiscriminator("@type")
public data class ConfigLocal(
    val local_ids: Collection<IdConfigLocal>,
    val dht: Collection<DhtConfigLocal>,
    val validators: Collection<ValidatorConfigLocal>,
    val liteservers: Collection<LiteServerConfigLocal>,
    val control: Collection<ControlConfigLocal>
)
