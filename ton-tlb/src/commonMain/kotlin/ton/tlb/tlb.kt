package ton.tlb

data class Field(
    override val name: String,
    val typeExpression: TypeExpression,
) : TypeExpression by typeExpression {

    override fun toString(): String = buildString {
        if (name != "_") {
            append(name)
            append('=')
        }
        append(typeExpression)
    }
}

interface TypeExpression {
    val name: String
    val value: Any
    val fields: MutableList<Field>

    operator fun contains(name: String) = fields.any { it.name == name }
    operator fun get(name: String) = fields.find { it.name == name }!!
    private fun set(name: String, typeExpression: TypeExpression) {
        if (contains(name)) {
            val oldField = get(name)
            val index = fields.indexOf(oldField)
            fields[index] = oldField.copy(typeExpression = typeExpression)
        } else {
            fields.add(Field(name, typeExpression))
        }
    }

    operator fun set(name: String, typeExpression: () -> TypeExpression): Any {
        val value = object : TypeExpressionImpl("") {
            val typeValue_ by lazy { typeExpression() }
            override val name: String
                get() = typeValue_.name
            override val value: Any
                get() = typeValue_.value
            override val fields: MutableList<Field>
                get() = typeValue_.fields
        }
        set(name, value)
        return value.value
    }

    fun toInt(): Int = when (val value = value) {
        is Number -> value.toInt()
        is ULong -> value.toInt()
        is UInt -> value.toInt()
        else -> throw IllegalArgumentException(value.toString())
    }

    fun toLong(): Long = when (val value = value) {
        is Number -> value.toLong()
        is ULong -> value.toLong()
        is UInt -> value.toLong()
        else -> throw IllegalArgumentException(value.toString())
    }

    fun toJsonString(): String = if (fields.isNotEmpty()) {
        buildString {
            append('{')
            fields.forEachIndexed { index, field ->
                append('"')
                append(field.name)
                append('"')
                append('=')
                append(field.toJsonString())
                if (index != fields.lastIndex) {
                    append(',')
                }
            }
        }
    } else {
        val stringValue = value.toString()
        when {
            stringValue == "true" -> stringValue
            stringValue == "false" -> stringValue
            stringValue.contains('.') -> stringValue.toDoubleOrNull()?.let { stringValue } ?: "\"$stringValue\""
            else -> stringValue.toLongOrNull()?.let { stringValue } ?: "\"$stringValue\""
        }
    }
}

abstract class TypeExpressionImpl(override val name: String) : TypeExpression {
    override val value: Any = Any()
    override val fields: MutableList<Field> = ArrayList()

    override fun toString(): String {
        return if (fields.isNotEmpty()) {
            buildString {
                append(name)
                append(fields.joinToString(prefix = "{", postfix = "}"))
            }
        } else {
            if (value::class == Any::class) name
            else value.toString()
        }
    }
}

operator fun TypeExpression.plus(other: TypeExpression) = object : TypeExpressionImpl("+") {
    override val value: Int get() = this@plus.toInt() + other.toInt()
}

class TypeExpressionIntConstant(override val value: Int = 0) : TypeExpressionImpl(value.toString())
class TypeExpressionObjectConstant(name: String, override val fields: MutableList<Field>) : TypeExpressionImpl(name)