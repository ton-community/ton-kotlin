package org.ton.api.dht.db

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

@Serializable
data class DhtDbKey(
    val id: Int
) : TlObject<DhtDbKey>{
    override fun tlCodec() = Companion

    companion object : TlConstructor<DhtDbKey>(
        schema = "dht.db.key.bucket id:int = dht.db.Key"
    ) {
        override fun encode(output: Output, value: DhtDbKey) {
            output.writeIntTl(value.id)
        }

        override fun decode(input: Input): DhtDbKey {
            val id = input.readIntTl()
            return DhtDbKey(id)
        }
    }
}
