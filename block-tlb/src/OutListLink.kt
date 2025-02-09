package org.ton.block

import kotlinx.serialization.SerialName


@SerialName("out_list")
public data class OutListLink(
    val prev: OutList,
    val action: OutAction
) : OutList
