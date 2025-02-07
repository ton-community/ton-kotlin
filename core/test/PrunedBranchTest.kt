package org.ton.kotlin.cell

class PrunedBranchTest {
    //todo: fix?
//    @Test
//    fun test() {
//        val cell = buildCell {
//            storeByteArray("000000000000000000deafbeaf123123".hexToByteArray())
//            storeRef(Cell.EMPTY)
//        }
//
//        val prunedBranch = CellBuilder.createPrunedBranch(cell, 0)
//        assertEquals(cell.hash(), prunedBranch.hash(0))
//        assertEquals(cell.depth(0), prunedBranch.depth(0))
//
//        val virtualCell = cell.virtualize()
//        assertEquals(cell.hash(), virtualCell.hash())
//        assertEquals(cell.depth(3), virtualCell.depth(3))
//
//        val virtualPrunedBranch = CellBuilder.createPrunedBranch(virtualCell, 0)
//        assertEquals(prunedBranch, virtualPrunedBranch)
//    }
}
