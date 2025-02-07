package org.ton.kotlin.currency

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.dict.Dictionary
import org.ton.kotlin.dict.RawDictionary

/**
 * Dictionary with amounts for multiple currencies.
 *
 * @see [CurrencyCollection]
 */
public class ExtraCurrencyCollection(
    dict: RawDictionary = RawDictionary(32),
) : Dictionary<Int, VarUInt248>(
    dict = dict,
    keySerializer = { CellBuilder().storeInt(it).toBitString() },
    keyDeserializer = { CellBuilder().storeBitString(it).toCellSlice().loadInt() },
    valueSerializer = VarUInt248
) {
    init {
        require(dict.keySize == 32) { "Dictionary key size must be 32 bits length" }
    }
}