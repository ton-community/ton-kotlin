package org.ton.vm.op

import org.ton.vm.VirtualMachine
import org.ton.vm.internal.CodePage0

internal fun CodePage0.executeNop(vm: VirtualMachine) {
}

internal fun CodePage0.executeSwap(vm: VirtualMachine, arg1: Int, arg2: Int) {
    val stack = vm.stack
    stack.checkDepth(arg1)
    stack.checkDepth(arg2)
    stack.swap(arg1, arg2)
}

internal fun CodePage0.executePush(vm: VirtualMachine, arg1: Int) {
    val stack = vm.stack
    stack.push(stack[arg1])
}

internal fun CodePage0.executePop(vm: VirtualMachine, arg1: Int) {
    val stack = vm.stack
    stack.checkDepth(arg1)
    stack.popAt(arg1)
}

internal fun CodePage0.executeXchg3(vm: VirtualMachine, i: Int, j: Int, k: Int) {
    val stack = vm.stack
    stack.checkDepth(i)
    stack.checkDepth(j)
    stack.checkDepth(k)
    stack.swap(2, i)
    stack.swap(1, j)
    stack.swap(0, k)
}

internal fun CodePage0.executeXchg2(vm: VirtualMachine, i: Int, j: Int) {
    val stack = vm.stack
    stack.checkDepth(i)
    stack.checkDepth(j)
    stack.swap(1, i)
    stack.swap(0, j)
}

internal fun CodePage0.executeXcpu(vm: VirtualMachine, i: Int, j: Int) {
    val stack = vm.stack
    stack.checkDepth(i)
    stack.checkDepth(j)
    stack.swap(0, i)
    stack.push(stack[j])
}

internal fun CodePage0.executePuxc(vm: VirtualMachine, i: Int, j: Int) {
    val stack = vm.stack
    stack.checkDepth(i)
    stack.checkDepth(j)
    stack.push(stack[i])
    stack.swap()
    stack.swap(1, j)
}

internal fun CodePage0.executePush2(vm: VirtualMachine, i: Int, j: Int) {
    val stack = vm.stack
    val newJ = j + 1
    stack.checkDepth(i)
    stack.checkDepth(newJ)
    stack.push(stack[i])
    stack.push(stack[newJ])
}

internal fun CodePage0.executeXc2pu(vm: VirtualMachine, i: Int, j: Int, k: Int) {
    val stack = vm.stack
    executeXchg2(vm, i, j)
    executePush(vm, k)
}

internal fun CodePage0.executeXcpuxc(vm: VirtualMachine, i: Int, j: Int, k: Int) {
    val stack = vm.stack
    stack.swap(0, i)
    executePuxc(vm, j, k - 1)
}
