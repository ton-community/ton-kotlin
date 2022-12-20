package org.ton.contract.wallet.v3

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

class WalletV3R2Test {
    private val privateKey = PrivateKeyEd25519(ByteArray(32))

    private fun wallet() = ContractV3R2(liteClient().liteApi, privateKey)

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
            Cell("FF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED54"),
            Cell("0000000029A9A3173B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29")
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `test raw address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(userFriendly = false, testOnly = true)
        val expected = "0:0A3404CB320CFF55C5DCD4FABE3F3C9841B1D57D0E77990094BB1116EE508E3F"
        assertEquals(expected, actual)
    }

    @Test
    fun `test non-bounceable address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(bounceable = false, testOnly = true)
        val expected = "0QAKNATLMgz_VcXc1Pq-PzyYQbHVfQ53mQCUuxEW7lCOP26D"
        assertEquals(expected, actual)
    }

    @Test
    fun `test bounceable address`() {
        val wallet = wallet()
        val address = wallet.address()
        val actual = address.toString(bounceable = true, testOnly = true)
        val expected = "kQAKNATLMgz_VcXc1Pq-PzyYQbHVfQ53mQCUuxEW7lCOPzNG"
        assertEquals(expected, actual)
    }

    @Test
    fun `test external message for initialization`() {
        val wallet = wallet()
        val actual = CellBuilder.createCell {
            storeTlb(Message.tlbCodec(AnyTlbConstructor), wallet.createExternalInitMessage())
        }
        val expected = Cell(
            "8800146809966419FEAB8BB9A9F57C7E79308363AAFA1CEF32012976222DDCA11C7E118BF85DFD9E89ED889B0D5E7B84E5DD2E9560AF5720A1133C02D183D331E7188220756A7A35742D3F5EDCC128E48E8733B614963CC261B948E077228DE6C3684125353462FFFFFFFFE00000001_",
            Cell("FF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED54"),
            Cell("0000000029A9A3173B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29")
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
        // TODO: CRC32C https://github.com/andreypfau/ton-kotlin/issues/20
        //  val expected = hex("B5EE9C724102030100010F0002DF8800146809966419FEAB8BB9A9F57C7E79308363AAFA1CEF32012976222DDCA11C7E118BF85DFD9E89ED889B0D5E7B84E5DD2E9560AF5720A1133C02D183D331E7188220756A7A35742D3F5EDCC128E48E8733B614963CC261B948E077228DE6C3684125353462FFFFFFFFE000000010010200DEFF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED5400500000000029A9A3173B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA297A25CA8B")
        val expected =
            hex("B5EE9C720102030100010F0002DF8800146809966419FEAB8BB9A9F57C7E79308363AAFA1CEF32012976222DDCA11C7E118BF85DFD9E89ED889B0D5E7B84E5DD2E9560AF5720A1133C02D183D331E7188220756A7A35742D3F5EDCC128E48E8733B614963CC261B948E077228DE6C3684125353462FFFFFFFFE000000010010200DEFF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED5400500000000029A9A3173B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29")
        assertContentEquals(expected, actual)
    }
}
