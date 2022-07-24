package org.ton.asm

import org.ton.asm.arithmetic.basic.*
import org.ton.asm.arithmetic.logical.*
import org.ton.asm.cell.deserialization.*
import org.ton.asm.codepage.SETCP
import org.ton.asm.codepage.SETCP0
import org.ton.asm.codepage.SETCPX
import org.ton.asm.comparsion.integer.*
import org.ton.asm.constant.bitstring.PUSHCONT
import org.ton.asm.constant.bitstring.PUSHREF
import org.ton.asm.constant.bitstring.PUSHREFSLICE
import org.ton.asm.constant.integer.*
import org.ton.asm.flow.conditional.*
import org.ton.asm.flow.control.*
import org.ton.asm.stack.basic.*
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.providers.TlbProvider

interface Instruction {
    companion object : TlbCombinatorProvider<Instruction> by InstructionTlbCombinator {
        fun loadList(cellSlice: CellSlice): List<Instruction> {
            val list = ArrayList<Instruction>()
            while (cellSlice.bitsPosition < cellSlice.bits.size) {
                val instruction = loadTlb(cellSlice)
                println(instruction)
                list.add(instruction)
            }
            return list
        }
    }
}

object InstructionTlbCombinator : TlbCombinator<Instruction>() {
    override val constructors: List<TlbConstructor<out Instruction>> = createConstructorList(
        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_21-basic-stack-manipulation-primitives
        NOP, SWAP, XCHG0, XCHG, PUSH, DUP, OVER, POP, DROP, NIP,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_41-integer-and-boolean-constants
        PUSHINT, ZERO, ONE, TWO, TEN, TRUE, PUSHPOW2, PUSHNAN, PUSHPOW2DEC, PUSHNEGPOW2,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_42-constant-slices-continuations-cells-and-references
        // TODO: PUSHSLICE
        PUSHREF, PUSHREFSLICE, PUSHCONT,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_51-addition-subtraction-multiplication
        ADD, SUB, SUBR, NEGATE, INC, DEC, ADDCONST, MULCONST, MUL,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_53-shifts-logical-operations
        LSHIFTBY, RSHIFTBY, LSHIFT, RSHIFT, POW2, AND, OR, XOR, NOT, FITS, CHKBOOL, UFITS, CHKBIT, FITSX, UFITSX,
        BITSIZE, UBITSIZE, MIN, MAX, MINMAX, ABS,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_61-integer-comparison
        SGN, LESS, EQUAL, LEQ, GREATER, NEQ, GEQ, CMP, EQINT, ISZERO, LESSINT, ISNEG, ISNPOS, ISNNEG, NEQINT,
        ISNAN, CHKNAN,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_72-cell-deserialization-primitives
        CTOS, ENDS, LDI, LDU, LDREF, LDREFRTOS, LDIX, LDUX, PLDIX, PLDUX, LDIXQ, LDUXQ, PLDIXQ, PLDUXQ, LDIL, LDUL,
        PLDI, PLDU, LDIQ, LDUQ, PLDIQ, PLDUQ, PLDUZ, LDSLICEX, PLDSLICEX, LDSLICEXQ, PLDSLICEXQ, LDSLICEL, PLDSLICE,
        LDSLICEQ, PLDSLICEQ, SDCUTFIRST, SDSKIPFIRST, SDCUTLAST, SDSKIPLAST, SDSUBSTR, SDBEGINSX, SDBEGINSXQ,
        SDBEGINS, SDBEGINSQ, SCUTFIRST, SSKIPFIRST, SCUTLAST, SSKIPLAST, SUBSLICE, SPLIT, SPLITQ, XCTOS, XLOAD, XLOADQ,
        SCHKBITS, SCHKREFS, SCHKBITREFS, SCHKBITSQ, SCHKREFSQ, SCHKBITREFSQ, PLDREFVAR, SBITS, SREFS, SBITREFS,
        PLDREFIDX, PLDREF, LDILE4, LDULE4, LDILE8, LDULE8, PLDILE4, PLDULE4, PLDILE8, PLDULE8, LDILE4Q, LDULE4Q,
        LDILE8Q, LDULE8Q, PLDILE4Q, PLDULE4Q, PLDILE8Q, PLDULE8Q, LDZEROES, LDONES, LDSAME, SDEPTH, CDEPTH,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_82-conditional-control-flow-primitives
        IFRET, IFNOTRET, IF, IFNOT, IFJMP, IFNOTJMP, IFELSE, IFREF, IFNOTREF, IFJMPREF, IFNOTJMPREF, CONDSEL,
        CONDSELCHK, IFRETALT, IFNOTRETALT, IFREFELSE, IFELSEREF, IFREFELSEREF, IFBITJMP, IFNBITJMP, IFBITJMPREF,
        IFNBITJMPREF,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_86-operations-with-continuation-savelists-and-control-registers
        PUSHCTR, POPCTR, SETCONT, SETRETCTR, SETALTCTR, POPSAVE, SAVE, SAVEALT, SAVEBOTH, PUSHCTRX, PUSHCTRX, POPCTRX,
        SETCONTCTRX, COMPOS, COMPOSALT, COMPOSBOTH, ATEXIT, ATEXITALT, SETEXITALT, THENRET, THENRETALT, INVERT,
        BOOLEVAL, SAMEALT, SAMEALTSAVE,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_13-codepage-primitives
        SETCP, SETCP0, SETCPX
    )

    private fun createConstructorList(vararg providers: TlbProvider<out Instruction>): List<TlbConstructor<out Instruction>> {
        val list = buildList {
            providers.forEach { provider ->
                when (provider) {
                    is TlbConstructorProvider<out Instruction> -> add(provider.tlbConstructor())
                    is TlbCombinatorProvider<out Instruction> -> addAll(provider.tlbCombinator().constructors)
                }
            }
        }.sortedBy { it.id }
        return list
    }
}