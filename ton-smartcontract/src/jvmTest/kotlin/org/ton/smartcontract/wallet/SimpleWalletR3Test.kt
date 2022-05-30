package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.Message
import org.ton.block.tlb.tlbCodec
import org.ton.boc.BagOfCells
import org.ton.cell.CellBuilder
import org.ton.crypto.base64
import org.ton.crypto.hex
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import org.ton.tlb.constructor.AnyTlbConstructor
import java.io.File

suspend fun main() {
    val pkFile = File("C:\\Users\\andreypfau\\ton\\wallet\\new-wallet.pk")
    val privateKey = PrivateKeyEd25519(pkFile.readBytes())
    val simpleWalletR3 = SimpleWalletR3(privateKey)
    println("Public Key: ${hex(simpleWalletR3.publicKey.key)}")
    val address = simpleWalletR3.address()
    println("new wallet address = ${address.workchainId}:${hex(address.address)}")
    println("Non-bounceable address (for init only): ${address.toString(bounceable = false, testOnly = true)}")
    println("Bounceable address (for later access): ${address.toString(bounceable = true, testOnly = true)}")

    val messageCodec = Message.tlbCodec(AnyTlbConstructor)

    val message = simpleWalletR3.createInitMessage()
    val cell = CellBuilder.createCell {
        messageCodec.storeTlb(this, message)
    }
    val boc = BagOfCells(cell)

    val liteClient = LiteClient(
        ipv4 = 1426768764,
        port = 13724,
        publicKey = base64("R1KsqYlNks2Zows+I9s4ywhilbSevs9dH1x2KF9MeSU="),
        logger = PrintLnLogger("TON SimpleWalletR3", Logger.Level.DEBUG)
    ).connect()
    val result = liteClient.sendMessage(boc)
    liteClient.logger.info { result.toString() }
}
