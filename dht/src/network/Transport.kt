package org.ton.dht.network

public interface Transport {
    public fun listen(address: AdnlAddress, connectionHandler: ConnectionHandler)

    public fun dial(address: AdnlAddress, connectionHandler: ConnectionHandler)
}

