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
        FecType::class,
        FecRaptorQ::class to FecRaptorQ,
    ) {
        fun check(fecType: FecType) {
            require(fecType.symbol_size != 0) { "expected symbol_size != 0, actual: ${fecType.symbol_size}" }
            require(fecType.symbol_size <= 1 shl 11) { "symbol_size must be less than ${1 shl 11}, actual: ${fecType.symbol_size}" }
            val expectedSymbolCount = (fecType.data_size + fecType.symbol_size - 1) / fecType.symbol_size
            if (fecType.symbol_count != expectedSymbolCount) {
                throw IllegalArgumentException("expected symbol_count: $expectedSymbolCount, actual: ${fecType.symbol_count}")
            }
        }
    }
}
