<p align="center">
  <img src="https://www.corda.net/wp-content/uploads/2016/11/fg005_corda_b.png" alt="Corda" width="500">
</p>

# CorDapp Template - Kotlin

Welcome to the Kotlin CorDapp template. The CorDapp template is a stubbed-out CorDapp that you can use to bootstrap 
your own CorDapps.

**This is the Kotlin version of the CorDapp template. The Java equivalent is 
[here](https://github.com/corda/cordapp-template-java/).**

# Pre-Requisites

See https://docs.corda.net/getting-set-up.html.

# Usage

## Running the nodes

See https://docs.corda.net/tutorial-cordapp.html#running-the-example-cordapp.

## Interacting with the nodes

### Shell

When started via the command line, each node will display an interactive shell:

    Welcome to the Corda interactive shell.
    Useful commands include 'help' to see what is available, and 'bye' to shut down the node.
    
    Tue Nov 06 11:58:13 GMT 2018>>>

You can use this shell to interact with your node. For example, enter `run networkMapSnapshot` to see a list of 
the other nodes on the network:

    Tue Nov 06 11:58:13 GMT 2018>>> run networkMapSnapshot
    [
      {
      "addresses" : [ "localhost:10002" ],
      "legalIdentitiesAndCerts" : [ "O=Notary, L=London, C=GB" ],
      "platformVersion" : 3,
      "serial" : 1541505484825
    },
      {
      "addresses" : [ "localhost:10005" ],
      "legalIdentitiesAndCerts" : [ "O=PartyA, L=London, C=GB" ],
      "platformVersion" : 3,
      "serial" : 1541505382560
    },
      {
      "addresses" : [ "localhost:10008" ],
      "legalIdentitiesAndCerts" : [ "O=PartyB, L=New York, C=US" ],
      "platformVersion" : 3,
      "serial" : 1541505384742
    }
    ]
    
    Tue Nov 06 12:30:11 GMT 2018>>> 

You can find out more about the node shell [here](https://docs.corda.net/shell.html).

### Client

`clients/src/main/kotlin/com/template/Client.kt` defines a simple command-line client that connects to a node via RPC 
and prints a list of the other nodes on the network.

#### Running the client

##### Via the command line

Run the `runTemplateClient` Gradle task. By default, it connects to the node with RPC address `localhost:10006` with 
the username `user1` and the password `test`.

##### Via IntelliJ

Run the `Run Template Client` run configuration. By default, it connects to the node with RPC address `localhost:10006` 
with the username `user1` and the password `test`.

### Webserver

`clients/src/main/kotlin/com/template/webserver/` defines a simple Spring webserver that connects to a node via RPC and 
allows you to interact with the node over HTTP.

The API endpoints are defined here:

     clients/src/main/kotlin/com/template/webserver/Controller.kt

And a static webpage is defined here:

     clients/src/main/resources/static/

#### Running the webserver

##### Via the command line

Run the `runTemplateServer` Gradle task. By default, it connects to the node with RPC address `localhost:10006` with 
the username `user1` and the password `test`, and serves the webserver on port `localhost:10050`.

##### Via IntelliJ

Run the `Run Template Server` run configuration. By default, it connects to the node with RPC address `localhost:10006` 
with the username `user1` and the password `test`, and serves the webserver on port `localhost:10050`.

#### Interacting with the webserver

The static webpage is served on:

    http://localhost:10050

While the sole template endpoint is served on:

    http://localhost:10050/templateendpoint
    
# Extending the template

You should extend this template as follows:

* Add your own state and contract definitions under `contracts/src/main/kotlin/`
* Add your own flow definitions under `workflows/src/main/kotlin/`
* Extend or replace the client and webserver under `clients/src/main/kotlin/`

For a guided example of how to extend this template, see the Hello, World! tutorial 
[here](https://docs.corda.net/hello-world-introduction.html).




### Sample token flow


#### NonFungibleToken flow


##### Create token
``` 
flow start CreateRealEstateNonFungibleToken valuation: 1000

 ✓ Starting
▶︎ Done
Flow completed with result: SignedTransaction(id=4DAAF73E8DB9301C954547C2C47DBA60CE889486E5C4ED111B583364CE6D8D82)
```

##### Issue tokens

```
start IssueRealEstateNonFungibleToken tokenId: 0a2828a4-717f-447b-9c82-20e8460cc424, holder: PartyB

 ✓ Starting
▶︎ Done
Flow completed with result: SignedTransaction(id=A7DC207DE8B917720009115079CF45A0B8D5C84C1C3F5359B53B8E684ED70D2C)
```


##### Run vaul query (EvolvableTokenType)

```
run vaultQuery contractStateType: com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
```

c5d14538-6108-411d-86d2-1da617b0b776

##### Issue tokens

```
start IssueRealEstateNonFungibleToken tokenId: c5d14538-6108-411d-86d2-1da617b0b776, holder: PartyB

 ✓ Starting
▶︎ Done
Flow completed with result: SignedTransaction(id=8423E10A385F7F3B7A3408E98F5E62D025974A8FBA8E569160B8A39D7702FA1C)
```

##### Vault query

```
run vaultQuery contractStateType: com.r3.corda.lib.tokens.contracts.states.NonFungibleToken


states:
- state:
    data: !<com.r3.corda.lib.tokens.contracts.states.NonFungibleToken>
      token:
        issuer: "O=PartyA, L=London, C=GB"
        tokenType:
          pointer:
            pointer:
              externalId: null
              id: "c5d14538-6108-411d-86d2-1da617b0b776"
            type: "com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType"
          fractionDigits: 0
      holder: "O=PartyB, L=New York, C=US"
      linearId:
        externalId: null
        id: "6c4681a2-7288-4388-8da7-bfb6d7580971"
      tokenTypeJarHash: null
    contract: "com.r3.corda.lib.tokens.contracts.NonFungibleTokenContract"
    notary: "O=Notary, L=London, C=GB"
    encumbrance: null
    constraint: !<net.corda.core.contracts.SignatureAttachmentConstraint>
      key: "aSq9DsNNvGhYxYyqA9wd2eduEAZ5AXWgJTbTEw3G5d2maAq8vtLE4kZHgCs5jcB1N31cx1hpsLeqG2ngSysVHqcXhbNts6SkRWDaV7xNcr6MtcbufGUchxredBb6"
  ref:
    txhash: "8423E10A385F7F3B7A3408E98F5E62D025974A8FBA8E569160B8A39D7702FA1C"
    index: 0
statesMetadata:
- ref:
    txhash: "8423E10A385F7F3B7A3408E98F5E62D025974A8FBA8E569160B8A39D7702FA1C"
    index: 0
  contractStateClassName: "com.r3.corda.lib.tokens.contracts.states.NonFungibleToken"
  recordedTime: "2020-01-07T09:49:53.100Z"
  consumedTime: null
  status: "UNCONSUMED"
  notary: "O=Notary, L=London, C=GB"
  lockId: null
  lockUpdateTime: null
  relevancyStatus: "RELEVANT"
  constraintInfo:
    constraint:
      key: "aSq9DsNNvGhYxYyqA9wd2eduEAZ5AXWgJTbTEw3G5d2maAq8vtLE4kZHgCs5jcB1N31cx1hpsLeqG2ngSysVHqcXhbNts6SkRWDaV7xNcr6MtcbufGUchxredBb6"
totalStatesAvailable: -1
stateTypes: "UNCONSUMED"
otherResults: []
```


##### Move non fungible token

```
 start MoveRealEstateNonFungibleToken tokenId: c5d14538-6108-411d-86d2-1da617b0b776, holder: PartyA

 ✓ Starting
▶︎ Done
Flow completed with result: SignedTransaction(id=EA4BD49C94B2F04FA949167F78D175056C673832EC12F007586F25D1FBF754A7)
```

##### Move non fungible token with linearId as parameter

```
start MoveRealEstateNonFungibleTokenWithLinearId tokenId: 705dba56-8e45-454f-a08c-b130916dde99, holder: PartyA, linearId: 895419b3-0f6f-46d8-9f90-9c5989d4cfa8
```



##### Redeem nonfungible token

```
start RedeemRealEstateNonFungibleToken tokenId: c5d14538-6108-411d-86d2-1da617b0b776, issuer: PartyA

 ✓ Starting
▶︎ Done
Flow completed with result: SignedTransaction(id=065F2EA3FBD20EA7E30FD9DAD56567DCFF53A08FCEFE22C745DE63BBCEA84B44)
```






#### Fungible token flow




##### Create evolvable fungible token

```
start CreateEvolvableFungibleToken valuation: 8000

 ✓ Starting
▶︎ Done
Flow completed with result: SignedTransaction(id=ACB38B0FBE4FD800E3D643D90C696852955359C9EA35C42AA7C83AD54D23DC9F)
```


##### Get evolvable fungible token from vault

```
run vaultQuery contractStateType: com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType


45b40d5e-486d-4139-ad85-58961467b175
```

##### Issue evolvable fungible token to partyB

```
start IssueEvolvableFungibleToken tokenId: 45b40d5e-486d-4139-ad85-58961467b175, quantity: 900, holder: PartyB

 ✓ Starting
▶︎ Done
Flow completed with result: SignedTransaction(id=F1389698FC7CA93E2F51475EA559C53EFFC6299EB0FA98A2832ABBE85BBE2FB7)
```


##### query fungible tokens at partyB side

```
run vaultQuery contractStateType: com.r3.corda.lib.tokens.contracts.states.FungibleToken    
states:
- state:
    data: !<com.r3.corda.lib.tokens.contracts.states.FungibleToken>
      amount: "900 TokenPointer(class com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType,\
        \ 45b40d5e-486d-4139-ad85-58961467b175) issued by PartyA"
      holder: "O=PartyB, L=New York, C=US"
      tokenTypeJarHash: null
    contract: "com.r3.corda.lib.tokens.contracts.FungibleTokenContract"
    notary: "O=Notary, L=London, C=GB"
    encumbrance: null
    constraint: !<net.corda.core.contracts.SignatureAttachmentConstraint>
      key: "aSq9DsNNvGhYxYyqA9wd2eduEAZ5AXWgJTbTEw3G5d2maAq8vtLE4kZHgCs5jcB1N31cx1hpsLeqG2ngSysVHqcXhbNts6SkRWDaV7xNcr6MtcbufGUchxredBb6"
  ref:
    txhash: "F1389698FC7CA93E2F51475EA559C53EFFC6299EB0FA98A2832ABBE85BBE2FB7"
    index: 0
statesMetadata:
- ref:
    txhash: "F1389698FC7CA93E2F51475EA559C53EFFC6299EB0FA98A2832ABBE85BBE2FB7"
    index: 0
  contractStateClassName: "com.r3.corda.lib.tokens.contracts.states.FungibleToken"
  recordedTime: "2020-01-07T10:14:41.470Z"
  consumedTime: null
  status: "UNCONSUMED"
  notary: "O=Notary, L=London, C=GB"
  lockId: null
  lockUpdateTime: "2020-01-07T10:14:41.507Z"
  relevancyStatus: "RELEVANT"
  constraintInfo:
    constraint:
      key: "aSq9DsNNvGhYxYyqA9wd2eduEAZ5AXWgJTbTEw3G5d2maAq8vtLE4kZHgCs5jcB1N31cx1hpsLeqG2ngSysVHqcXhbNts6SkRWDaV7xNcr6MtcbufGUchxredBb6"
totalStatesAvailable: -1
stateTypes: "UNCONSUMED"
otherResults: []
```

##### Move tokens

```
start MoveEvolvableFungibleToken tokenId: 45b40d5e-486d-4139-ad85-58961467b175, holder: PartyA, quantity: 100
```

##### Redeem tokens

```
start RedeemEvolvableFungibleToken tokenId: 45b40d5e-486d-4139-ad85-58961467b175, issuer: PartyA, quantity: 30

```




### FixedToken Type Example


#### Issue fungible FixedToken

```
start IssueFixedToken currencyCode: USD, amount: 1000, holder: PartyB
```


#### Query fungible fixed token

```
run vaultQuery contractStateType: com.r3.corda.lib.tokens.contracts.states.FungibleToken
```

#### Move fungible fixed token

```
start MoveFixedTokens tokenId: USD, holder: PartyC, quantity: 50000
```

#### Redeem fungible fixed token


```
start RedeemSampleFixedToken tokenId: USD, issuer: PartyA, quantity: 10000
```




