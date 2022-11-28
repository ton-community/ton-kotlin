package org.ton.api.fec

import kotlinx.serialization.Serializable
import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

@Serializable
sealed interface FecType : TlObject<FecType> {
    val data_size: Int
    val symbol_size: Int
    val symbol_count: Int

    companion object : TlCombinator<FecType>(
        FecRaptorQ,
    )
}
