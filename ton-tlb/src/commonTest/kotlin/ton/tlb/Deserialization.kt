//package ton.tlb
//
//import ton.bitstring.BitString
//import ton.tlb.old.TlbDecoder
//import ton.tlb.old.types.NegatedTypeExpression
//import ton.tlb.old.types.TypeCombinator
//import ton.tlb.old.types.constructor
//import ton.tlb.old.types.field
//import kotlin.test.Test
//
//class Deserialization {
//
//    @Test
//    fun testDecode() {
//        // addr_extern$01 len:(## 9) external_address:(bits len) = MsgAddressExt;
//        val decoder = TlbDecoder(Cell(BitString(1024) {
//            writeBits(false, true)
//            writeUInt(12u, 9)
//            writeBitString(BitString(12).also { bs ->
//                repeat(12) {
//                    bs.writeBit()
//                }
//            })
//        }))
//
//        val result = decoder.decode(decoder.MsgAddressExt())
//        println("decoded: $result")
//    }
//
//    @Test
//    fun testParseData() {
//        val cell = Cell(
//            "783DFD552E63729B472FCBCC8C45EBCC6691702558B68EC7527E1BA403A0F31A80000178EBA3796472ADF624C1E07A11E874B48C0389A2C6BB3719FF2758796DF45E84D1D06E9A4DA00001788D30F8206621E3321000146032822",
//            Cell(
//                "A_",
//                Cell("48016173957EE1B849B00D183AE2954B185B888152D36268EE2C71389D441F42E5D50020F7F554B98DCA6D1CBF2F323117AF319A45C09562DA3B1D49F86E900E83CC6A0C9BA3C00614586000002F1D746F2C8CC43C664200000000343A3A38399D1797BA1736B297BA37B72FB3393AB4BA39AFB137BA103830BCB7BABA4_")
//            ),
//            Cell("72CCCDA69F223F0035678D71A9262F28CE9275C2F56D6DE7068299B4CC91758C6AAF146F6619F6BFB457283C19C0F9C7C686BF52FCABD212CB9888451865B3D2E0"),
//            Cell(
//                "0C835C48C9BA3C186030D411_",
//                Cell("27C827D800000000000000000300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"),
//                Cell("C00000000000000000000000012D452DA449E50B8CF7DD27861F146122AFE1B546BB8B70FC8216F0C614139F8E04_")
//            )
//        )
//
//        val decoder = TlbDecoder(cell)
//        val result = decoder.decodeToJson(decoder.Transaction())
//
//        println(result)
//    }
//
//    @Test
//    fun testUnary() {
//        val cell = Cell(BitString(true, true, true, false))
//        val decoder = TlbDecoder(cell)
//        val result = decoder.decodeToJson(decoder.TestType())
//        println(result)
//    }
//
//    fun TlbDecoder.TestType() = TypeCombinator("TestType") {
//        constructor("testtype") {
//            val n = NegatedTypeExpression<Int>()
//            field("x", Unary(n))
//            field("y", n)
//        }
//    }
//}