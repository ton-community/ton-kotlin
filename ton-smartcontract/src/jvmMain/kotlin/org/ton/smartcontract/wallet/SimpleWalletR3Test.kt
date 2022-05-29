package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.block.Message
import org.ton.block.tlb.tlbCodec
import org.ton.boc.BagOfCells
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.crypto.hex
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import org.ton.tlb.TlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import java.io.File

suspend fun main() {
    val queryFile = File("C:\\Users\\andreypfau\\ton\\wallet\\new-wallet-query.boc")
    val pkFile = File("C:\\Users\\andreypfau\\ton\\wallet\\new-wallet.pk")
    val privateKey = PrivateKeyEd25519(pkFile.readBytes())
    val simpleWalletR3 = SimpleWalletR3(privateKey)
    println("Public Key: ${hex(simpleWalletR3.publicKey.key)}")
    val address = simpleWalletR3.address()
    println("new wallet address = ${address.workchainId}:${hex(address.address)}")
    println("Non-bounceable address (for init only): ${address.toString(bounceable = false, testOnly = true)}")
    println("Bounceable address (for later access): ${address.toString(bounceable = true, testOnly = true)}")

    val messageCodec = Message.tlbCodec(object : TlbCodec<BitString> {
        override fun storeTlb(cellBuilder: CellBuilder, value: BitString) {
            cellBuilder.storeBits(value)
        }

        override fun loadTlb(cellSlice: CellSlice): BitString {
            return cellSlice.loadBitString(cellSlice.bits.size - cellSlice.bitsPosition)
        }
    })
    val bocExample = BagOfCells(queryFile.readBytes())
    val messageExample = bocExample.first().beginParse().loadTlb(messageCodec)
    println("Example: $messageExample")
    println("Example BOC: $bocExample")

    val message = simpleWalletR3.createInitMessage()
    val cell = CellBuilder.createCell {
        storeTlb(messageCodec, message)
    }
    val boc = BagOfCells(cell)
    println("Current: $message")
    println("Current BOC: $boc")

    val liteClient = LiteClient(
        host = "5.9.10.47",
        port = 19949,
        publicKey = hex("9f85439d2094b92a639c2c9493d7b740e39dea8d08b525986d39d6dd69e7f309"),
        logger = PrintLnLogger("TON SimpleWalletR3", Logger.Level.DEBUG)
    ).connect()
    liteClient.sendMessage(boc.toByteArray())
}
