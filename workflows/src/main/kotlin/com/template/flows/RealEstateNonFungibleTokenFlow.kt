package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.r3.corda.lib.tokens.contracts.states.NonFungibleToken
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.getAttachmentIdForGenericParam
import com.r3.corda.lib.tokens.workflows.flows.rpc.*
import com.r3.corda.lib.tokens.workflows.types.PartyAndAmount
import com.r3.corda.lib.tokens.workflows.types.PartyAndToken
import com.template.states.RealEstateTokenState
import net.corda.core.contracts.Amount
import net.corda.core.contracts.TransactionState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.transactions.SignedTransaction
import java.math.BigDecimal
import java.util.*

class RealEstateNonFungibleTokenFlow{

    @StartableByRPC
    class CreateRealEstateNonFungibleToken(
            val valuation: BigDecimal
    ) : FlowLogic<SignedTransaction> (){


        @Suspendable
        override fun call(): SignedTransaction {

            //grab the notary
            val notary = serviceHub.networkMapCache.notaryIdentities.single()

            //Create token type
            val realEstateTokenState = RealEstateTokenState(valuation, ourIdentity, 0, UniqueIdentifier())

            // Wrap it with transaction state specifying the notary
            val transactionState = TransactionState<EvolvableTokenType>(realEstateTokenState, notary = notary)

            return subFlow(CreateEvolvableTokens(transactionState))
        }
    }

    @StartableByRPC
    class IssueRealEstateNonFungibleToken(
            val tokenId: String,
            val holder: Party
    ) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            val uuid = UUID.fromString(tokenId);
            // Build query criteria with tokenId
            val queryCriteria = QueryCriteria.LinearStateQueryCriteria(uuid = listOf(uuid))
            val stateAndRef = serviceHub.vaultService.queryBy<RealEstateTokenState>(queryCriteria).states.single()

            //get RealEstateToken
            val realEstateToken = stateAndRef.state.data

            //Get the pointer to the house
            val tokenPointer = realEstateToken.toPointer<EvolvableTokenType>()

            // assign the issuer to the house type who will be issuing the tokens
            val issuedTokenType = IssuedTokenType(ourIdentity, tokenPointer)

            //Create fungible amount specifying the new user
            val nonFungibleToken = NonFungibleToken(issuedTokenType, holder, UniqueIdentifier())

            //use built in flow for issuing tokens on ledger
            return subFlow( IssueTokens(listOf(nonFungibleToken)))
        }
    }

    @StartableByRPC
    class MoveRealEstateNonFungibleToken(
            val tokenId: String,
            val holder: Party
    ) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {

            //get uuid from input tokenId
            val uuid = UUID.fromString(tokenId)

            //Create query criteria to get all unconsumed house states on ledger with uuid as input tokenId
            val queryCriteria = QueryCriteria.LinearStateQueryCriteria(uuid = listOf(uuid))
            val stateAndRef = serviceHub.vaultService.queryBy<RealEstateTokenState>(queryCriteria).states.single()

            // get the RealEstateTokenState object
            val realEstateTokenState = stateAndRef.state.data

            //get the token pointer to the house
            val tokenPointer = realEstateTokenState.toPointer<EvolvableTokenType>()

            //specify how much amount to transfer to which holder
            //val amount = Amount<TokenType>(quantity, tokenPointer)
            //val partyAndAmount = PartyAndAmount(holder, amount)

            val partyAndToken = PartyAndToken(holder, tokenPointer)
            //use built in flow to move fungible tokens to holder

            //Build new queryCriteria

            //val newQueryCriteria = QueryCriteria.LinearStateQueryCriteria()
            return subFlow(MoveNonFungibleTokens(partyAndToken))
        }
    }

    @StartableByRPC
    class MoveRealEstateNonFungibleTokenWithLinearId(
            val tokenId: String,
            val holder: Party,
            val linearId: String
    ) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {

            //get uuid from input tokenId
            val uuid = UUID.fromString(tokenId)
            val linearId = UUID.fromString(linearId)

            //Create query criteria to get all unconsumed house states on ledger with uuid as input tokenId
            val queryCriteria = QueryCriteria.LinearStateQueryCriteria(uuid = listOf(uuid))
            val stateAndRef = serviceHub.vaultService.queryBy<RealEstateTokenState>(queryCriteria).states.single()

            // get the RealEstateTokenState object
            val realEstateTokenState = stateAndRef.state.data

            //get the token pointer to the house
            val tokenPointer = realEstateTokenState.toPointer<EvolvableTokenType>()

            //specify how much amount to transfer to which holder
            //val amount = Amount<TokenType>(quantity, tokenPointer)
            //val partyAndAmount = PartyAndAmount(holder, amount)

            val partyAndToken = PartyAndToken(holder, tokenPointer)
            //use built in flow to move fungible tokens to holder

            //Build new queryCriteria

            val newQueryCriteria = QueryCriteria.LinearStateQueryCriteria(uuid = listOf(linearId))
            return subFlow(MoveNonFungibleTokens(partyAndToken, emptyList(), newQueryCriteria ))
        }
    }




    @StartableByRPC
    class RedeemRealEstateNonFungibleToken(
            val tokenId : String,
            val issuer: Party
    ) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {

            val uuid = UUID.fromString(tokenId)

            //Create query criteria to get all unconsumed house states on ledger with uuid as input tokenId
            val queryCriteria = QueryCriteria.LinearStateQueryCriteria(uuid = listOf(uuid))
            val stateAndRef = serviceHub.vaultService.queryBy<RealEstateTokenState>(queryCriteria).states.single()

            // get the RealEstateTokenState object
            val realEstateTokenState = stateAndRef.state.data

            //get the token pointer to the house
            val tokenPointer = realEstateTokenState.toPointer<EvolvableTokenType>()

            //specify how much amount to transfer to which holder
            //val amount = Amount<TokenType>(quantity, tokenPointer)
            //val partyAndAmount = PartyAndAmount(holder, amount)

            return subFlow(RedeemNonFungibleTokens(tokenPointer, issuer))
        }
    }
}