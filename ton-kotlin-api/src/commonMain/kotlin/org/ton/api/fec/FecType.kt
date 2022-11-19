package org.ton.api.fec

import kotlinx.serialization.Serializable
import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

@Serializable
sealed interface FecType : TlObject<FecType> {
    companion object : TlCombinator<FecType>(
        FecRaptorQ,
    )
}
