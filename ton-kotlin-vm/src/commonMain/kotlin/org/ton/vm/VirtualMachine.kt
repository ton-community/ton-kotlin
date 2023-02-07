package org.ton.vm

import org.ton.asm.AsmInstruction
import org.ton.cell.CellSlice
import org.ton.logger.Logger
import org.ton.vm.exception.TvmInvalidOpcodeException
import org.ton.vm.exception.TvmNormalTerminationException
import org.ton.vm.internal.CodePage
import org.ton.vm.internal.CodePage0
import org.ton.vm.stack.MutableVmStack

public class VirtualMachine(
    public var code: CellSlice,
    public var stack: MutableVmStack = MutableVmStack(16)
) {
    public val log: Logger = Logger.println("TVM", Logger.Level.DEBUG)

    public var codePage: CodePage? = null

    public fun run() {
        log.debug { "Started with stack: ${stack.toList()}" }
        try {
            while (true) {
                step()
            }
        } catch (_: TvmNormalTerminationException) {
            log.debug { "Normal termination with stack: ${stack.toList()}" }
        }
    }

    public fun step() {
        if (code.bitsPosition < code.bits.size) {
            run(code)
        } else {
            throw TvmNormalTerminationException()
        }
    }

    private fun run(cellSlice: CellSlice) {
        val pos = cellSlice.bitsPosition
        val instruction = try {
            AsmInstruction.loadTlb(cellSlice)
        } catch (e: Exception) {
            throw TvmInvalidOpcodeException("Invalid opcode at $pos")
        }
        val pos2 = cellSlice.bitsPosition
        val bitsRead = pos2 - pos
        (codePage ?: CodePage0).execute(this, instruction)
    }
}
