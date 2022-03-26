package ton.tlb

open class Field(
    val name: String,
    val type: TypeExpression,
    val implicit: Boolean = false,
    val constraint: Boolean = false,
) {
    override fun toString() = "Field(name='$name', type=$type, implicit=$implicit, constraint=$constraint)"
}

fun ConstraintField(type: TypeExpression) = Field(
    name = "_",
    type = type,
    implicit = true,
    constraint = true
)

fun ImplicitField(name: String, type: TypeExpression) = Field(
    name = name,
    type = type,
    implicit = true
)