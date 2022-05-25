package org.ton.block.tlb

import org.ton.bigint.BigInt
import org.ton.bitstring.BitString
import org.ton.block.VmStackValue
import org.ton.cell.CellBuilder
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class VmStackValue {
    @Test
    fun `test VmStackValue (de)serialization`() {
        testStackValue(VmStackValue.Null)

        testStackValue(VmStackValue.TinyInt(17239))
        testStackValue(VmStackValue.TinyInt(-17))
        testStackValue(VmStackValue.TinyInt(1000000239))
        testStackValue(VmStackValue.TinyInt(1000000239L * 1000000239))
        repeat(10) {
            testStackValue(VmStackValue.TinyInt(Random.nextLong()))
        }

        testStackValue(VmStackValue.Int(17239))
        testStackValue(VmStackValue.Int(-17))
        testStackValue(VmStackValue.Int(1000000239))
        testStackValue(VmStackValue.Int(1000000239L * 1000000239))
        testStackValue(VmStackValue.Int(BigInt("-1000000000000000000000000239")))

        repeat(10) {
            testStackValue(VmStackValue.Int(Random.nextLong()))
            testStackValue(VmStackValue.Int(BigInt(Random.nextBytes(256 / 8))))
        }

        testStackValue(VmStackValue.Cell(
            CellBuilder.createCell {
                storeBits(BitString("989A386C05EFF862FFFFE23_"))
                storeRef {
                    storeBits(BitString("00000001BC16E45E4D41643_"))
                }
                storeRef {
                    storeBits(BitString("3B9ACAEF"))
                    storeRef {
                        storeBits(BitString("FDF_"))
                    }
                }
            }
        ))
    }

    private fun testStackValue(stackValue: VmStackValue) {
        val cellBuilder = CellBuilder.beginCell()
        cellBuilder.storeTlb(stackValue, VmStackValue.tlbCodec())
        val cell = cellBuilder.endCell()

        val cellSlice = cell.beginParse()
        val result = cellSlice.loadTlb(VmStackValue.tlbCodec())
        assertEquals(stackValue, result)
        cellSlice.endParse()
    }
}
