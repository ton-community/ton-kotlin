package org.ton.lite.api.liteserver

import kotlin.test.Test
import kotlin.test.assertEquals

class LiteServerRunSmcMethodTest {
    @Test
    fun `check methodId sanity`() {
        assertEquals(
            85143L,
            LiteServerRunSmcMethod.methodId("seqno")
        )
        assertEquals(
            78748L,
            LiteServerRunSmcMethod.methodId("get_public_key")
        )
        assertEquals(
            78748L,
            LiteServerRunSmcMethod.methodId("get_public_key")
        )
        assertEquals(
            102351L,
            LiteServerRunSmcMethod.methodId("get_nft_data")
        )
        assertEquals(
            102491L,
            LiteServerRunSmcMethod.methodId("get_collection_data")
        )
        assertEquals(
            92067L,
            LiteServerRunSmcMethod.methodId("get_nft_address_by_index")
        )
        assertEquals(
            85719L,
            LiteServerRunSmcMethod.methodId("royalty_params")
        )
        assertEquals(
            68445L,
            LiteServerRunSmcMethod.methodId("get_nft_content")
        )
    }
}