###### Dylan Balata
# Bank 

## Functionality Summary
Holds agent accounts. 

Accepts requests from auction central to place holds and withdraw funds from accounts.

Replys whether or not hold request are valid to auction central.

Note: Agents registering with the bank must have unique names.
  
## Message Interface

### Received
#### CreateBankAccount
Sent by an agent, contains the agents name and the initial deposit.
#### WithdrawFunds
Sent by auction central, contains amount to withdraw from specified account. Does not require a response
as an overdraw would indicate a logical failure in the fund blocking system (an exception is thrown).
#### BlockFunds
Sent by auction central, contains amount to (un)block from specified account
as well as whether the block should be placed or removed.

### Sent
#### BankAccountInfo
Sent to agents, contains the account number as well as their secret bank key.
#### BlockFundsResult
Sent to auction central, indicates whether last request to block funds succeded, i.e.
the specified account had more funds availiable than the amount to block requested.

## GUI Instructions (Requires GUI enabled jar)
Initially only a message stating no agents have registered is displayed.
Once an agent has created an account, the display format is: AgentName(AccountNumber) FundState. 
Clicking on any agent produces a new window in which the transaction history for said agent is displayed.
The funds displayed are updated in real time as transactions are processed.

## Testing Functionality
First run Bank, then BankTest. This tests all funcionality through passing messages in a highly
concurrent manner, indicating that no bugs will arise when high amounts of clients are indicating
with the bank at the same time. The use of RNG allows distributed timing and values, more closely emulating
a real use case.
