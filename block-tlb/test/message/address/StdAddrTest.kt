package org.ton.block.message.address

import org.ton.kotlin.message.address.StdAddr
import kotlin.test.Test

class StdAddrTest {
    @Test
    fun testParse() {
        val original = "EQAKtVj024T9MfYaJzU1xnDAkf_GGbHNu-V2mgvyjTuP6rvC"
        val stdAddr = StdAddr(original)
        println(stdAddr)
        val stdAddr1 = StdAddr(0, "0AB558F4DB84FD31F61A273535C670C091FFC619B1CDBBE5769A0BF28D3B8FEA".hexToByteArray())
        println(original)
        println(stdAddr1.toString(userFriendly = true))
    }
}