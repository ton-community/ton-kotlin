import ton.bitstring.BitString
import ton.cell.Cell
import ton.cell.buildCell
import ton.cell.slice
import ton.tlb.HmLabel
import ton.tlb.Transaction
import ton.tlb.TypeExpression
import ton.tlb.TypeExpressionIntConstant
import kotlin.test.Test
import kotlin.test.assertEquals

class TlbTest {

    @Test
    fun testHmLabelDeserialization() {
        val cs = buildCell {
            writeBit(false)  // hml_short$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;
            writeBits(true, true, true, true, true, true, true, true, false) // Unary 8
            writeUInt(0xfa.toUInt(), 8)
        }.slice()
        val hmLabel = cs.HmLabel({
            assertEquals(8, it.toInt())
        }, TypeExpressionIntConstant(0))
        assertEquals(BitString("FA"), hmLabel["s"].value)
    }

    @Test
    fun testTransactionDeserialization() {
        val cs = Cell(
            "783DFD552E63729B472FCBCC8C45EBCC6691702558B68EC7527E1BA403A0F31A80000178EBA3796472ADF624C1E07A11E874B48C0389A2C6BB3719FF2758796DF45E84D1D06E9A4DA00001788D30F8206621E3321000146032822",
            Cell(
                "A_",
                Cell("48016173957EE1B849B00D183AE2954B185B888152D36268EE2C71389D441F42E5D50020F7F554B98DCA6D1CBF2F323117AF319A45C09562DA3B1D49F86E900E83CC6A0C9BA3C00614586000002F1D746F2C8CC43C664200000000343A3A38399D1797BA1736B297BA37B72FB3393AB4BA39AFB137BA103830BCB7BABA4_")
            ),
            Cell("72CCCDA69F223F0035678D71A9262F28CE9275C2F56D6DE7068299B4CC91758C6AAF146F6619F6BFB457283C19C0F9C7C686BF52FCABD212CB9888451865B3D2E0"),
            Cell(
                "0C835C48C9BA3C186030D411_",
                Cell("27C827D800000000000000000300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"),
                Cell("C00000000000000000000000012D452DA449E50B8CF7DD27861F146122AFE1B546BB8B70FC8216F0C614139F8E04_")
            )
        ).slice()
        val transaction = cs.Transaction()
        assertEquals(
            BitString("83DFD552E63729B472FCBCC8C45EBCC6691702558B68EC7527E1BA403A0F31A8"),
            transaction["account_addr"].value
        )
        assertEquals(25901777000007, transaction["lt"].toLong())
        assertEquals(
            BitString("2ADF624C1E07A11E874B48C0389A2C6BB3719FF2758796DF45E84D1D06E9A4DA"),
            transaction["prev_trans_hash"].value
        )
        assertEquals(25876424000006, transaction["prev_trans_lt"].toLong())
        assertEquals(1646146337, transaction["now"].toLong())
        assertEquals(0, transaction["outmsg_cnt"].toInt())
        assertEquals("acc_state_active", transaction["orig_status"].value)
        assertEquals("acc_state_active", transaction["end_status"].value)
        assertEquals(3, transaction["total_fees"]["grams"]["amount"]["len"].toInt())
        assertEquals(103441, transaction["total_fees"]["grams"]["amount"]["value"].toInt())
        assertEquals("hme_empty", transaction["total_fees"]["other"]["dict"].value)
    }
}