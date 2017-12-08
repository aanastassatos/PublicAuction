package Agent;

import AuctionHouse.Item;
import Messages.BidResultMessage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import java.util.HashMap;

public class Agent extends Thread
{
  int secretKey;
  AgentAuctionHouse agentAuctionHouse;

  AgentGUI agentGui;

  public Agent()
  {
    new JFXPanel();
    Platform.runLater(() -> agentGui = new AgentGUI(this));
  }

  public void activateBank(String hostname, String name, int deposit)
  {
    AgentBankAccount bankAccount = new AgentBankAccount(this);
    new Thread(() -> bankAccount.connectToBank(hostname, name, deposit)).start();
    secretKey = bankAccount.getSecretKey();
    AgentAuctionCentral auctionCentral = new AgentAuctionCentral(hostname, name, secretKey, this);
    HashMap<Integer, String> map = auctionCentral.getHouses();
    agentGui.showAuctionHouses(auctionCentral);
  }

  public void activateHouse(AgentAuctionCentral auctionCentral)
  {
    int biddingKey = auctionCentral.getBiddingKey();
    int port = auctionCentral.getPort();
    String address = auctionCentral.getAddress();
    System.out.println("Address = " + address + "port is: " + port);

    agentAuctionHouse = new AgentAuctionHouse(biddingKey, address, port, this);
    agentGui.openAuctionHouse();
  }

  AgentAuctionHouse getAgentAuctionHouse()
  {
    return agentAuctionHouse;
  }

  public static void main(String[] args) //throws Exception
  {
    Agent agent = new Agent();
    agent.start();
  }

//  void setItems(HashMap<Integer, Item> items)
//  {
//    this.auctionHouseItems = items;
//    //printItems();
//  }
//
//  void printItems()
//  {
//    for(Item item : auctionHouseItems.values())
//    {
//      System.out.println("Item Name: " + item.getItem() + " Item ID: " + item.getID() + " Price: " + item.getPrice());
//    }
//  }
//
//  Integer getItemToBidOn() { return itemToBidOn; }
//
//  int getAmountBid() { return amountToBid; }

  @Override
  public void run()
  {
    while(agentGui == null)
    {

    }
    while(true)
    {
      if(agentGui.isFinished())
      {
        activateBank(agentGui.getHostname(), agentGui.getName(), agentGui.getDepositAmount());
        agentGui.setFinished(false);
      }
//      if(auctionHouseItems != null)
//      {
//        System.out.print("Which item to bid on?: ");
//        Scanner scanner = new Scanner(System.in);
//        itemToBidOn = scanner.nextInt();
//        System.out.print("How much?: ");
//        amountToBid = scanner.nextInt();
//      }
    }
  }
}
