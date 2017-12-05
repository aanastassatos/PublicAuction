package Agent;

import AuctionHouse.Item;


import java.util.HashMap;
import java.util.Scanner;

public class Agent extends Thread
{
  HashMap<Integer, Item> auctionHouseItems;
  int itemToBidOn;

  public Agent()
  {
    AgentBankAccount bankAccount = new AgentBankAccount();
    String hostname = bankAccount.getHostName();
    String name = bankAccount.getAgentName();
    int secretKey = bankAccount.getSecretKey();

    AgentAuctionCentral auctionCentral = new AgentAuctionCentral(hostname, name, secretKey, this);
    int house = auctionCentral.getHouse();

    int biddingKey = auctionCentral.getBiddingKey();
    int port = auctionCentral.getPort();
    String address = auctionCentral.getAddress();
    System.out.println("Address = " + address + "port is: " + port);
    AgentAuctionHouse auctionHouse = new AgentAuctionHouse(biddingKey, address, port, this);

  }

  public static void main(String[] args) //throws Exception
  {
    Agent agent = new Agent();
    agent.start();
  }

  void setItems(HashMap<Integer, Item> items)
  {
    this.auctionHouseItems = items;
  }

  void printItems()
  {
    for(Item item : auctionHouseItems.values())
    {
      System.out.println(item);
    }
  }


  @Override
  public void run()
  {
    while(true)
    {
      if(auctionHouseItems != null)
      {
        System.out.println("Size = " + auctionHouseItems.size());
        printItems();
        System.out.print("Which item to bid on?: ");
        Scanner scanner = new Scanner(System.in);
        itemToBidOn = scanner.nextInt();
      }
    }
  }
}
