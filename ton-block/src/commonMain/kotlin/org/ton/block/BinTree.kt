package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("@type")
sealed interface BinTree<X : Any>

@Serializable
@SerialName("bt_leaf")
data class BinTreeLeaf<X : Any>(
    val leaf: X
) : BinTree<X>

@Serializable
@SerialName("bt_fork")
data class BinTreeFork<X : Any>(
    val left: BinTree<X>,
    val right: BinTree<X>
) : BinTree<X>