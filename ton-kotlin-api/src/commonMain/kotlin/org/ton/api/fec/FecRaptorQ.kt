package org.ton.api.fec

import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class FecRaptorQ(
    override val data_size: Int,
    override val symbol_size: Int,
    override val symbol_count: Int
) : FecType {
    init {
        FecType.check(this)
    }

    override fun tlCodec(): TlCodec<FecRaptorQ> = Companion

    public companion object : TlConstructor<FecRaptorQ>(
        schema = "fec.raptorQ data_size:int symbol_size:int symbols_count:int = fec.Type",
    ) {
        override fun encode(output: TlWriter, value: FecRaptorQ) {
            output.writeInt(value.data_size)
            output.writeInt(value.symbol_size)
            output.writeInt(value.symbol_count)
        }

        override fun decode(input: TlReader): FecRaptorQ {
            val data_size = input.readInt()
            val symbol_size = input.readInt()
            val symbol_count = input.readInt()
            return FecRaptorQ(data_size, symbol_size, symbol_count)
        }
    }
}
