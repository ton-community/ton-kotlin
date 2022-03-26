package ton.tlb

sealed interface TypeExpression {
    val isNat get() = false
    val isNatSubtype: Boolean get() = false
}

abstract class AbstractTypeExpression(
    open val name: String? = null,
    val args: List<TypeExpression> = emptyList(),
) : TypeExpression {
    override fun toString(): String = name.toString()
}

interface NaturalTypeExpression: TypeExpression {
    val negated: Boolean
    val value: Int
}

class TypeTypeExpression : AbstractTypeExpression() {
    override fun toString(): String = "Type"
}

class ParamTypeExpression(
    override val value: Int,
    override val negated: Boolean = false,
) : AbstractTypeExpression(), NaturalTypeExpression {
    override fun toString(): String = buildString {
        val i = value
        var paramName: String? = null
        // todo: calc param name from constructor
        if (negated) {
            append('~')
        }
        if (paramName == null) {
            append("_${i + 1}")
        }
    }
}

class ApplyTypeExpression(
    val typeApplied: () -> Type,
    args: List<TypeExpression> = emptyList(),
) : AbstractTypeExpression(args = args) {
    override val name: String get() = typeApplied().name
    override val isNatSubtype: Boolean
        get() {
            val type = typeApplied()
            return type.isProducesNat && type.arity > 0
        }

    override fun toString(): String = buildString {
        if (args.isNotEmpty()) {
            append('(')
        }
        append(typeApplied().name)
        args.forEach { arg ->
            append(' ')
            append(arg.toString())
        }
        if (args.isNotEmpty()) {
            append(')')
        }
    }
}

class AddTypeExpression(
    val left: NaturalTypeExpression,
    val right: NaturalTypeExpression,
    override val negated: Boolean = false,
) : AbstractTypeExpression(args = listOf(left, right)), NaturalTypeExpression {
    override val value: Int get() = left.value + right.value

    override fun toString(): String = "($left + $right)"
}

class GetBitTypeExpression() : AbstractTypeExpression()
class MulConstTypeExpression() : AbstractTypeExpression()
class IntConstTypeExpression(
    override val value: Int,
    override val negated: Boolean = false,
) : AbstractTypeExpression(), NaturalTypeExpression {
    override fun toString(): String = value.toString()
}

class TupleTypeExpression() : AbstractTypeExpression()
class ReferenceTypeExpression() : AbstractTypeExpression()
class CondTypeTypeExpression() : AbstractTypeExpression()