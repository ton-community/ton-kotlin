package org.ton.crypto.digest

@Deprecated("Use kotlinx-crypto instead")
public interface Digest {
    public val algorithmName: String
    public val digestSize: Int

    public fun update(input: ByteArray): Unit = update(input, 0, input.size)
    public fun update(input: ByteArray, offset: Int, length: Int)

    public operator fun plusAssign(input: ByteArray): Unit = update(input)

    public fun build(): ByteArray = build(ByteArray(digestSize))
    public fun build(output: ByteArray): ByteArray = build(output, 0)
    public fun build(output: ByteArray, offset: Int): ByteArray

    public fun reset()
}

@Deprecated(
    "Use kotlinx-crypto instead",
    replaceWith = ReplaceWith("io.github.andreypfau.kotlinx.crypto.sha2.sha256(bytes)")
)
public fun sha256(bytes: ByteArray): ByteArray = io.github.andreypfau.kotlinx.crypto.sha2.sha256(bytes)

@Deprecated(
    "Use kotlinx-crypto instead",
    replaceWith = ReplaceWith("io.github.andreypfau.kotlinx.crypto.sha2.sha512(bytes)")
)
public fun sha512(bytes: ByteArray): ByteArray = io.github.andreypfau.kotlinx.crypto.sha2.sha512(bytes)
