#Auction House

#####Phuong Tran

##Functionality Summary

#####Holds auction houses.
#####Register to auction central.
#####Accepts bids from agents and return messages accordingly to the result.
#####Make requests to auction central to block funds and request money.
#####Close connection when done.

##Message Interface

###Received

####AgentInfo
#####Sent by an agent, contains the agent's bidding key so the Auction House can add this agent to a hashmap of agentClient and their bidding key.

####BidPlaced
#####Sent by an agent, contains the agent's bidding key, the item ID and the bid amount they want to bid on.

####CloseConnection
#####Sent by an agent, auction house will then close the connection with the agent, close the socket and sent a CloseConnectionMessage to the agent.  

####AuctionHouseInfo
#####Sent by Auction Central, contains the auction house publicID and the secret key and the auction house will store that infomation.

####RequestConnectionToAuctionHouse
#####Sent by Auction Central, contains the auction house ID and the agent ID. Auction House will send a AuctionHouseConnectionInfoMessage back.

####BlockFundsResult
#####Sent by Auction Central, contains the result and the transactionID. This will let the Auction House know whether the or not the agent who placed the bid has a sufficient fund.


###Sent

####BidResult
#####Sent to an agent, contains 4 types of results. Each is dealt with differently. 
#####  1. If an agent bids on an item that is no longer there, BidResult will send a message with result NOT_IN_STOCK.
#####  2. If an agent's bid is lower than current highest bid, BidResult will send a message with result BID_IS_TOO_LOW
#####  3. If an agent's bid is higher and the agent has enough money, BidResult will send a message with result SUCCESS
#####  4. If an agent's bid is higher but the agent doesnt have enough money, BidResult will send a message with result INSUFFICIENT_FUNDS. 

####HigherBidPlaced
#####Sent to agents, contains item ID and bid amount. If some higher bid was placed, auction house will send this message to all clients.

####ItemSold
#####Sent to agents. This message will be sent after a bid went through successfully to inform all clients.

####ModifyBlockedFunds
#####Sent to Auction Central. This message will be send after the agent's bid is higher than the current one. Auction House asks central to block the funds. 

####NoItemLeft
#####Sent to Agents. When the auction house sells all of its item, it will send the message to all clients.

####SuccessfulBid
#####Sent to Agents. When the clock counts down from 30 seconds and the time is up without any other agent places another bid(otherwise, the clock will get reset) this message will be sent to the highest current bidder.

####WithdrawFunds
#####Sent to Auction Central. When the time is up, then this message will be sent to ask central to send money that is the same cost as the item that is sold.