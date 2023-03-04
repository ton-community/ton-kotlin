package org.ton.crypto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
public inline fun base64(string: String): ByteArray = Base64.Default.decode(string)

@OptIn(ExperimentalEncodingApi::class)
public inline fun base64(byteArray: ByteArray): String = Base64.Default.encode(byteArray)

@OptIn(ExperimentalEncodingApi::class)
public inline fun base64url(string: String): ByteArray = Base64.UrlSafe.decode(string)

@OptIn(ExperimentalEncodingApi::class)
public inline fun base64url(byteArray: ByteArray): String = Base64.UrlSafe.encode(byteArray)

public object Base64ByteArraySerializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor = serialDescriptor<ByteArray>()

    override fun deserialize(decoder: Decoder): ByteArray = base64(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(base64(value))
    }
}
