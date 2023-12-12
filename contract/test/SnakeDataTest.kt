package org.ton.contract

import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.test.Test
import kotlin.test.assertEquals

class SnakeDataTest {
    @Test
    fun serialization() {
        assertEquals(
            Cell.of("BABE"),
            SnakeDataTail(BitString("BABE"))
                .toCell()
        )
        assertEquals(
            Cell.of(
                "C0FFEE",
                Cell.of("BABE")
            ),
            SnakeDataCons(
                BitString("C0FFEE"),
                SnakeDataTail(BitString("BABE"))
            )
                .toCell()
        )
        assertEquals(
            Cell.of(
                "DEAD",
                Cell.of(
                    "BEEF",
                    Cell.of(
                        "C0FFEE",
                        Cell.of("BABE")
                    )
                )
            ),
            SnakeDataCons(
                BitString("DEAD"),
                SnakeDataCons(
                    BitString("BEEF"),
                    SnakeDataCons(
                        BitString("C0FFEE"),
                        SnakeDataTail(BitString("BABE"))
                    )
                )
            )
                .toCell()
        )
    }

    @Test
    fun deserialization() {
        assertEquals(
            SnakeDataTail(BitString("BABE")),
            Cell.of("BABE")
                .toSnakeData(),
        )
        assertEquals(
            SnakeDataCons(
                BitString("C0FFEE"),
                SnakeDataTail(BitString("BABE"))
            ),
            Cell.of(
                "C0FFEE",
                Cell.of("BABE")
            )
                .toSnakeData()
        )
        assertEquals(
            SnakeDataCons(
                BitString("DEAD"),
                SnakeDataCons(
                    BitString("BEEF"),
                    SnakeDataCons(
                        BitString("C0FFEE"),
                        SnakeDataTail(BitString("BABE"))
                    )
                )
            ),
            Cell.of(
                "DEAD",
                Cell.of(
                    "BEEF",
                    Cell.of(
                        "C0FFEE",
                        Cell.of("BABE")
                    )
                )
            )
                .toSnakeData()
        )
    }

    private fun SnakeData.toCell() = CellBuilder.createCell { storeTlb(SnakeData, this@toCell) }
    private fun Cell.toSnakeData() = this.parse { loadTlb(SnakeData) }
}
