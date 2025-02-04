package org.ton.cell

import io.github.andreypfau.kotlinx.crypto.Sha256

@kotlin.native.concurrent.ThreadLocal
internal actual val CELL_BUILDER_HASHER: Sha256 = Sha256()