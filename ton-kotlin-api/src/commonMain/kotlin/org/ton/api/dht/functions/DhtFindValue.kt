package org.ton.api.dht.functions

import io.ktor.utils.io.core.*
import org.ton.api.dht.DhtKey
import org.ton.api.dht.DhtValueResult
import org.ton.tl.TLFunction
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl

data class DhtFindValue(
    val key: ByteArray,
    val k: Int
) : TLFunction<DhtFindValue, DhtValueResult> {
    constructor(key: DhtKey, k: Int) : this(key.key(), k)

    override fun tlCodec(): TlCodec<DhtFindValue> = DhtFindValueTlConstructor

    override fun resultTlCodec(): TlCodec<DhtValueResult> = DhtValueResult

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DhtFindValue) return false
        if (!key.contentEquals(other.key)) return false
        if (k != other.k) return false
        return true
    }

    override fun hashCode(): Int {
        var result = key.contentHashCode()
        result = 31 * result + k
        return result
    }
}

private object DhtFindValueTlConstructor : TlConstructor<DhtFindValue>(
    type = DhtFindValue::class,
    schema = "dht.findValue key:int256 k:int = dht.ValueResult"
) {
    override fun encode(output: Output, value: DhtFindValue) {
        output.writeInt256Tl(value.key)
        output.writeIntTl(value.k)
    }

    override fun decode(input: Input): DhtFindValue {
        val key = input.readInt256Tl()
        val k = input.readIntTl()
        return DhtFindValue(key, k)
    }
}
