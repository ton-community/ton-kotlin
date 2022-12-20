package org.ton.adnl.node

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import org.ton.adnl.network.IPAddress
import org.ton.adnl.network.UdpServer
import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlIdShort
import org.ton.bitstring.BitString
import org.ton.logger.Logger
import org.ton.tl.Bits256
import kotlin.coroutines.CoroutineContext

public class AdnlNetworkManager(
    coroutineContext: CoroutineContext
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = coroutineContext + CoroutineName(toString())

    private var callback: Callback? = null

    private val log = Logger.println(toString())
    private val adnlId2Category = HashMap<AdnlIdShort, Int>()
    private val port2Socket = HashMap<Int, UdpSocketDesc>()
    private var receivedDatagrams = 0
    private var sentDatagrams = 0
    private val inputDesc = ArrayList<InputDesc>()
    private val outputDesc = HashMap<Int, ArrayList<OutputDesc>>()
    private val proxyAddrs = HashMap<Bits256, UdpSocketDesc>()

    public fun addSelfAddress(
        address: AdnlAddressUdp,
        categoryMask: AdnlCategoryMask,
        priority: Int
    ) {
        val port = address.port
        val socket = addListeningUdpPort(port)
        addInputAddress(InputDesc(socket, categoryMask, null), socket)
        addOutputAddress(OutputDesc(socket, null, null), categoryMask, priority)
    }

    public fun setLocalIdCategory(id: AdnlIdShort, category: Int) {
        if (category == 255) {
            adnlId2Category.remove(id)
        } else {
            adnlId2Category[id] = category
        }
    }

    public suspend fun sendDatagram(
        srcId: AdnlIdShort,
        dstId: AdnlIdShort,
        dstAddr: AdnlAddressUdp,
        data: ByteReadPacket,
        priority: Int = 0
    ) {
        val category = adnlId2Category[srcId] ?: kotlin.run {
            log.warn { "dropping OUT datagram [$srcId->$dstId]: unknown src" }
            return
        }
        val out = choseOutputInterface(category, priority) ?: kotlin.run {
            log.warn { "dropping OUT datagram [$srcId->$dstId]: no output interface" }
            return
        }
        val socket = out.socket
        if (out.isProxy) {
            TODO()
        } else {
            if (data.remaining > MTU) {
                log.warn { "dropping OUT datagram [$srcId->$dstId]: sending huge datagram of size: ${data.remaining}" }
                return
            }
            socket.server.send(
                IPAddress.ipv4(dstAddr.ip, dstAddr.port),
                data
            )
        }
        sentDatagrams++
    }

    private fun receiveDatagram(
        address: IPAddress,
        data: ByteReadPacket
    ) {
        receiveDatagram(address.toAdnlAddress(), data)
    }

    private fun receiveDatagram(
        address: AdnlAddress,
        data: ByteReadPacket
    ) {
        val callback = callback ?: run {
            log.fatal { "dropping IN datagram [?->?]: callback not initialized" }
            return
        }
        if (data.remaining < 32) {
            log.warn { "received too small datagram of size: ${data.remaining}" }
            return
        }
        if (data.remaining >= MTU) {
            log.warn { "received huge datagram of size: ${data.remaining}" }
        }
        receivedDatagrams++
        if (receivedDatagrams % 64 == 0) {
            log.debug { "received $receivedDatagrams datagrams" }
        }
        log.trace { "received datagram of size: ${data.remaining}" }
        callback.receiveDatagram(address, data)
    }

    private fun choseOutputInterface(
        category: Int,
        priority: Int
    ) = outputDesc[priority]?.find { it.categoryMask[category] }

    private fun addListeningUdpPort(port: Int): UdpSocketDesc = port2Socket.getOrPut(port) {
        val server = UdpServer.create(coroutineContext, port, ::receiveDatagram)
        UdpSocketDesc(port, server)
    }

    private fun addInputAddress(
        desc: InputDesc,
        socket: UdpSocketDesc
    ) {
        val currentDesc = inputDesc.find { it == desc }
        if (currentDesc != null) {
            currentDesc.categoryMask = currentDesc.categoryMask or desc.categoryMask
            return
        }
        val proxy = desc.proxy
        if (proxy != null) {
            socket.allowProxy = true
            proxyAddrs[proxy.id] = socket
        }
        inputDesc.add(desc)
    }

    private fun addOutputAddress(
        desc: OutputDesc,
        categoryMask: AdnlCategoryMask,
        priority: Int
    ) {
        val list = outputDesc.getOrPut(priority) { ArrayList() }
        val currentOutputDesc = list.find { it == desc }
        if (currentOutputDesc == null) {
            desc.categoryMask = categoryMask
            list.add(desc)
        } else {
            currentOutputDesc.categoryMask = currentOutputDesc.categoryMask or categoryMask
        }
    }

    public fun interface Callback {
        public fun receiveDatagram(address: AdnlAddress, data: ByteReadPacket)
    }

    private data class OutputDesc(
        val socket: UdpSocketDesc,
        val proxyAddress: AdnlAddressUdp?,
        val proxy: AdnlProxy?,
        var categoryMask: AdnlCategoryMask = AdnlCategoryMask()
    ) {
        val isProxy get() = proxy != null
    }

    private data class InputDesc(
        val socketDesc: UdpSocketDesc,
        var categoryMask: AdnlCategoryMask,
        val proxy: AdnlProxy?,
    ) {
        val isProxy get() = proxy != null

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is InputDesc) return false
            if (socketDesc.port != other.socketDesc.port) return false
            if (proxy != other.proxy) return false
            return true
        }

        override fun hashCode(): Int {
            var result = socketDesc.port
            result = 31 * result + (proxy?.hashCode() ?: 0)
            result = 31 * result + categoryMask.hashCode()
            return result
        }
    }

    private data class UdpSocketDesc(
        val port: Int,
        val server: UdpServer,
        var allowProxy: Boolean = false,
        var inDesc: InputDesc? = null,
    )

    public companion object {
        public const val MTU: Int = 1440
    }
}
