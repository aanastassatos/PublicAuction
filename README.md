# PublicAuction

## General Instructions
Run individual jars in the order Bank -> AuctionCentral -> AuctionHouse -> Agent, following the on screen prompts to enter IP addresses
Once all are running, you will be prompted for a name and initial deposit amount for the Bank. This bank account info will then allow the
agent to register with Auction Central, resulting in the delivery of a connection to auction houses. All of this happens automatically. At
this point the user will be prompted to choose an auction house, then given the option to bid on an auction. The bidding, timing, and funds part 
of this process can be observed through the component's respective stdouts. At this point the agent(user) can choose to bid on another 
auction or exit.

## Integration Test
The integration test allows for an easy way to test component functionality, with the draw back that there are no longer separate processes
for each component, just separate threads.

## Refer to the individual component READMEs for more detailed information.
