package currency

import org.ton.kotlin.currency.Coins
import kotlin.test.Test

class CoinsTest {
    @Test
    fun testSerialization() {
        println(Coins.MAX.amount.bitLength)
    }
}