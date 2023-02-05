package org.ton.crypto.kdf

public abstract class PBEParametersGenerator(
    password: ByteArray,
    salt: ByteArray,
    iterationCount: Int
) {
    public constructor() : this(ByteArray(0), ByteArray(0), 0)

    public var password: ByteArray =password
        protected set
    public var salt: ByteArray = salt
        protected set
    public var iterationCount: Int = iterationCount
        protected set

    public fun init(password: ByteArray, salt: ByteArray, iterationCount: Int): PBEParametersGenerator = apply {
        this.password = password
        this.salt = salt
        this.iterationCount = iterationCount
    }

    public abstract fun generateDerivedParameters(keySize: Int): ByteArray

    public abstract fun generateDerivedMacParameters(keySize: Int): ByteArray
}
