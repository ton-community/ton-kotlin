package org.ton.block.tlb

import org.ton.bigint.BigInt
import org.ton.bitstring.BitString
import org.ton.block.StateInit
import org.ton.cell.BagOfCells
import org.ton.cell.CellBuilder
import org.ton.crypto.hex
import org.ton.hashmap.EmptyHashMapE
import org.ton.tlb.storeTlb
import kotlin.test.Test
import kotlin.test.assertEquals

class StateInitTest {
    private val SIMPLE_WALLET_R3_CODE = BagOfCells(hex("B5EE9C7241010101005F0000BAFF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F260810200D71820D70B1FED44D0D31FD3FFD15112BAF2A122F901541044F910F2A2F80001D31F3120D74A96D307D402FB00DED1A4C8CB1FCBFFC9ED54B5B86E42")).roots.first()
    private val stateInitCodec by lazy { StateInit.tlbCodec() }

    @Test
    fun `serialization of an example wallet StateInit`() {
        val publicKey = "4745EDE03EB4EF607843359C1F206D061A5632F68CAA6F63021AA23B400950FD"
        val hash = "2c49a26a126a6e3a7b1a99c5041698504cbef27fca60a230f2f475954deb07cf"

        val stateInit = StateInit(
            splitDepth = null,
            special = null,
            code = SIMPLE_WALLET_R3_CODE,
            data = CellBuilder.createCell {
                storeUInt(0, 32)
                storeBits(BitString(publicKey))
            },
            library = EmptyHashMapE()
        )

        val stateInitCell = CellBuilder.createCell { storeTlb(stateInitCodec, stateInit) }

        assertEquals(BitString("34_"),  stateInitCell.bits)
        assertEquals(SIMPLE_WALLET_R3_CODE.bits, stateInitCell.refs[0].bits)

        stateInitCell.refs[1].parse {
            assertEquals(BigInt(0), loadUInt(32))
            assertEquals(BitString(publicKey), BitString(loadBits(256).toList()))
        }

        assertEquals(hash, hex(stateInitCell.hash()))
    }
}
