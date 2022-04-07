package ton.lite.client

import io.ktor.utils.io.core.*

class LiteServerCurrentTime(
    val now: Long
) {
    companion object : TLCodec<LiteServerCurrentTime> {
        override val id: Int = -380436467

        override fun decode(input: Input): LiteServerCurrentTime {
            val now = input.readIntLittleEndian().toLong()
            return LiteServerCurrentTime(now)
        }

        override fun encode(output: Output, message: LiteServerCurrentTime) {
            output.writeIntLittleEndian(message.now.toInt())
        }
    }
}