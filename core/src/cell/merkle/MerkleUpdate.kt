package org.ton.kotlin.cell.merkle

import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.cell.CellRef

public class MerkleUpdate<T>(
    /**
     * Representation hash of the original cell.
     */
    public val oldHash: ByteString,

    /**
     * Representation hash of the updated cell.
     */
    public val newHash: ByteString,

    /**
     * Representation depth of the original cell.
     */
    public val oldDepth: Int,

    /**
     * Representation depth of the updated cell.
     */
    public val newDepth: Int,

    /**
     * Partially pruned tree with unchanged cells of the origin cell.
     */
    public val old: CellRef<T>,

    /**
     * Partially pruned tree with all cells that are not in the original cell.
     */
    public val new: CellRef<T>,
)