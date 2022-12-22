package org.ton.fift

public class Dictionary(
    public val storage: MutableMap<String, WordDef> = HashMap(),
) : MutableMap<String, WordDef> by storage

public operator fun Dictionary.set(
    name: String,
    isActive: Boolean = false,
    function: FiftInterpretator.() -> Unit,
): Unit = set(name, WordDef(name, isActive, function))

public data class WordList(
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

public interface WordDef {
    public val isActive: Boolean

    public fun execute(fift: FiftInterpretator)
}

public object NopWordDef : NamedWordDef("nop ", false) {
    override fun execute(fift: FiftInterpretator) {
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}BEGIN  execute WordDef: ${toString()}" }
        // nop
        fift.logger.debug { "${fift.debugExecutionDepthIndent()}FINISH execute WordDef: ${toString()}" }
    }
}

public open class NamedWordDef(
    public val name: String,
    override val isActive: Boolean,
    public val function: FiftInterpretator.() -> Unit = {},
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

public data class StackPushWordDef(
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

public data class SequentialWordDef(
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

public fun WordDef(entry: Any) = StackPushWordDef(entry)
public fun WordDef(name: String, isActive: Boolean = false, block: FiftInterpretator.() -> Unit): WordDef =
    NamedWordDef(name, isActive, block)
