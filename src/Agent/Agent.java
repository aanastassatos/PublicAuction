package Agent;

public class Agent extends Thread
{

  public Agent()
  {
    AgentBankAccount bankAccount = new AgentBankAccount();
    String hostname = bankAccount.getHostName();
    String name = bankAccount.getAgentName();
    int secretKey = bankAccount.getSecretKey();

    AgentAuctionCentral auctionCentral = new AgentAuctionCentral(hostname, name, secretKey);
    AgentAuctionHouse auctionHouse = new AgentAuctionHouse(auctionCentral.getAuctionHouseSocket());
  }

  public static void main(String[] args) throws Exception
  {
    Agent agent = new Agent();
  }


  @Override
  public void run()
  {
    while(true)
    {

    }
  }
}
