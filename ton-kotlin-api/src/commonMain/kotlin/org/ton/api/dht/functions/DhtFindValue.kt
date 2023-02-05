package org.ton.api.dht.functions

import org.ton.api.dht.DhtKey
import org.ton.api.dht.DhtValueResult
import org.ton.bitstring.Bits256
import org.ton.tl.*

public data class DhtFindValue(
    val key: Bits256,
    val k: Int
) : TLFunction<DhtFindValue, DhtValueResult> {
    public constructor(key: ByteArray, k: Int) : this(Bits256(key), k)
    public constructor(key: DhtKey, k: Int) : this(key.hash(), k)

    override fun tlCodec(): TlCodec<DhtFindValue> = DhtFindValueTlConstructor

    override fun resultTlCodec(): TlCodec<DhtValueResult> = DhtValueResult
}

private object DhtFindValueTlConstructor : TlConstructor<DhtFindValue>(
    schema = "dht.findValue key:int256 k:int = dht.ValueResult"
) {
    override fun encode(output: TlWriter, value: DhtFindValue) {
        output.writeBits256(value.key)
        output.writeInt(value.k)
    }

    override fun decode(input: TlReader): DhtFindValue {
        val key = input.readBits256()
        val k = input.readInt()
        return DhtFindValue(key, k)
    }
}
