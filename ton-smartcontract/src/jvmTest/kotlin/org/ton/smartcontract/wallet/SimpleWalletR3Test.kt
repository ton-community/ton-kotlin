package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.Message
import org.ton.block.StateInit
import org.ton.block.tlb.tlbCodec
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.crypto.base64
import org.ton.crypto.hex
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class SimpleWalletR3Test {
    private val privateKey = PrivateKeyEd25519(ByteArray(32))

    private fun liteClient() = LiteClient(
        ipv4 = 1426768764,
        port = 13724,
        publicKey = base64("R1KsqYlNks2Zows+I9s4ywhilbSevs9dH1x2KF9MeSU="),
        logger = PrintLnLogger("TON SimpleWalletR3", Logger.Level.DEBUG)
    )

    private fun wallet() = SimpleWalletR3(liteClient(), privateKey)

    @Test
    fun `test private key`() {
        val actual = hex(privateKey.key)
        val expected = "0000000000000000000000000000000000000000000000000000000000000000"
        assertEquals(expected, actual)
    }

    @Test
    fun `test public key`() {
        val actual = hex(privateKey.publicKey().key)
        val expected = "3b6a27bcceb6a42d62a3a8d02a6f0d73653215771de243a63ac048a18b59da29"
        assertEquals(expected, actual)
    }

    @Test
    fun `test StateInit Cell`() {
        val wallet = wallet()
        val actual = CellBuilder.createCell {
            storeTlb(StateInit.tlbCodec(), wallet.createStateInit())
        }
        val expected = Cell(
            "34_",
            Cell("FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F260810200D71820D70B1FED44D0D31FD3FFD15112BAF2A122F901541044F910F2A2F80001D31F3120D74A96D307D402FB00DED1A4C8CB1FCBFFC9ED54"),
            Cell("000000003B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29")
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `test raw address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(userFriendly = false, testOnly = true)
        val expected = "0:41d1632383c5c5337cbb2d32200e7035a83d03bf1721370e075ce86fe22de3d6"
        assertEquals(expected, actual)
    }

    @Test
    fun `test non-bounceable address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(bounceable = false, testOnly = true)
        val expected = "0QBB0WMjg8XFM3y7LTIgDnA1qD0DvxchNw4HXOhv4i3j1pZk"
        assertEquals(expected, actual)
    }

    @Test
    fun `test bounceable address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(bounceable = true, testOnly = true)
        val expected = "kQBB0WMjg8XFM3y7LTIgDnA1qD0DvxchNw4HXOhv4i3j1suh"
        assertEquals(expected, actual)
    }

    @Test
    fun `test external message for initialization`() {
        val wallet = wallet()
        val actual = CellBuilder.createCell {
            storeTlb(Message.tlbCodec(AnyTlbConstructor), wallet.createExternalInitMessage())
        }
        val expected = Cell(
            "880083A2C647078B8A66F9765A64401CE06B507A077E2E426E1C0EB9D0DFC45BC7AC11985B73C78DD0FEF1AD628FEEBA22CE006DDD70361E50F671607A41713AD2373BD9C24DAC2CB48BF838599E15897E063F5D528E5BDC150D40E28E78AFF0C798C0800000001_",
            Cell("FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F260810200D71820D70B1FED44D0D31FD3FFD15112BAF2A122F901541044F910F2A2F80001D31F3120D74A96D307D402FB00DED1A4C8CB1FCBFFC9ED54"),
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
        val expected =
            hex("b5ee9c724101030100f10002cf880083a2c647078b8a66f9765a64401ce06b507a077e2e426e1c0eb9d0dfc45bc7ac11985b73c78dd0fef1ad628feeba22ce006ddd70361e50f671607a41713ad2373bd9c24dac2cb48bf838599e15897e063f5d528e5bdc150d40e28e78aff0c798c08000000010010200baff0020dd2082014c97ba218201339cbab19c71b0ed44d0d31fd70bffe304e0a4f260810200d71820d70b1fed44d0d31fd3ffd15112baf2a122f901541044f910f2a2f80001d31f3120d74a96d307d402fb00ded1a4c8cb1fcbffc9ed540048000000003b6a27bcceb6a42d62a3a8d02a6f0d73653215771de243a63ac048a18b59da296d304c45")
        assertContentEquals(expected, actual)
    }
}
