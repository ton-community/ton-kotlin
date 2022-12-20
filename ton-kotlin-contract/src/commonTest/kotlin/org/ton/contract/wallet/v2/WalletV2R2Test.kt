package org.ton.contract.wallet.v2

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.Message
import org.ton.block.StateInit
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.contract.wallet.liteClient
import org.ton.crypto.hex
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class WalletV2R2Test {
    private val privateKey = PrivateKeyEd25519(ByteArray(32))

    private fun wallet() = ContractV2R2(liteClient().liteApi, privateKey)

    @Test
    fun `test private key`() {
        val actual = hex(privateKey.key.toByteArray())
        val expected = "0000000000000000000000000000000000000000000000000000000000000000"
        assertEquals(expected, actual)
    }

    @Test
    fun `test public key`() {
        val actual = hex(privateKey.publicKey().key.toByteArray())
        val expected = "3b6a27bcceb6a42d62a3a8d02a6f0d73653215771de243a63ac048a18b59da29"
        assertEquals(expected, actual)
    }

    @Test
    fun `test StateInit Cell`() {
        val wallet = wallet()
        val actual = CellBuilder.createCell {
            storeTlb(StateInit.tlbCodec(), wallet.createStateInit())
        }
        println(actual)
        val expected = Cell(
            "34_",
            Cell("FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F2608308D71820D31FD31F01F823BBF263ED44D0D31FD3FFD15131BAF2A103F901541042F910F2A2F800029320D74A96D307D402FB00E8D1A4C8CB1FCBFFC9ED54"),
            Cell("000000003B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29")
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `test raw address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(userFriendly = false, testOnly = true)
        val expected = "0:F31EC5CC91A9A225EA822DCACD0D4BB2067EFC26BECD51CC74EF95A111FA883E"
        assertEquals(expected, actual)
    }

    @Test
    fun `test non-bounceable address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(bounceable = false, testOnly = true)
        val expected = "0QDzHsXMkamiJeqCLcrNDUuyBn78Jr7NUcx075WhEfqIPsHj"
        assertEquals(expected, actual)
    }

    @Test
    fun `test bounceable address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(bounceable = true, testOnly = true)
        val expected = "kQDzHsXMkamiJeqCLcrNDUuyBn78Jr7NUcx075WhEfqIPpwm"
        assertEquals(expected, actual)
    }

    @Test
    fun `test external message for initialization`() {
        val wallet = wallet()
        val actual = CellBuilder.createCell {
            storeTlb(Message.tlbCodec(AnyTlbConstructor), wallet.createExternalInitMessage())
        }
        val expected = Cell(
            "8801E63D8B992353444BD5045B959A1A97640CFDF84D7D9AA398E9DF2B4223F5107C118D4B49B11CA8E15107EF66723DF4ED3EAB59F6BBF2708C8F38B1DD692D3C47B7ABFF5A4F939080A8C2D056B4C76F15A18100F935030281940F356BD5886B2441C00000001FFFFFFFF_",
            Cell("FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F2608308D71820D31FD31F01F823BBF263ED44D0D31FD3FFD15131BAF2A103F901541042F910F2A2F800029320D74A96D307D402FB00E8D1A4C8CB1FCBFFC9ED54"),
            Cell("000000003B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29")
        )
        assertEquals(actual, expected)
    }

    @Test
    fun `test deploy boc`() {
        val wallet = wallet()
        val message = wallet.createExternalInitMessage()
        val actual =
            BagOfCells(CellBuilder.createCell {
                storeTlb(Message.tlbCodec(AnyTlbConstructor), message)
            }).toByteArray()
        println(hex(actual))
        // TODO: CRC32C https://github.com/andreypfau/ton-kotlin/issues/20
        //  val expected = hex("B5EE9C724101030100F90002D78801E63D8B992353444BD5045B959A1A97640CFDF84D7D9AA398E9DF2B4223F5107C118D4B49B11CA8E15107EF66723DF4ED3EAB59F6BBF2708C8F38B1DD692D3C47B7ABFF5A4F939080A8C2D056B4C76F15A18100F935030281940F356BD5886B2441C00000001FFFFFFFF0010200C2FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F2608308D71820D31FD31F01F823BBF263ED44D0D31FD3FFD15131BAF2A103F901541042F910F2A2F800029320D74A96D307D402FB00E8D1A4C8CB1FCBFFC9ED540048000000003B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA293CCA9E76")
        val expected =
            hex("B5EE9C720101030100F90002D78801E63D8B992353444BD5045B959A1A97640CFDF84D7D9AA398E9DF2B4223F5107C118D4B49B11CA8E15107EF66723DF4ED3EAB59F6BBF2708C8F38B1DD692D3C47B7ABFF5A4F939080A8C2D056B4C76F15A18100F935030281940F356BD5886B2441C00000001FFFFFFFF0010200C2FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F2608308D71820D31FD31F01F823BBF263ED44D0D31FD3FFD15131BAF2A103F901541042F910F2A2F800029320D74A96D307D402FB00E8D1A4C8CB1FCBFFC9ED540048000000003B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29")
        assertContentEquals(expected, actual)
    }
}
