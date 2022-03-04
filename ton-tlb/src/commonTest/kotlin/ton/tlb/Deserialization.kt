package ton.tlb

import ton.bitstring.BitString
import ton.tlb.types.AddrExtern
import ton.tlb.types.AddrNone
import ton.tlb.types.MsgAddressExt
import kotlin.test.Test
import kotlin.test.assertEquals

class Deserialization {

    @Test
    fun test() {
        val addrNone: MsgAddressExt = Tlb.decodeFromBitString(BitString(false, false))
        assertEquals(AddrNone, addrNone)
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
        assertEquals(AddrExtern(12, BitString(12).also { bs ->
            repeat(12) {
                bs.writeBit()
            }
        }), addrExtern)
    }

    @Test
    fun testDecode() {
        // addr_extern$01 len:(## 9) external_address:(bits len) = MsgAddressExt;
        val decoder = TlbDecoder(Cell(BitString(1024) {
            writeBits(false, true)
            writeUInt(12u, 9)
            writeBitString(BitString(12).also { bs ->
                repeat(12) {
                    bs.writeBit()
                }
            })
        }))

        val result = decoder.decode(decoder.MsgAddressExt())
        println("decoded: $result")
    }

    @Test
    fun testParseData() {
        val cell = Cell(
            "783DFD552E63729B472FCBCC8C45EBCC6691702558B68EC7527E1BA403A0F31A80000178EBA3796472ADF624C1E07A11E874B48C0389A2C6BB3719FF2758796DF45E84D1D06E9A4DA00001788D30F8206621E3321000146032822",
            Cell(
                "A_",
                Cell("48016173957EE1B849B00D183AE2954B185B888152D36268EE2C71389D441F42E5D50020F7F554B98DCA6D1CBF2F323117AF319A45C09562DA3B1D49F86E900E83CC6A100487AB000614586000002F24F6CDA38AC43DF8A000000000343A3A38399D1797BA1736B297BA37B72FB3393AB4BA39AFB137BA103830BCB7BABA4_")
            ),
            Cell("72B3894B2A29B4D7348ADE9D178977D08D938D715F93AE1A98723EBF60DF508F175F25E0EBB0DE12CD5B651989F2AA84E140072A7DFBEF010740E504DFA616A322"),
            Cell(
                "0C81828900487AB0186030D411_",
                Cell("27C928E000000000000000000300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"),
                Cell("C00000000000000000000000012D452DA449E50B8CF7DD27861F146122AFE1B546BB8B70FC8216F0C614139F8E04")
            )
        )

        println(cell.debug())

        val decoder = TlbDecoder(cell)
        val result = decoder.decode(decoder.Transaction())
        println("==================")
        println(result)
    }
}