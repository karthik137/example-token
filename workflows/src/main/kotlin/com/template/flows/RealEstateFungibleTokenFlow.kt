package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.TokenUtilities
import com.r3.corda.lib.tokens.contracts.utilities.getAttachmentIdForGenericParam
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.MoveFungibleTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.RedeemFungibleTokens
import com.r3.corda.lib.tokens.workflows.types.PartyAndAmount
//import com.r3.corda.lib.tokens.contracts.utilities.getAttachmentIdForGenericParam;

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


class RealEstateFungibleTokenFlow {

    @StartableByRPC
    class CreateEvolvableFungibleToken(
            val valuation: BigDecimal
    ) : FlowLogic<SignedTransaction>() {

        @Suspendable
        override fun call(): SignedTransaction {
         //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            //Get notary
            val notary = serviceHub.networkMapCache.notaryIdentities.single()

            //Create token type
            val realEstateTokenType: RealEstateTokenState = RealEstateTokenState(valuation, ourIdentity,  0, UniqueIdentifier())

            //Warp it within transaction state specifying notary
            val transactionState = TransactionState<EvolvableTokenType>(realEstateTokenType, notary = notary)

            //call built in sub flow CreateEvolvableTokens
            return subFlow(CreateEvolvableTokens(transactionState))
        }
    }

    @StartableByRPC
    class IssueEvolvableFungibleToken(
            val tokenId: String,
            val quantity: Long,
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

            //specify how much amount to issue to holder
            val amount = Amount<IssuedTokenType>(quantity,issuedTokenType)

            //Create fungible amount specifying the new user
            val fungibleToken  = FungibleToken(amount, holder, amount.token.tokenType.getAttachmentIdForGenericParam());

            //use built in flow for issuing tokens on ledger
            return subFlow( IssueTokens(listOf(fungibleToken)))
        }
    }

    @StartableByRPC
    class MoveEvolvableFungibleToken(
            val tokenId: String,
            val holder: Party,
            val quantity: Long
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
            val amount = Amount<TokenType>(quantity, tokenPointer)
            val partyAndAmount = PartyAndAmount(holder, amount)

            //use built in flow to move fungible tokens to holder
            return subFlow(MoveFungibleTokens(partyAndAmount))
        }
    }

    @StartableByRPC
    class RedeemEvolvableFungibleToken(
            val tokenId : String,
            val issuer: Party,
            val quantity: Long
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
            val amount = Amount<TokenType>(quantity, tokenPointer)
            //val partyAndAmount = PartyAndAmount(holder, amount)

            return subFlow(RedeemFungibleTokens(amount, issuer))
        }
    }
}