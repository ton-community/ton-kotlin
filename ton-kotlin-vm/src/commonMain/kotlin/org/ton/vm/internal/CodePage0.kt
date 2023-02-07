package org.ton.vm.internal

import org.ton.asm.AsmInstruction
import org.ton.asm.stackbasic.*
import org.ton.asm.stackcomplex.*
import org.ton.vm.VirtualMachine
import org.ton.vm.op.*

public object CodePage0 : CodePage {
    override val value: Int get() = 0

    override fun execute(virtualMachine: VirtualMachine, op: AsmInstruction): Unit {
        virtualMachine.log.debug { "execute $op" }
        when (op) {
            is NOP -> {
                // NOP.
            }
            is SWAP -> executeSwap(virtualMachine, 0, 1)
            is XCHG_0I -> executeSwap(virtualMachine, 0, op.i)
            is XCHG_IJ -> executeSwap(virtualMachine, op.i, op.j)
            is XCHG_0II -> executeSwap(virtualMachine, 0, op.ii)
            is XCHG_1I -> executeSwap(virtualMachine, 1, op.i)
            is PUSH -> executePush(virtualMachine, op.i)
            is DUP -> executePush(virtualMachine, 0)
            is OVER -> executePush(virtualMachine, 1)
            is POP -> executePop(virtualMachine, op.i)
            is DROP -> executePop(virtualMachine, 0)
            is NIP -> executePop(virtualMachine, 1)
            is XCHG3 -> executeXchg3(virtualMachine, op.i, op.j, op.k)
            is XCHG2 -> executeXchg2(virtualMachine, op.i, op.j)
            is XCPU -> executeXcpu(virtualMachine, op.i.toInt(), op.j.toInt())
            is PUXC -> executePuxc(virtualMachine, op.i.toInt(), op.j.toInt())
            is PUSH2 -> executePush2(virtualMachine, op.i.toInt(), op.j.toInt())
            is XCHG3_LONG -> executeXchg3(virtualMachine, op.i.toInt(), op.j.toInt(), op.k.toInt())
            else -> throw Exception("Unknown instruction: $op")
        }
    }
}
