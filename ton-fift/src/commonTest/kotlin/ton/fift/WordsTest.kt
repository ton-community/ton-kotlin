package ton.fift

import ton.types.int257.int257
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun testFift(block: FiftInterpretator.() -> Unit) {
    val fift = FiftInterpretator(output = {})
    fift.apply(block)
    assertTrue(fift.stack.isEmpty)
}

class WordsTest {
    @Test
    fun testBasicArithmetic() = testFift {
        interpret("7 4 -")
        assertEquals(int257(3), stack.popInt257())
        interpret("2 3 4 * +")
        interpret("2 3 + 4 *")
        assertEquals(int257(20), stack.popInt257())
        assertEquals(int257(14), stack.popInt257())
    }


    @Test
    fun `test word '+ '`() = testFift {
        interpret("1 2 +")
        assertEquals(int257(3), stack.popInt257())
        interpret("5 -10 +")
        assertEquals(int257(-5), stack.popInt257())
        interpret("42 37 94 + + 11 +")
        assertEquals(int257(184), stack.popInt257())
        assertTrue(stack.isEmpty)
    }

    @Test
    fun `test word '- '`() = testFift {
        interpret("5 3 -")
        assertEquals(int257(2), stack.popInt257())
        interpret("-10 -3 -")
        assertEquals(int257(-7), stack.popInt257())
        interpret("-19 397 -24 - - 33 -")
        assertEquals(int257(-473), stack.popInt257())
        assertTrue(stack.isEmpty)
    }

    @Test
    fun `test word 'negate '`() = testFift {
        interpret("53 negate")
        assertEquals(int257(-53), stack.popInt257())
        interpret("-10 negate")
        assertEquals(int257(10), stack.popInt257())
        interpret("397 -24 negate negate 99 negate 11")
        assertEquals(int257(11), stack.popInt257())
        assertEquals(int257(-99), stack.popInt257())
        assertEquals(int257(-24), stack.popInt257())
        assertEquals(int257(397), stack.popInt257())
    }

    @Test
    fun testDup() = testFift {
        interpret("1 2 dup")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
    }

    @Test
    fun testDrop() = testFift {
        interpret("3 1 2 drop")
        assertEquals(int257(1), stack.popInt257())
        interpret("drop")
    }

    @Test
    fun testSwap() = testFift {
        interpret("1 2 3 swap")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(3), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
    }

    @Test
    fun testRot() = testFift {
        interpret("1 2 3 rot")
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(3), stack.popInt257())
        assertEquals(int257(2), stack.popInt257())
    }

    @Test
    fun testNegRot() = testFift {
        interpret("1 2 3 -rot")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(3), stack.popInt257())
        interpret("1 2 3 rot rot")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(3), stack.popInt257())
    }

    @Test
    fun testOver() = testFift {
        interpret("1 2 over")
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
    }

    @Test
    fun testTuck() = testFift {
        interpret("1 2 tuck")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(2), stack.popInt257())
        interpret("1 2 swap over")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(2), stack.popInt257())
    }

    @Test
    fun testNip() = testFift {
        interpret("1 2 nip")
        assertEquals(int257(2), stack.popInt257())
        interpret("1 2 swap drop")
        assertEquals(int257(2), stack.popInt257())
    }

    @Test
    fun test2dup() = testFift {
        interpret("1 2 2dup")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
        interpret("1 2 over over")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
    }

    @Test
    fun test2drop() = testFift {
        interpret("1 2 2drop")
        assertTrue(stack.isEmpty)
        interpret("1 2 drop drop")
        assertTrue(stack.isEmpty)
    }

    @Test
    fun test2swap() = testFift {
        interpret("1 2 3 4 2swap")
        assertEquals(int257(2), stack.popInt257())
        assertEquals(int257(1), stack.popInt257())
        assertEquals(int257(4), stack.popInt257())
        assertEquals(int257(3), stack.popInt257())
    }

    @Test
    fun testPick() = testFift {
        interpret("1 2 3")
        interpret("0 pick")
        assertEquals(int257(3), stack.popInt257())
        interpret("1 pick")
        assertEquals(int257(2), stack.popInt257())
        interpret("2 pick")
        assertEquals(int257(1), stack.popInt257())
        repeat(stack.depth) {
            stack.pop()
        }
    }
}