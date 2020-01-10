package com.template.contracts

import com.r3.corda.lib.tokens.contracts.EvolvableTokenContract
import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

class RealEstateContract : EvolvableTokenContract(), Contract{
    override fun additionalCreateChecks(tx: LedgerTransaction) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun additionalUpdateChecks(tx: LedgerTransaction) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}