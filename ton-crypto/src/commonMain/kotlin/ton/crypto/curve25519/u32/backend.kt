package ton.crypto.curve25519.u32

import ton.crypto.curve25519.Backend

object BackendU32 : Backend {
    override val CONSTANTS = ConstantsU32
    override val FIELD_ELEMENT_FACTORY = FieldElement2625.FactoryU32
}
