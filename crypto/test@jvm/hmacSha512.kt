import org.junit.Test
import org.ton.crypto.digest.sha2.SHA512Digest
import org.ton.crypto.mac.hmac.HMac
import kotlin.test.assertContentEquals

@Suppress("DEPRECATION")
class HmacSha512Test {
    @Test
    fun testHmacSha512() {
        val key = ByteArray(32) { 0x11 }
        val input = ByteArray(32) { 0x22 }

        val expected =
            "f5b421da607e3180d016ab217ef93297596ff980c15cc1b697fb69354dbe409adddc17ff8292eeaaf279d77166dabfa88db21debacf132d5b1139a3337d5f07b".hexToByteArray()

        val actual = HMac(SHA512Digest(), key).apply {
            update(input)
        }.build()

        assertContentEquals(expected, actual)
    }
}
