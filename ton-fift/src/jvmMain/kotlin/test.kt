import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import ton.fift.FiftInterpretator

fun main() {
    val vm = FiftInterpretator(ByteReadPacket(byteArrayOf()))
    while (true) {
        vm.input = buildPacket {
            writerUTF8().write(readln())
        }
        try {
            vm.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
