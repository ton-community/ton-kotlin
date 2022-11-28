package org.ton.vm

sealed interface VmState {
    fun step(): VmState
}
