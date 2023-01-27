import org.junit.Test
import org.ton.boc.BagOfCells
import org.ton.crypto.hex
import org.ton.hashmap.HmEdge
import org.ton.tlb.constructor.AnyTlbConstructor

class TestDict {
    @Test
    fun test() {
        val cell =
            BagOfCells(hex(runCmd("fift -I /usr/local/lib/fift/ /Users/andreypfau/IdeaProjects/ton-kotlin/ton-kotlin-hashmap/src/jvmTest/resources/test.fif"))).first()
        val codec = HmEdge.tlbCodec(4, AnyTlbConstructor)
        val map = codec.loadTlb(cell)
        println(map.toString())
        map.forEach {
            println("${it.first.toBinary()} = ${it.second.bits.toBinary()}")
        }
    }
}

private fun runCmd(cmd: String): String {
    return Runtime.getRuntime().exec(cmd).inputStream.use {
        it.readBytes().decodeToString().also {
            println("cmd result: $it")
        }
    }
}
