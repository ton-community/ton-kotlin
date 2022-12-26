package org.ton.vm

import org.ton.asm.AsmInstruction
import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellSlice
import org.ton.vm.instructions.registerCodepage
import kotlin.reflect.KClass

public typealias InstructionMap = MutableMap<KClass<out AsmInstruction>, (VirtualMachine, AsmInstruction) -> Unit>

public class VirtualMachine(
    code: Cell,
    gasLimits: VmGasLimits = VmGasLimits()
) {
    public val stack: MutableVmStack = MutableVmStack()
    public var cp: Int = 0
    private var code: CellSlice = code.beginParse()
    public val gasConsumer: GasConsumer = GasConsumer(gasLimits)
    public var controlRegistries: VmSaveList = VmSaveList()

    public val instructionHandlers: InstructionMap = HashMap()

    init {
        registerCodepage()
    }

    internal inline fun <reified T : AsmInstruction> register(noinline handler: VirtualMachine.(T) -> Unit) {
        register(T::class, handler)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : AsmInstruction> register(kClass: KClass<T>, handler: VirtualMachine.(T) -> Unit) {
        instructionHandlers[kClass] = handler as (VirtualMachine, AsmInstruction) -> Unit
    }

    public fun execute(methodId: Int = 0, stack: VmStack? = null): VmCont {
        this.stack.pushTinyInt(methodId)
        println("RUN CODE: ${code.bits}")
        stack?.forEach {
            this.stack.push(it)
        }
        while (true) {
            step()
        }
    }

    public fun step() {
        val startBits = code.bitsPosition
        val instruction = AsmInstruction.loadTlb(code)
        val endBits = code.bitsPosition
        gasConsumer.consume(endBits - startBits)
        instruction(this)
    }

    // simple jump to continuation cont
    fun jump(cont: VmCont) {
        val controlData = cont.cdata
        if (controlData != null && (controlData.stack.value != null || (controlData.nargs.value?.toInt() ?: -1) >= 0)) {
            // if cont has non-empty stack or expects fixed number of arguments, jump is not simple
            jump(cont, -1)
        } else {
            jumpTo(cont)
        }
    }

    fun jump(cont: VmCont, passArgs: Int) {
        val controlData = cont.cdata
        if (controlData != null) {
            val depth = stack.depth
            val nargs = controlData.nargs.value?.toInt() ?: -1
            require(!(passArgs > depth || nargs > depth)) {
                "Not enough arguments to jump to continuation"
            }
            require(passArgs !in 0 until nargs) {
                "Too many arguments to jump to continuation"
            }

            TODO()
        } else {
            if (passArgs >= 0) {
                val depth = stack.depth
                if (passArgs > depth) {
                    throw IllegalStateException("stack underflow while jumping to a continuation: not enough arguments on stack")
                } else if (passArgs < depth) {
                    repeat(depth - passArgs) {
                        stack.pop()
                    }
                    gasConsumer.consumeStack(passArgs)
                }
            }
            jumpTo(cont)
        }
    }

    fun jumpTo(cont: VmCont) = when (cont) {
        is VmContStd -> {
            controlRegistries = cont.cdata.save
            code = cont.code.toCellSlice()
        }

        is VmCont.Again -> TODO()
        is VmCont.PushInt -> TODO()
        is VmCont.Until -> TODO()
        is VmContEnvelope -> TODO()
        is VmContQuit -> TODO()
        VmContQuitExc -> TODO()
        is VmContRepeat -> TODO()
        is VmCont.WhileBody -> TODO()
        is VmCont.WhileCond -> TODO()
    }
}

@Suppress("UNCHECKED_CAST")
public operator fun AsmInstruction.invoke(vm: VirtualMachine) {
    val handler = vm.instructionHandlers[this::class]
    if (handler == null) {
        val debugString = this.toString()
        val className = this::class.simpleName
        val message = if (className == debugString) debugString else "$debugString ($className)"
        throw Exception("Unknown instruction: $message, stack: ${vm.stack.toList()}")
    }
    println("EXECUTE: $this")
    return handler(vm, this)
}
