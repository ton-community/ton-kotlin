package org.ton.adnl.exception

import kotlinx.datetime.Instant
import org.ton.api.adnl.AdnlAddressList

public class AdnlTooNewAddressListReinitDateException(
    public val reinitDate: Instant,
    public val addressList: AdnlAddressList
) : RuntimeException("Too new reinit date: $reinitDate (${reinitDate.epochSeconds}) in address list: $addressList")
