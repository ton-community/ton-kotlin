package ton.tlb

import ton.cell.CellReader
import ton.cell.CellWriter

private fun Int.fitsBitsLeq() = Int.SIZE_BITS - countLeadingZeroBits()
private fun Int.fitsBitsLes() = Int.SIZE_BITS - (this - 1).countLeadingZeroBits()

fun CellReader.readIntLeq(max: Int) = readInt(max.fitsBitsLeq())
fun CellWriter.writeIntLeq(value: Int, max: Int) = writeInt(value, max.fitsBitsLeq())

fun CellReader.readIntLes(max: Int) = readInt(max.fitsBitsLes())
fun CellWriter.writeIntLes(value: Int, max: Int) = writeInt(value, max.fitsBitsLes())