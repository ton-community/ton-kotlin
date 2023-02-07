package org.ton.vm.internal

import org.ton.asm.AsmInstruction
import org.ton.vm.VirtualMachine

public interface CodePage {
    public val value: Int

    public fun execute(virtualMachine: VirtualMachine, instruction: AsmInstruction)
}
