package org.ton.api.fec

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.IntTlConstructor
import org.ton.tl.constructors.writeIntTl

@Serializable
data class FecRaptorQ(
    override val data_size: Int,
    override val symbol_size: Int,
    override val symbol_count: Int
) : FecType {
    init {
        FecType.check(this)
    }

    override fun tlCodec(): TlCodec<FecRaptorQ> = Companion

    companion object : TlConstructor<FecRaptorQ>(
        type = FecRaptorQ::class,
        schema = "fec.raptorQ data_size:int symbol_size:int symbols_count:int = fec.Type",
        fields = listOf(IntTlConstructor, IntTlConstructor, IntTlConstructor)
    ) {
        override fun encode(output: Output, value: FecRaptorQ) {
            output.writeIntTl(value.data_size)
            output.writeIntTl(value.symbol_size)
            output.writeIntTl(value.symbol_count)
        }

        override fun decode(values: Iterator<*>): FecRaptorQ {
            val data_size = values.next() as Int
            val symbol_size = values.next() as Int
            val symbol_count = values.next() as Int
            return FecRaptorQ(data_size, symbol_size, symbol_count)
        }
    }
}
