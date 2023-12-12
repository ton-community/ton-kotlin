package org.ton.crypto.mac

/**
 * The base interface for implementations of message authentication codes (MACs).
 */
public interface Mac {
    public val algorithmName: String
    public val macSize: Int

    public fun init(key: ByteArray): Mac

    public fun update(input: ByteArray): Unit = update(input, 0, input.size)
    public fun update(input: ByteArray, offset: Int, length: Int)

    public operator fun plusAssign(input: ByteArray): Unit = update(input)

    public fun build(): ByteArray = build(ByteArray(macSize))
    public fun build(output: ByteArray): ByteArray = build(output, 0)
    public fun build(output: ByteArray, offset: Int): ByteArray

    public fun reset()
}
