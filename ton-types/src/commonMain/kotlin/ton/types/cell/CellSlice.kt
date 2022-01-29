package ton.types.cell

data class CellSlice(
    val cell: Cell = Cell(),
    val dataWindow: IntRange = 0..0,
    val referencesWindow: IntRange = 0..0,
)