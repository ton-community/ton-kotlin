package ton.crypto.curve25519.u32

import ton.crypto.curve25519.Constants
import ton.crypto.curve25519.FieldElement

object ConstantsU32 : Constants {
    override val SQRT_M1: FieldElement = FieldElement2625(
        intArrayOf(34513072, 25610706, 9377949, 3500415, 12389472, 33281959, 41962654, 31548777, 326685, 11406482)
    )
}