package org.ton.fift

internal fun Any.fiftFormat() = if (this is String) "\"$this\"" else toString()
