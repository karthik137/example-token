package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.r3.corda.lib.tokens.contracts.utilities.heldBy
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens
import com.template.states.SampleEvolvableTokenState
import net.corda.core.contracts.TransactionState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import java.util.*

// com/r3/corda/lib/tokens/selection/api/Selector
@StartableByRPC
class SampleEvolvableFlow(
        val evolvableTokenId: String,
        val amount: Long,
        val recipient: Party
): FlowLogic<SignedTransaction>() {

    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call(): SignedTransaction {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        // get vault
        val notary = serviceHub.networkMapCache
                .notaryIdentities.get(0)

        //get token state from vault
        val uuid = UUID.fromString(evolvableTokenId)
        val queryCriteria = QueryCriteria.LinearStateQueryCriteria(uuid = listOf(uuid))
        val tokenStateAndRef = serviceHub.vaultService.queryBy<EvolvableTokenType>(queryCriteria).states.single()
        val token = tokenStateAndRef.state.data.toPointer<EvolvableTokenType>()

        //Start the flow with a new flow session
        val issueTokensFlow = IssueTokens(listOf(amount of token issuedBy ourIdentity heldBy recipient))
        return subFlow(issueTokensFlow)
    }
}

@StartableByRPC
class CreateSampleEvolvableToken(val data: String) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call(): SignedTransaction {
        val notary = serviceHub.networkMapCache.notaryIdentities.single()
        val evolvableTokenType = SampleEvolvableTokenState(ourIdentity, data, linearId = UniqueIdentifier())
        val transactionState = TransactionState(evolvableTokenType, notary = notary)

        return subFlow(CreateEvolvableTokens(transactionState))
    }
}