@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.json.JsonClassDiscriminator


@JsonClassDiscriminator("@type")
public sealed interface OutList
