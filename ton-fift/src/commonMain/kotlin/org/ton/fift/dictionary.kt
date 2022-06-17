package org.ton.fift

class Dictionary(
        val storage: MutableMap<String, WordDef> = HashMap(),
) : MutableMap<String, WordDef> by storage

operator fun Dictionary.set(
        name: String,
        isActive: Boolean = false,
        function: FiftInterpretator.() -> Unit,
) = set(name, WordDef(name, isActive, function))

data class WordList(
        val list: MutableList<WordDef> = ArrayList(),
) : MutableList<WordDef> by list, WordDef {
    override val isActive: Boolean
        get() = false

    override fun execute(fift: FiftInterpretator) {
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}BEGIN  execute WordDef: ${toString()}" }
        fift.debugExecutionDepth++
        list.forEach {
            it.execute(fift)
        }
        fift.debugExecutionDepth--
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}FINISH execute WordDef: ${toString()}" }
    }
}

interface WordDef {
    val isActive: Boolean

    fun execute(fift: FiftInterpretator)
}

object NopWordDef : NamedWordDef("nop ", false) {
    override fun execute(fift: FiftInterpretator) {
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}BEGIN  execute WordDef: ${toString()}" }
        // nop
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}FINISH execute WordDef: ${toString()}" }
    }
}

open class NamedWordDef(
        val name: String,
        override val isActive: Boolean,
        val function: FiftInterpretator.() -> Unit = {},
) : WordDef {
    override fun execute(fift: FiftInterpretator) {
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}BEGIN  execute WordDef: ${toString()}" }
        fift.debugExecutionDepth++
        function(fift)
        fift.debugExecutionDepth--
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}FINISH execute WordDef: ${toString()}" }
    }

    override fun toString(): String = "WordDef($name)"
}

data class StackPushWordDef(
        val element: Any,
) : NamedWordDef(element.toString(), false) {
    override fun execute(fift: FiftInterpretator) {
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}BEGIN  execute WordDef: ${toString()}" }
        fift.debugExecutionDepth++
        fift.stack.push(element)
        fift.debugExecutionDepth--
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}FINISH execute WordDef: ${toString()}" }
    }

    override fun toString(): String = "StackPushWordDef(${element.fiftFormat()})"
}

data class SequentialWordDef(
        val iterable: Iterable<WordDef>,
) : WordDef {
    override val isActive: Boolean = false

    override fun execute(fift: FiftInterpretator) {
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}BEGIN  execute WordDef: ${toString()}" }
        fift.debugExecutionDepth++
        iterable.forEach {
            it.execute(fift)
        }
        fift.debugExecutionDepth--
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}FINISH execute WordDef: ${toString()}" }
    }

    override fun toString(): String = "WordDef(${iterable.joinToString(",")})"
}

fun WordDef(entry: Any) = StackPushWordDef(entry)
fun WordDef(name: String, isActive: Boolean = false, block: FiftInterpretator.() -> Unit): WordDef =
        NamedWordDef(name, isActive, block)