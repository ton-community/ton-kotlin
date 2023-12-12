
import io.ktor.utils.io.core.*
import org.ton.crypto.digest.sha512
import org.ton.crypto.encodeHex
import kotlin.test.Test

class Sha256Test {
    @Test
    fun test() {
        println(sha512("вика".toByteArray()).encodeHex())
    }
}
