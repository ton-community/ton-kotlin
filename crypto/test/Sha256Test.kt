
import io.ktor.utils.io.core.*
import org.ton.crypto.digest.sha512
import kotlin.test.Test

class Sha256Test {
    @Test
    fun test() {
        println(sha512("вика".toByteArray()).toHexString())
    }
}
