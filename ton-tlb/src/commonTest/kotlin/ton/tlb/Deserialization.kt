package ton.tlb

import ton.bitstring.BitString
import ton.tlb.types.MsgAddressExt
import kotlin.test.Test
import kotlin.test.assertEquals

class Deserialization {

    @Test
    fun test() {
        val addrNone: MsgAddressExt = Tlb.decodeFromBitString(BitString(false, false))
        assertEquals(MsgAddressExt.AddrNone, addrNone)
        val addrExtern: MsgAddressExt = Tlb.decodeFromBitString(BitString(1024).apply {
            writeBit(false)
            writeBit(true)
            writeInt(12, 9)
            writeBitString(BitString(12).also { bs ->
                repeat(12) {
                    bs.writeBit()
                }
            })
        })
        assertEquals(MsgAddressExt.AddrExtern(12, BitString(12).also { bs ->
            repeat(12) {
                bs.writeBit()
            }
        }), addrExtern)
    }

    @Test
    fun a() {
        val b = BitString(64)
        b.writeInt(14, 9)
        val r = BitStringReader(b)
        println("result: ${r.readInt(9)}")
    }
}