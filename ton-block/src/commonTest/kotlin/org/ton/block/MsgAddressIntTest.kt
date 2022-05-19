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

    @Test
    fun `parse user-friendly base64(url) addresses`() {
        val bounceableAddr1 =
            MsgAddressInt.AddrStd.parseUserFriendly("Ef8zMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzM0vF")
        assertEquals(-1, bounceableAddr1.workchain_id)
        assertEquals("3333333333333333333333333333333333333333333333333333333333333333", hex(bounceableAddr1.address))

        val nonBounceableAddr1 =
            MsgAddressInt.AddrStd.parseUserFriendly("Uf8zMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMxYA")
        assertEquals(-1, nonBounceableAddr1.workchain_id)
        assertEquals(
            "3333333333333333333333333333333333333333333333333333333333333333",
            hex(nonBounceableAddr1.address)
        )

        assertEquals(bounceableAddr1, nonBounceableAddr1)

        val bounceableAddr2 =
            MsgAddressInt.AddrStd.parseUserFriendly("EQCD39VS5jcptHL8vMjEXrzGaRcCVYto7HUn4bpAOg8xqB2N")
        assertEquals(0, bounceableAddr2.workchain_id)
        assertEquals("83dfd552e63729b472fcbcc8c45ebcc6691702558b68ec7527e1ba403a0f31a8", hex(bounceableAddr2.address))

        val nonBounceableAddr2 =
            MsgAddressInt.AddrStd.parseUserFriendly("UQCD39VS5jcptHL8vMjEXrzGaRcCVYto7HUn4bpAOg8xqEBI")
        assertEquals(0, nonBounceableAddr2.workchain_id)
        assertEquals(
            "83dfd552e63729b472fcbcc8c45ebcc6691702558b68ec7527e1ba403a0f31a8",
            hex(nonBounceableAddr2.address)
        )

        assertEquals(bounceableAddr2, nonBounceableAddr2)


        val bounceableAddr3 =
            MsgAddressInt.AddrStd.parseUserFriendly("Ef_dJMSh8riPi3BTUTtcxsWjG8RLKnLctNjAM4rw8NN-xWdr")
        assertEquals(-1, bounceableAddr3.workchain_id)
        assertEquals("dd24c4a1f2b88f8b7053513b5cc6c5a31bc44b2a72dcb4d8c0338af0f0d37ec5", hex(bounceableAddr3.address))

        val nonBounceableAddr3 =
            MsgAddressInt.AddrStd.parseUserFriendly("Uf_dJMSh8riPi3BTUTtcxsWjG8RLKnLctNjAM4rw8NN-xTqu")
        assertEquals(-1, nonBounceableAddr3.workchain_id)
        assertEquals(
            "dd24c4a1f2b88f8b7053513b5cc6c5a31bc44b2a72dcb4d8c0338af0f0d37ec5",
            hex(nonBounceableAddr3.address)
        )

        assertEquals(bounceableAddr3, nonBounceableAddr3)

        val bounceableAddr4 =
            MsgAddressInt.AddrStd.parseUserFriendly("Ef/dJMSh8riPi3BTUTtcxsWjG8RLKnLctNjAM4rw8NN+xWdr")
        assertEquals(-1, bounceableAddr4.workchain_id)
        assertEquals("dd24c4a1f2b88f8b7053513b5cc6c5a31bc44b2a72dcb4d8c0338af0f0d37ec5", hex(bounceableAddr4.address))

        val nonBounceableAddr4 =
            MsgAddressInt.AddrStd.parseUserFriendly("Uf/dJMSh8riPi3BTUTtcxsWjG8RLKnLctNjAM4rw8NN+xTqu")
        assertEquals(-1, nonBounceableAddr4.workchain_id)
        assertEquals(
            "dd24c4a1f2b88f8b7053513b5cc6c5a31bc44b2a72dcb4d8c0338af0f0d37ec5",
            hex(nonBounceableAddr4.address)
        )

        assertEquals(bounceableAddr4, nonBounceableAddr4)
    }
}