package ton.tlb.types

import ton.tlb.TlbDecoder

interface TypeField<T> : TypeExpression<T> {
    val type: TypeExpression<T>
    var value: T?
    override fun decode(decoder: TlbDecoder): T = value ?: run {
        println("Start decoding field: $this")
        type.decode(decoder).also {
            println("End decoding field: $this value: $it")
            value = it
        }
    }
}

data class NamedTypeField<T>(
    val name: String,
    override val type: TypeExpression<T>,
) : TypeField<T> {
    override var value: T? = null
    override fun toString(): String = name
}

class UnnamedTypeField<T>(
    override val type: TypeExpression<T>,
) : TypeField<T> {
    override var value: T? = null
    override fun toString(): String = "_"
}

fun <T> TypeConstructor.field(name: String, type: TypeExpression<T>) =
    NamedTypeField(name, type).also {
        fields.add(it)
    }

fun <T> TypeConstructor.unnamedField(type: TypeExpression<T>) = UnnamedTypeField(type).also {
    fields.add(it)
}