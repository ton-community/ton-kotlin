import io.ktor.util.*
import io.ktor.utils.io.core.*
import org.ton.boc.BagOfCells
import org.ton.boc.readBagOfCell
import org.ton.boc.writeBagOfCells
import org.ton.crypto.base64
import kotlin.test.Test
import kotlin.test.assertEquals

class BagOfCellsTest {
    @Test
    fun serialization() {
        val bytes = base64(
            "te6ccgECGwEAA7IAAnHAAKtVj024T9MfYaJzU1xnDAkf/GGbHNu+V2mgvyjTuP6iNoZbQxKrNWAAAGJfLXQfDWdzUzx000ABAgEU/wD0pBP0vPLICwMBUQAAACYpqaMXyHis2gqgz6zaub/4vKhA59ENikHR7pbK96xkUBavlN/AFgIBIAQFAgFIBgcE+PKDCNcYINMf0x/THwL4I7vyZO1E0NMf0x/T//QE0VFDuvKhUVG68qIF+QFUEGT5EPKj+AAkpMjLH1JAyx9SMMv/UhD0AMntVPgPAdMHIcAAn2xRkyDXSpbTB9QC+wDoMOAhwAHjACHAAuMAAcADkTDjDQOkyMsfEssfy/8SExQVAubQAdDTAyFxsJJfBOAi10nBIJJfBOAC0x8hghBwbHVnvSKCEGRzdHK9sJJfBeAD+kAwIPpEAcjKB8v/ydDtRNCBAUDXIfQEMFyBAQj0Cm+hMbOSXwfgBdM/yCWCEHBsdWe6kjgw4w0DghBkc3RyupJfBuMNCAkCASAKCwB4AfoA9AQw+CdvIjBQCqEhvvLgUIIQcGx1Z4MesXCAGFAEywUmzxZY+gIZ9ADLaRfLH1Jgyz8gyYBA+wAGAIpQBIEBCPRZMO1E0IEBQNcgyAHPFvQAye1UAXKwjiOCEGRzdHKDHrFwgBhQBcsFUAPPFiP6AhPLassfyz/JgED7AJJfA+ICASAMDQBZvSQrb2omhAgKBrkPoCGEcNQICEekk30pkQzmkD6f+YN4EoAbeBAUiYcVnzGEAgFYDg8AEbjJftRNDXCx+AA9sp37UTQgQFA1yH0BDACyMoHy//J0AGBAQj0Cm+hMYAIBIBARABmtznaiaEAga5Drhf/AABmvHfaiaEAQa5DrhY/AAG7SB/oA1NQi+QAFyMoHFcv/ydB3dIAYyMsFywIizxZQBfoCFMtrEszMyXP7AMhAFIEBCPRR8qcCAHCBAQjXGPoA0z/IVCBHgQEI9FHyp4IQbm90ZXB0gBjIywXLAlAGzxZQBPoCFMtqEssfyz/Jc/sAAgBsgQEI1xj6ANM/MFIkgQEI9Fnyp4IQZHN0cnB0gBjIywXLAlAFzxZQA/oCE8tqyx8Syz/Jc/sAAAr0AMntVAIFf8AYFxgAQr+OGwvF382gPpL5tLn/xDhZV3DAaG2RveZ0rWENupvGbgIBSBkaAEG/D4leVvKTP9xffCG8KSkv3wQVtzaLmj7vW9I87TAhJ4oAQb8W/Gj5IwT7STylK13e+rxCohMfPkVEKx8q5FFWspcr6g=="
        )
        val input = ByteReadPacket(bytes)
        val bagOfCell = input.readBagOfCell()

        val serialized = buildPacket {
            writeBagOfCells(bagOfCell)
        }.readBytes()

        assertEquals(bagOfCell, ByteReadPacket(serialized).readBagOfCell())
    }

    @Test
    fun `simple BoC from bytes`() {
        val boc = BagOfCells(hex("b5ee9c72010102010011000118000001010000000000000045010000"))
        assertEquals(1, boc.roots.size)

        assertEquals("000001010000000000000045", hex(boc.roots.first().bits.toByteArray()))
    }
}
