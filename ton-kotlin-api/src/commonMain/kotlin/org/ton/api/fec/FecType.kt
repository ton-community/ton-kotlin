package org.ton.api.fec

import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

interface FecType : TlObject<FecType> {
    companion object : TlCombinator<FecType>(
        FecRaptorQ,
    )
}
