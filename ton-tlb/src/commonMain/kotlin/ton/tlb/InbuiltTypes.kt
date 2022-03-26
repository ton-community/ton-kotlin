package ton.tlb

import ton.bitstring.BitString

interface InbuiltTypes {
    fun nat() = Type(
        name = "#",
        isProducesNat = true
    )

    fun unary(): Type = Type(
        name = "Unary",
        arity = 1,
        constructors = buildList {
            add(Constructor(
                name = "unary_zero",
                beginsWith = BitPrefix(BitString(false)),
                params = buildList {
                    add(IntConstTypeExpression(0))
                },
                paramNegated = buildList {
                    add(true)
                }
            ))
            add(Constructor(
                name = "unary_succ",
                beginsWith = BitPrefix(BitString(true)),
                fields = buildList {
                    add(ImplicitField(
                        name = "n",
                        type = ApplyTypeExpression(typeApplied = { nat() })
                    ))
                    add(Field(
                        name = "x",
                        type = ApplyTypeExpression(
                            typeApplied = { unary() },
                            args = buildList {
                                add(ParamTypeExpression(0, true))
                            }
                        ),
                    ))
                },
                params = buildList {
                    add(AddTypeExpression(
                        left = ParamTypeExpression(0),
                        right = IntConstTypeExpression(1),
                    ))
                },
                paramNegated = buildList {
                    add(true)
                }
            ))
        }
    )
}