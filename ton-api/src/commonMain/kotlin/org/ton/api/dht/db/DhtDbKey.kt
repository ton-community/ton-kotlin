package org.ton.api.dht.db

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

@Serializable
data class DhtDbKey(
        val id: Int
) {
    companion object : TlConstructor<DhtDbKey>(
            type = DhtDbKey::class,
            schema = "dht.db.key.bucket id:int = dht.db.Key"
    ) {
        override fun encode(output: Output, message: DhtDbKey) {
            output.writeIntLittleEndian(message.id)
        }

        override fun decode(input: Input): DhtDbKey {
            val id = input.readIntLittleEndian()
            return DhtDbKey(id)
        }
    }
}