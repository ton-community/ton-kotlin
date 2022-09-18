package org.ton.vm

import org.ton.block.VmCont
import org.ton.block.VmContQuitExc
import org.ton.block.VmTuple
import org.ton.block.VmTupleNil
import org.ton.cell.Cell

data class VmControlRegistry(
    var c0: VmCont = VmContQuitExc,
    var c1: VmCont = VmContQuitExc,
    var c2: VmCont = VmContQuitExc,
    var c3: VmCont = VmContQuitExc,
    var c4: Cell = Cell.empty(),
    var c5: Cell = Cell.empty(),
    var c7: VmTuple = VmTupleNil
)
