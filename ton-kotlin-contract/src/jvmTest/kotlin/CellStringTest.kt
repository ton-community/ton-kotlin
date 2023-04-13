
import org.ton.cell.buildCell
import org.ton.contract.CellStringTlbConstructor
import org.ton.tl.asByteString
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals

class CellStringTest {
    @Test
    fun test() {
        val byteArray = Random.nextBytes(500).asByteString()
        val cell = buildCell {
            CellStringTlbConstructor.storeTlb(this, byteArray)
        }
        val bytes = CellStringTlbConstructor.loadTlb(cell)
        assertContentEquals(byteArray, bytes)
    }
}
