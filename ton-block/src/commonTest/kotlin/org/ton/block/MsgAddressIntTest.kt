package org.ton.block

import org.ton.crypto.hex
import kotlin.test.Test
import kotlin.test.assertEquals

class MsgAddressIntTest {
    @Test
    fun `parse raw addresses`() {
        val addr1 =
            MsgAddressInt.AddrStd.parseRaw("-1:3333333333333333333333333333333333333333333333333333333333333333")
        assertEquals(-1, addr1.workchain_id)
        assertEquals("3333333333333333333333333333333333333333333333333333333333333333", hex(addr1.address))

        val addr2 =
            MsgAddressInt.AddrStd.parseRaw("0:83dfd552e63729b472fcbcc8c45ebcc6691702558b68ec7527e1ba403a0f31a8")
        assertEquals(0, addr2.workchain_id)
        assertEquals("83dfd552e63729b472fcbcc8c45ebcc6691702558b68ec7527e1ba403a0f31a8", hex(addr2.address))

        val addr3 =
            MsgAddressInt.AddrStd.parseRaw("-1:dd24c4a1f2b88f8b7053513b5cc6c5a31bc44b2a72dcb4d8c0338af0f0d37ec5")
        assertEquals(-1, addr3.workchain_id)
        assertEquals("dd24c4a1f2b88f8b7053513b5cc6c5a31bc44b2a72dcb4d8c0338af0f0d37ec5", hex(addr3.address))
    }
}