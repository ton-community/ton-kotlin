package org.ton.vm.exception

public abstract class TvmException : RuntimeException {
    public abstract val code: Int

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Should never be generated, but it is useful for some tricks
 */
public class TvmNormalTerminationException : TvmException {
    override val code: Int = 0

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Should never be generated, but it is useful for some tricks
 */
public class TvmAlternativeTerminationException : TvmException {
    override val code: Int = 1

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Not enough arguments in the stack for a primitive.
 */
public class TvmStackUnderflowException : TvmException {
    override val code: Int = 2

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * More values have been stored on a stack than allowed by this version of TVM.
 */
public class TvmStackOverflowException : TvmException {
    override val code: Int = 3

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Integer does not fit into −2^256 ≤ x < 2^256,
 * or a division by zero has occurred.
 */
public class TvmIntegerOverflowException : TvmException {
    override val code: Int = 4

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Integer out of expected range.
 */
public class TvmRangeCheckException : TvmException {
    override val code: Int = 5

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Instruction or its immediate arguments cannot be decoded.
 */
public class TvmInvalidOpcodeException : TvmException {
    override val code: Int = 6

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * An argument to a primitive is of incorrect value type
 */
public class TvmTypeCheckException : TvmException {
    override val code: Int = 7

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Error in one of the serialization primitives
 */
public class TvmCellOverflowException : TvmException {
    override val code: Int = 8

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Deserialization error.
 */
public class TvmCellUnderflowException : TvmException {
    override val code: Int = 9

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Error while deserializing a dictionary object.
 */
public class TvmDictionaryException : TvmException {
    override val code: Int = 10

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Unknown error, may be thrown by user programs.
 */
public class TvmUnknownException : TvmException {
    override val code: Int = 11

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Thrown by TVM in situations deemed impossible
 */
public class TvmFatalErrorException : TvmException {
    override val code: Int = 12

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * Thrown by TVM when the remaining gas (`gr`)
 * becomes negative. This exception usually cannot be caught and leads
 * to an immediate termination of TVM.
 */
public class TvmOutOfGasException : TvmException {
    override val code: Int = 13

    public constructor() : super()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}
