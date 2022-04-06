package ton.crypto.curve25519

import ton.crypto.curve25519.u32.BackendU32

internal interface Backend {
    val CONSTANTS: Constants
    val FIELD_ELEMENT_FACTORY: FieldElementFactory
}

internal val BACKEND = BackendU32
internal val CONSTANTS = BACKEND.CONSTANTS

