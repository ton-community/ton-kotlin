package org.ton.block

import org.ton.bigint.BigInt
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import kotlin.random.Random
import kotlin.test.Test

class VmStackValueTest {
    val codec = VmStackValue.tlbCodec()

    @Test
    fun `test VmStackValue (de)serialization`() {

        testSerialization(codec, VmStackValue.Null)

        testSerialization(codec, VmStackValue.TinyInt(17239))
        testSerialization(codec, VmStackValue.TinyInt(-17))
        testSerialization(codec, VmStackValue.TinyInt(1000000239))
        testSerialization(codec, VmStackValue.TinyInt(1000000239L * 1000000239))
        repeat(10) {
            testSerialization(codec, VmStackValue.TinyInt(Random.nextLong()))
        }

        testSerialization(codec, VmStackValue.Int(17239))
        testSerialization(codec, VmStackValue.Int(-17))
        testSerialization(codec, VmStackValue.Int(1000000239))
        testSerialization(codec, VmStackValue.Int(1000000239L * 1000000239))
        testSerialization(codec, VmStackValue.Int(BigInt("-1000000000000000000000000239")))

        repeat(10) {
            testSerialization(codec, VmStackValue.Int(Random.nextLong()))
            testSerialization(codec, VmStackValue.Int(BigInt(Random.nextBytes(256 / 8))))
        }

        testSerialization(
            codec, VmStackValue.Cell(
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


}
