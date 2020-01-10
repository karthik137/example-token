package com.template.contracts

import com.r3.corda.lib.tokens.contracts.EvolvableTokenContract
import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

class SampleEvolvableContract : EvolvableTokenContract(), Contract {
    override fun additionalCreateChecks(tx: LedgerTransaction) {
        return Unit
    }

    override fun additionalUpdateChecks(tx: LedgerTransaction) {
        return Unit
    }

}