package org.ton.api.validator.config

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.adnl.AdnlAddressList

@SerialName("validator.config.random.local")
@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
public data class ValidatorConfigRandomLocal(
    val addr_list: AdnlAddressList
) : ValidatorConfigLocal
