package org.ton.kotlin.account

import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.cell.Cell
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.dict.Dictionary
import org.ton.kotlin.dict.RawDictionary

/**
 * Deployed account state.
 */
public data class StateInit(
    /**
     * Optional split depth for large smart contracts.
     */
    val splitDepth: SplitDepth? = null,

    /**
     * Optional special contract flags.
     */
    val special: TickTock? = null,

    /**
     * Optional contract code.
     */
    val code: Cell? = null,

    /**
     * Optional contract data.
     */
    val data: Cell? = null,

    /**
     * Libraries used in smart-contract.
     */
    val libraries: Dictionary<ByteString, SimpleLib>? = null,
) {
    public constructor(
        code: Cell? = null,
        data: Cell? = null,
        library: Dictionary<ByteString, SimpleLib>,
        splitDepth: SplitDepth? = null,
        special: TickTock? = null
    ) : this(
        splitDepth,
        special,
        code,
        data,
        library
    )

    /**
     * Number of data bits that this struct occupies.
     */
    val bitLength: Int
        get() =
            1 + (if (splitDepth != null) 1 else 0) * SplitDepth.BITS +
                    (1 + (if (special != null) 1 else 0) * TickTock.BITS) +
                    3

    /**
     * Returns the number of references that this struct occupies.
     */
    val referenceCount: Int
        get() =
            if (code != null) 1 else 0 + if (data != null) 1 else 0 + if (libraries.isNullOrEmpty()) 0 else 1

    public class Libraries(
        dict: RawDictionary
    ) : Dictionary<ByteString, SimpleLib>(dict, {
        BitString(it)
    }, {
        ByteString(*it.toByteArray())
    }, SimpleLib)

    public companion object : CellSerializer<StateInit> by StateInitSerializer
}

private object StateInitSerializer : CellSerializer<StateInit> {

    override fun store(builder: CellBuilder, value: StateInit, context: CellContext) {

    }

    override fun load(slice: CellSlice, context: CellContext): StateInit {
        TODO()
    }
}