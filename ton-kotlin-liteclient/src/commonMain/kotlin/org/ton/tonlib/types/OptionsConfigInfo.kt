package org.ton.tonlib.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("options.configInfo")
internal data class OptionsConfigInfo(
    @SerialName("default_wallet_id")
    val defaultWalletId: Long,
    @SerialName("default_rwallet_init_public_key")
    val defaultRWalletInitPublicKey: String
)
