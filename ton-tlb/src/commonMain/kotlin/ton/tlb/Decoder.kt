package ton.tlb

import ton.bitstring.BitString
import ton.cell.CellReader

class Decoder(
    val cellReader: CellReader,
    val fields: MutableMap<String, Any> = LinkedHashMap(),
) {
    fun decode(constructor: Constructor, tmpArgs: IntArray = IntArray(constructor.params.size)): Map<String, Any> {
        constructor.fields.forEachIndexed { index, field ->
            if (field.constraint) {

            } else {
                val value = decode(field.type)
                fields[field.name] = value
            }
        }
        constructor.params.forEachIndexed { index, typeExpression ->
            if (typeExpression is NaturalTypeExpression && typeExpression.negated) {
                tmpArgs[index] = decode(typeExpression)
            }
        }
        return fields
    }

    fun decode(typeExpression: TypeExpression) = when (typeExpression) {
        is ApplyTypeExpression -> decode(typeExpression)
        is AddTypeExpression -> decode(typeExpression)
        is NaturalTypeExpression -> decode(typeExpression)
        is ParamTypeExpression -> fields.values.elementAt(typeExpression.value)
        else -> TODO()
    }

    fun decode(naturalTypeExpression: NaturalTypeExpression) = naturalTypeExpression.value

    fun decode(typeExpression: AddTypeExpression): Int {
        val left = typeExpression.left
        val right = typeExpression.right
        return decode(left) + decode(right)
    }

    fun decode(typeExpression: ApplyTypeExpression): Map<String, Any> {
        val typeApplied = typeExpression.typeApplied()
        val args = typeExpression.args
        return decode(typeApplied, args)
    }

    fun decode(type: Type, args: List<TypeExpression>): Map<String, Any> {
        // prefix strategy
        val newDecoder = Decoder(cellReader)
        if (type.constructors.all { it.beginsWith.size > 0}) {
            var bitPrefix = BitString()
            var constructor: Constructor? = null
            while (constructor == null && bitPrefix.size <= Int.SIZE_BITS) {
                constructor = type.constructors.find { it.beginsWith == bitPrefix }
                if (constructor == null) {
                    bitPrefix += cellReader.readBit()
                }
            }
            if (constructor != null) {
                
                newDecoder.decode(constructor)
            }
        }

        return newDecoder.fields
    }
}