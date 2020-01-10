package com.template.states

import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.money.FiatCurrency
import net.corda.core.contracts.Amount
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.FungibleState
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.util.*

class SampleFixedTokenState(val currency: Currency, val tokenCreator: Party) : TokenType(currency.currencyCode, currency.defaultFractionDigits), ContractState {
       override val participants: List<AbstractParty>
        get() = listOf(tokenCreator)
}