package ton.tlb

import kotlinx.serialization.InheritableSerialInfo

@InheritableSerialInfo
@Target(AnnotationTarget.CLASS)
annotation class TlbConstructorPrefix(
    val bitPrefix: BooleanArray,
)

@InheritableSerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class TlbNumber(
    val bitSize: Int,
)

@InheritableSerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class TlbBits(
    val lengthField: String,
)

@InheritableSerialInfo
@Target(AnnotationTarget.CLASS)
annotation class TlbScheme(
    val scheme: String,
)