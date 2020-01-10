package com.template.states

import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.template.contracts.RealEstateContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import java.math.BigDecimal

@BelongsToContract(RealEstateContract::class)
class RealEstateTokenState : EvolvableTokenType {

    val valuation : BigDecimal
    val maintainer : Party
    override val fractionDigits: Int
    override val linearId: UniqueIdentifier

    constructor(valuation: BigDecimal, maintainer: Party, fractionDigits: Int, linearId: UniqueIdentifier) {
        this.valuation = valuation
        this.maintainer = maintainer
        this.fractionDigits = fractionDigits
        this.linearId = linearId
    }

    override val maintainers: List<Party>
        get() = listOf(maintainer)
}