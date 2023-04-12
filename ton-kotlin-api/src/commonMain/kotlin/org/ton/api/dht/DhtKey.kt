package org.ton.api.dht

import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlIdShort
import org.ton.tl.*
import org.ton.tl.ByteString.Companion.toByteString
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

@Serializable
public data class DhtKey
@JvmOverloads constructor(
    @get:JvmName("id")
    val id: ByteString,

    @get:JvmName("name")
    val name: String,

    @get:JvmName("idx")
    val idx: Int = 0
) : TlObject<DhtKey> {
    @JvmOverloads
    public constructor(id: ByteArray, name: String, idx: Int = 0) : this(id.toByteString(), name, idx)

    @JvmOverloads
    public constructor(id: AdnlIdShort, name: String, idx: Int = 0) : this(id.id, name, idx)

    init {
        require(id.size == 256)
    }

    override fun tlCodec(): TlCodec<DhtKey> = DhtKey

    public companion object : TlConstructor<DhtKey>(
        schema = "dht.key id:int256 name:bytes idx:int = dht.Key"
    ) {
        @JvmStatic
        public fun address(adnlIdShort: AdnlIdShort): DhtKey = DhtKey(adnlIdShort, "address")

        @JvmStatic
        public fun nodes(adnlIdShort: AdnlIdShort): DhtKey = DhtKey(adnlIdShort, "nodes")

        override fun encode(writer: TlWriter, value: DhtKey) {
            writer.writeRaw(value.id.toByteArray())
            writer.writeString(value.name)
            writer.writeInt(value.idx)
        }

        override fun decode(reader: TlReader): DhtKey {
            val id = reader.readRaw(32)
            val name = reader.readString()
            val idx = reader.readInt()
            return DhtKey(id, name, idx)
        }
    }
}
