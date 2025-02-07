package org.ton.kotlin.message.address

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

public sealed interface MsgAddress {
    public companion object : CellSerializer<MsgAddress?> by MsgAddressSerializer
}

private object MsgAddressSerializer : CellSerializer<MsgAddress?> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): MsgAddress? {
        return if (slice.preloadBoolean()) {
            slice.load(IntAddr, context)
        } else {
            slice.load(ExtAddr, context)
        }
    }

    override fun store(
        builder: CellBuilder,
        value: MsgAddress?,
        context: CellContext
    ) {
        when (value) {
            is IntAddr -> builder.store(IntAddr, value)
            is ExtAddr, null -> {
                builder.store(ExtAddr, value)
            }
        }
    }
}
