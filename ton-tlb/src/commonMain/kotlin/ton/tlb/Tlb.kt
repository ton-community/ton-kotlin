@file:OptIn(ExperimentalSerializationApi::class)

package ton.tlb

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import ton.bitstring.BitString

sealed class Tlb(
    override val serializersModule: SerializersModule,
) : SerialFormat {
    companion object Default : Tlb(EmptySerializersModule)

    fun <T> decodeFromBitString(serializationStrategy: DeserializationStrategy<T>, bitString: BitString): T {


        TODO()
    }

    inline fun <reified T> decodeFromBitString(value: BitString): T =
        decodeFromBitString(serializersModule.serializer(), value)
}