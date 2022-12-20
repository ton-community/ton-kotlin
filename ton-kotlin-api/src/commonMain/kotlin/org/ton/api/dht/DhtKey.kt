package org.ton.api.dht

import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlIdShort
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
public data class DhtKey(
    val id: Bits256,
    val name: String,
    val idx: Int = 0
) : TlObject<DhtKey> {
    public constructor(id: ByteArray, name: String, idx: Int = 0) : this(Bits256(id), name, idx)
    public constructor(id: AdnlIdShort, name: String, idx: Int = 0) : this(id.id, name, idx)

    override fun tlCodec(): TlCodec<DhtKey> = DhtKey

    public companion object : TlConstructor<DhtKey>(
        schema = "dht.key id:int256 name:bytes idx:int = dht.Key"
    ) {
        @JvmStatic
        public fun address(adnlIdShort: AdnlIdShort): DhtKey = DhtKey(adnlIdShort, "address")

        @JvmStatic
        public fun nodes(adnlIdShort: AdnlIdShort): DhtKey = DhtKey(adnlIdShort, "nodes")

        override fun encode(writer: TlWriter, value: DhtKey) {
            writer.writeBits256(value.id)
            writer.writeString(value.name)
            writer.writeInt(value.idx)
        }

        override fun decode(reader: TlReader): DhtKey {
            val id = reader.readBits256()
            val name = reader.readString()
            val idx = reader.readInt()
            return DhtKey(id, name, idx)
        }
    }
}
