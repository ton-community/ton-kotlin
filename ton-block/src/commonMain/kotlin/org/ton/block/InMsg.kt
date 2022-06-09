@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

interface InMsg

@JsonClassDiscriminator("@type")
@Serializable
sealed interface InMsgData : InMsg