package Agent;

import AuctionHouse.Item;

import java.net.Socket;
import java.util.ArrayList;

public class Agent extends Thread
{
  ArrayList<Item> auctionHouseItems;

  public Agent()
  {
    AgentBankAccount bankAccount = new AgentBankAccount();
    String hostname = bankAccount.getHostName();
    String name = bankAccount.getAgentName();
    int secretKey = bankAccount.getSecretKey();

    AgentAuctionCentral auctionCentral = new AgentAuctionCentral(hostname, name, secretKey);
    int biddingKey = auctionCentral.getBiddingKey();
    Socket houseSocket = auctionCentral.getAuctionHouseSocket();
    AgentAuctionHouse auctionHouse = new AgentAuctionHouse(biddingKey, houseSocket, this);
  }

  public static void main(String[] args) //throws Exception
  {
    Agent agent = new Agent();
  }

  void setItems(ArrayList<Item> items)
  {
    this.auctionHouseItems = items;
  }

  void printItems()
  {
    for(Item item : auctionHouseItems)
    {
      System.out.println(item);
    }
  }


  @Override
  public void run()
  {
    while(true)
    {
      if(auctionHouseItems.size() != 0)
      {
        printItems();

      }
    }
  }
}
