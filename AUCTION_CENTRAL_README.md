###### Alex Anastassatos
# AuctionCentral

## Functionality Summary
Serves as an intermediary between Agents and Auction Houses.

Connects Agents to Auction Houses.

Stores the bank keys of agents, and sends modify fund messages from auction houses to the bank.

## Message Interface

### Received
#### RegisterAgent
Sent from agent. Contains the agent name.
#### RegisterAuctionHouse
Sent from Auction house. Contains Auction House name. Used to register auction house.
#### DeregisterAuctionHouse
Sent from Auction house. Contains Auction House public ID. Used to remove Auction House from Auction Central.
#### RequestAuctionHouseList
Sent from agent. Used to request a list of active auction houses.
#### ModifyBlockedFunds
Sent from Auction House. Contains a bidding key, an amount, an enum representing whether to add or remove blocked funds, and a transaction ID. Bidding key is changed out with the corresponding bank key and sent on to the bank.
#### BlockFundsResult
Sent from the bank, contains a boolean representing the success of the fund blocking and a transaction ID.
#### WithdrawFunds
Sent from Auction house. Contains a bidding key, and an amount. Bidding key is changed out with the corresponding bank key and sent on to the bank.

### Send
#### AgentInfo
Sent to agent. Contains a bidding key.
#### AuctionHouseInfo
Sent to Auction House. Contains a public ID and a secret key.
#### DeregisterAuctionHouseResult
Sent to Auction House. Contains a boolean denoting the success of a deregistration.
#### AuctionHouseList
Sent to Agent. Contains a hashmap of Auction Houses and their public IDs.
#### ModifyBlockedFunds
Sent to bank. Contains a bank key, an amount, an enum representing whether to add or remove blocked funds, and a transaction ID.
#### BlockFundsResult
Sent to Auction House, contains a boolean representing the success of the fund blocking and a transaction ID.
#### WithDrawFunds
Sent to bank. Contains a bank key, and an amount to withdraw.
## Instructions
Run from console. Run the bank first and enter the address of the computer it is on. Run this and bank before running Auction House or Agent.

