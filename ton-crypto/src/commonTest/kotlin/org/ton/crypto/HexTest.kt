package org.ton.crypto

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails

class HexTest {
    @Test
    fun `test HEX`() {
        assertHex("48656c6c6f2c20576f726c6421", "Hello, World!")
        assertHex("544f4e", "TON")
        assertHex(
            "544f4e20697320612066756c6c7920646563656e7472616c697a6564206c617965722d3120626c6f636b636861696e2064657369676e65642062792054656c656772616d20746f206f6e626f6172642062696c6c696f6e73206f662075736572732e20497420626f6173747320756c7472612d66617374207472616e73616374696f6e732c2074696e7920666565732c20656173792d746f2d75736520617070732c20616e6420697320656e7669726f6e6d656e74616c6c7920667269656e646c792e",
            "TON is a fully decentralized layer-1 blockchain designed by Telegram to onboard billions of users. It boasts ultra-fast transactions, tiny fees, easy-to-use apps, and is environmentally friendly."
        )
        assertFails { hex("Random string.") }
        assertFails { hex("qwerty123") }
    }
}

private fun assertHex(hex: String, string: String) {
    assertEquals(hex, hex(string.encodeToByteArray()))
    assertEquals(hex, hex(string.encodeToByteArray().asIterable()))
    assertContentEquals(string.encodeToByteArray(), hex(hex))
}
