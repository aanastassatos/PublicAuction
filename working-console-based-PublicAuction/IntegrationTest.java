import Agent.Agent;
import AuctionCentral.AuctionCentral;
import Bank.Bank;
import AuctionHouse.AuctionHouse;

public class IntegrationTest
{
  public static void main(String args[]) throws Exception
  {
    new Thread(() -> Bank.main(null)).start();
    Thread.sleep(1000);
    new Thread(() -> AuctionCentral.main(new String[]{"test"})).start();
    Thread.sleep(1000);
    new Thread(() -> AuctionHouse.main(new String[]{"test"})).start();
    Thread.sleep(1000);
    new Thread(() -> Agent.main(new String[]{"test"})).start();
  }
}
