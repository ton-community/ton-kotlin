package ton.fift

class Dictionary(
    val storage: MutableMap<String, WordDef> = HashMap()
) {
    operator fun get(name: String) = storage[name]
    operator fun set(name: String, wordDef: WordDef) {
        storage[name] = wordDef
    }
}

operator fun Dictionary.set(name: String, isActive: Boolean = false, function: (FiftInterpretator)->Unit) = set(name, object : WordDef {
    override val isActive: Boolean = isActive

    override fun execute(fift: FiftInterpretator) = function(fift)

    override fun toString(): String = "$name $function"
})

interface WordDef {
    val isActive: Boolean

    fun execute(fift: FiftInterpretator)
}

object NopWordDef : WordDef {
    override val isActive: Boolean = false

    override fun execute(fift: FiftInterpretator) {
        // nop.
    }
}