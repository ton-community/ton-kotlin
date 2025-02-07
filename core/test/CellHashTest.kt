package org.ton.kotlin.cell

import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class CellHashTest {
    @Test
    fun testCellHash() {
        val c0 = CellBuilder.beginCell().endCell()
        assertHex("96a296d224f285c67bee93c30f8a309157f0daa35dc5b87e410b78630a09cfc7", c0.hash())

        val c1 = CellBuilder.beginCell().storeUInt(0u, 2).endCell()
        assertHex("a1bb2a842d54edb8942f95bedaf53923d2d788d698232cfb256571e9e8b10a86", c1.hash())

        val c2 = CellBuilder.beginCell().storeUInt(0x5f21u, 15).endCell()
        assertHex("7c617d77cf0f8561eb82bede8fcc95e728710b4c31510699eb78b5793fd8c6c2", c2.hash())
    }

    @Test
    fun testNestedCellHash() {
        val c1 = CellBuilder.beginCell().storeUInt(42u, 7).endCell()
        assertHex("9184089c2c7fe2f12874575da31cf5d15ea91a3b7b5e41e910d4ccf935bf0a76", c1.hash())

        val c2 = CellBuilder.beginCell().storeUInt(12u, 8).storeRef(c1).endCell()
        assertHex("13f640f9f30969b9f7b6d51a6ad277e719bc93c792c81e14cf9cddd7b387ff47", c2.hash())

        val c3 = CellBuilder.beginCell().storeUInt(13u, 8).storeRef(c1).storeRef(c2).endCell()
        assertHex("6c2f0317132aad2b120968921bac0e3788b7588cc6ff470946e3ada3430d3338", c3.hash())
    }

    private fun assertHex(expected: String, actual: ByteString) {
        assertEquals(expected, actual.toHexString())
    }
}