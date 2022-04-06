package ton.crypto.curve25519

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FieldTest {
    @Test
    fun `a mul a vs a squared constant`() {

    }

    @Test
    fun sqrt_ratio_behavior() {
        val zero = FieldElement.zero()
        val one = FieldElement.one()
        val i = CONSTANTS.SQRT_M1
        val two = one + one // 2 is nonsquare mod p.
        val four = two + two // 4 is square mod p.

        // 0/0 should return (1, 0) since u is 0
        val (result1, sqrt1) = FieldElement.sqrtRatioI(zero, zero)
        assertTrue(result1)
        assertEquals(zero, sqrt1)
        assertFalse(sqrt1.isNegative)

        // 1/0 should return (0, 0) since v is 0, u is nonzero
        val (result2, sqrt2) = FieldElement.sqrtRatioI(one, zero)
        assertFalse(result2)
        assertEquals(zero, sqrt2)
        assertFalse(sqrt2.isNegative)

        // 2/1 is nonsquare, so we expect (0, sqrt(i*2))
        val (result3, sqrt3) = FieldElement.sqrtRatioI(two, one)
        assertFalse(result3)
        assertEquals(two * i, sqrt3.square())
        assertFalse(sqrt3.isNegative)

        // 4/1 is square, so we expect (1, sqrt(4))
        val (result4, sqrt4) = FieldElement.sqrtRatioI(four, one)
        assertTrue(result4)
        assertEquals(four, sqrt4.square())
        assertFalse(sqrt4.isNegative)

        // 1/4 is square, so we expect (1, 1/sqrt(4))
        val (result5, sqrt5) = FieldElement.sqrtRatioI(one, four)
        assertTrue(result5)
        assertEquals(one, sqrt5.square() * four)
        assertFalse(sqrt5.isNegative)
    }

    @Test
    fun encoding_is_canonical() {
        // Encode 1 wrongly as 1 + (2^255 - 19) = 2^255 - 18
        val one_encoded_wrongly_bytes = ByteArray(32) { 0xff.toByte() }
        one_encoded_wrongly_bytes[0] = 0xee.toByte()
        one_encoded_wrongly_bytes[31] = 0x7f.toByte()
        // Decode to a field element
        val one = FieldElement.fromBytes(one_encoded_wrongly_bytes)
        // .. then check that the encoding is correct
        val oneBytes = one.toBytes()
        assertEquals(1, oneBytes[0])
        for (i in 1..32) {
            assertEquals(oneBytes[i], 0)
        }
    }

    @Test
    fun batch_invert_empty() {
        FieldElement.batchInvert(emptyArray())
    }
}