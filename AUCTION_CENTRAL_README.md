###### Alex Anastassatos
# AuctionCentral

## Functionality Summary
Serves as an intermediary between Agents and Auction Houses.

Connects Agents to Auction Houses.

Stores the bank keys of agents, and sends modify fund messages from auction houses to the bank.

## Message Interface

### Received
#### RegisterAgent

#### RegisterAuctionHouse

#### DeregisterAuctionHouse

#### RequestAuctionHouseList

#### ModifyBlockedFunds

#### BlockFundsResult

#### WithdrawFunds


### Send
#### AgentInfo

#### AuctionHouseInfo

#### DeregisterAuctionHouseResult

#### AuctionHouseList

#### ModifyBlockedFunds

#### BlockFundsResult

#### WithDrawFunds

## GUI Instructions

