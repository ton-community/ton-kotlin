package org.ton.kotlin.message

/**
 * Message payload layout.
 */
public data class MessageLayout(
    /**
     * Whether to store state init in a child cell.
     */
    val initToCell: Boolean,

    /**
     * Whether to store payload as a child cell.
     */
    val bodyToCell: Boolean,
) {
    public companion object {
        /**
         * Plain message layout (init and body stored in the root cell).
         */
        public val PLAIN: MessageLayout = MessageLayout(initToCell = false, bodyToCell = false)
    }
}