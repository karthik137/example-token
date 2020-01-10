package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.r3.corda.lib.tokens.contracts.states.NonFungibleToken
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.heldBy
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveFungibleTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.RedeemFungibleTokens
import com.r3.corda.lib.tokens.workflows.types.PartyAndAmount
import com.template.states.SampleFixedTokenState
import net.corda.core.contracts.Amount
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party

import net.corda.core.transactions.SignedTransaction
import java.util.*


class SampleFixedTokenFlow {

    @StartableByRPC
    class IssueFixedToken(
            val currencyCode: String,
            val amount: Long,
            val holder: Party
    ) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

//            //Get notary
//            val notary = serviceHub.networkMapCache.notaryIdentities.single()
//
//            // Get currency from currency utility
            val currency = Currency.getInstance(currencyCode)
            val sampleFixedToken = SampleFixedTokenState(currency = currency, tokenCreator = ourIdentity)

            val issuedToken = IssueTokens(listOf(amount of sampleFixedToken issuedBy ourIdentity heldBy holder))
            return subFlow(issuedToken)
        }

    }

    @StartableByRPC
    class MoveFixedTokens(val tokenId: String,
                          val holder: Party,
                          val quantity: Long
    ) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {
            //get uuid from input tokenId
//            val uuid = UUID.fromString(tokenId)

//            //Create query criteria to get all unconsumed house states on ledger with uuid as input tokenId
//            val queryCriteria = QueryCriteria.VaultQueryCriteria(uuid)
//            val stateAndRef = serviceHub.vaultService.queryBy<SampleFixedTokenState>(queryCriteria).states.single()
//
//            // get the sampleFixedTokenObject object
//            val sampleFixedTokenState = stateAndRef.state.data
//
//            //get the token pointer to the house
//            val tokenPointer = sampleFixedTokenState
//
//            //specify how much amount to transfer to which holder
//            val amount: Amount<TokenType> = Amount<TokenType>(quantity, tokenPointer)
//            val partyAndAmount = PartyAndAmount(holder, amount)
//
//            //use built in flow to move fungible tokens to holder
//            return subFlow(MoveFungibleTokens(partyAndAmount))

            // Retrieve the SampleFixedTokenState
            val sampleTokenStateAndRef = serviceHub.vaultService.queryBy(FungibleToken::class.java).states.single()

            val  sampleFixedTokenState = sampleTokenStateAndRef.state.data


            //specify how much amount to transfer
            val amount = Amount<TokenType> (quantity, sampleTokenStateAndRef.state.data.tokenType)
            val partyAndAmount = PartyAndAmount(holder, amount)

            //use built in flow to move Fixed fungible token
            return subFlow(MoveFungibleTokens(partyAndAmount = partyAndAmount))
        }
    }

    @StartableByRPC
    class RedeemSampleFixedToken(
            val tokenId : String,
            val issuer: Party,
            val quantity: Long
    ) : FlowLogic<SignedTransaction>() {


        @Suspendable
        override fun call(): SignedTransaction {

            val sampleTokenStateAndRef = serviceHub.vaultService.queryBy(FungibleToken::class.java).states.single()

            val  sampleFixedTokenState = sampleTokenStateAndRef.state.data

            //specify how much amount to transfer to which holder
            val amount = Amount<TokenType>(quantity, sampleTokenStateAndRef.state.data.tokenType)
            //val partyAndAmount = PartyAndAmount(holder, amount)

            return subFlow(RedeemFungibleTokens(amount, issuer))
        }
    }
}