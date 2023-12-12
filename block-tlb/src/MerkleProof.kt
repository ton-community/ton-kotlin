package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellType
import org.ton.tlb.CellRef

@Serializable
@SerialName("merkle_proof")
public data class MerkleProof<X>(
    val virtualHash: BitString,
    val depth: Int,
    val virtualRoot: CellRef<X>
) {
    public companion object {
        public fun virtualize(cell: Cell, offset: Int = 1): Cell {
            require(cell.type == CellType.MERKLE_PROOF) {
                "Invalid cell type, expected: ${CellType.MERKLE_PROOF}, actual: ${cell.type}"
            }
            return cell.refs.first().virtualize(offset)
        }
    }
}
