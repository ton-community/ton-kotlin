package org.ton.proxy.rldp.fec.raptorq

class Symbol(
    val id: Int,
    val data: ByteArray
) {
    companion object {
        @JvmStatic
        fun fromBytes(data: ByteArray, symbolsCount: Int, symbolsSize: Int): Array<Symbol> =
            Array(symbolsCount) { id ->
                val offset = id * symbolsSize
                val symbolData = ByteArray(symbolsSize)
                if (offset < data.size) {
                    data.copyInto(symbolData, 0, offset, offset + symbolsSize)
                }
                Symbol(id, symbolData)
            }
    }
}
