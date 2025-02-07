package org.ton.kotlin.message

import org.ton.kotlin.account.StateInit
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.message.info.MsgInfo

/**
 * Blockchain message.
 *
 * ```tlb
 * message$_ {X:Type} info:CommonMsgInfo
 *   init:(Maybe (Either StateInit ^StateInit))
 *   body:(Either X ^X) = Message X;
 * ```
 */
public open class Message<I : MsgInfo, B>(
    /**
     * Message info.
     */
    public val info: I,

    /**
     * Optional state init.
     */
    public val init: StateInit? = null,

    /**
     * Optional payload.
     */
    public val body: B,

    /**
     * Message layout.
     */
    public val layout: MessageLayout
) {
    public companion object {
        public fun <I : MsgInfo, B> cellSerializer(
            infoSerializer: CellSerializer<I>,
            bodySerializer: CellSerializer<B>,
        ): CellSerializer<Message<I, B>> = MessageSerializer(infoSerializer, bodySerializer)
    }
}

private class MessageSerializer<I : MsgInfo, B>(
    val infoSerializer: CellSerializer<I>,
    bodySerializer: CellSerializer<B>
) : CellSerializer<Message<I, B>> {
    private val bodySliceOrCellSerializer = SliceOrCellSerializer(bodySerializer)

    override fun load(
        slice: CellSlice,
        context: CellContext
    ): Message<I, B> {
        val info = slice.load(infoSerializer, context)
        val init = slice.loadNullable(initSerializer, context)
        val body = slice.load(bodySliceOrCellSerializer, context)
        val layout = MessageLayout(init?.toCell ?: false, body.toCell)
        return Message(info, init?.value, body.value, layout)
    }

    override fun store(
        builder: CellBuilder,
        value: Message<I, B>,
        context: CellContext
    ) {
        builder.store(infoSerializer, value.info, context)
        builder.storeNullable(initSerializer, value.init?.let { SliceOrCell(value.layout.initToCell, it) }, context)
        builder.store(bodySliceOrCellSerializer, SliceOrCell(value.layout.bodyToCell, value.body), context)
    }

    companion object {
        private val initSerializer = SliceOrCellSerializer(StateInit)
    }
}

private data class SliceOrCell<T>(
    val toCell: Boolean,
    val value: T
)

private class SliceOrCellSerializer<T>(
    val serializer: CellSerializer<T>,
) : CellSerializer<SliceOrCell<T>> {
    override fun load(slice: CellSlice, context: CellContext): SliceOrCell<T> {
        val toCell = slice.loadBoolean()
        val childSlice = if (toCell) {
            context.loadCell(slice.loadRef()).asCellSlice()
        } else {
            slice
        }
        val value = childSlice.load(serializer, context)
        return SliceOrCell(toCell, value)
    }

    override fun store(builder: CellBuilder, value: SliceOrCell<T>, context: CellContext) {
        if (value.toCell) {
            val cell = context.finalizeCell(
                CellBuilder().store(serializer, value.value, context)
            )
            builder.storeRef(cell)
        } else {
            builder.store(serializer, value.value)
        }
    }
}