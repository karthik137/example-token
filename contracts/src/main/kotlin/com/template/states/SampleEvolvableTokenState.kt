package com.template.states

import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.template.ExampleEvolvableTokenTypeContract
import com.template.contracts.SampleEvolvableContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party

@BelongsToContract(SampleEvolvableContract::class)
class SampleEvolvableTokenState : EvolvableTokenType {
    override val fractionDigits: Int = 0
    override val linearId: UniqueIdentifier
    val importantInformationThatMayChange: String
    val maintainer: Party

    companion object {
        val contractId = this::class.java.enclosingClass.canonicalName
    }

    constructor(maintainer: Party, importantInformationThatMayChange: String, linearId: UniqueIdentifier){
        this.linearId = linearId
        this.importantInformationThatMayChange = importantInformationThatMayChange
        this.maintainer = maintainer
    }


    override val maintainers: List<Party>
        get() = listOf(maintainer)

}

//@BelongsToContract(ExampleEvolvableTokenTypeContract::class)
//class SampleEvolvableTokenState(
//        val importantInformationThatMayChange: String,
//        val maintainer: Party,
//        override val linearId: UniqueIdentifier,
//        override val fractionDigits: Int = 0
//) : EvolvableTokenType() {
//    companion object {
//        val contractId = this::class.java.enclosingClass.canonicalName
//    }
//
//    override val maintainers: List<Party> get() = listOf(maintainer)
//}