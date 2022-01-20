package ton.types

enum class ExceptionCode(
    val code: Int,
    val message: String,
) {
    NormalTermination(0, "normal termination"),
    AlternativeTermination(1, "alternative termination"),
    StackUnderflow(2, "stack underflow"),
    StackOverflow(3, "stack overflow"),
    IntegerOverflow(4, "integer overflow"),
    RangeCheckError(5, "range check error"),
    InvalidOpcode(6, "invalid opcode"),
    TypeCheckError(7, "type check error"),
    CellOverflow(8, "cell overflow"),
    CellUnderflow(9, "cell underflow"),
    DictionaryError(10, "dictionary error"),
    UnknownError(11, "unknown error"),
    FatalError(12, "fatal error"),
    OutOfGas(13, "out of gas")
}