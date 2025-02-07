package org.ton.dht.network

public interface Connection {
    public val localNode: LocalNode
    public val remoteNode: RemoteNode
}

public fun interface ConnectionHandler {
    public fun handleConnection(conn: Connection)
}