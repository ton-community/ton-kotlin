package ton.tlb.types

import ton.tlb.TlbDecoder

class AnonymousTypeConstructor(
    override val fields: MutableList<TypeField<*>> = ArrayList(),
    override val negated: MutableList<NegatedTypeExpression> = ArrayList(),
) : TypeConstructor {
    override fun decode(decoder: TlbDecoder): Map<TypeField<*>, Any?> =
        fields.asSequence().map { field ->
            field to field.decode(decoder)
        }.toMap()
}

fun TypeNamedConstructor.anonymousConstructor(builder: AnonymousTypeConstructor.() -> Unit) =
    AnonymousTypeConstructor().apply(builder)