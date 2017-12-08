package Agent;

import AuctionHouse.Item;
import Messages.*;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class AgentAuctionHouse extends Thread
{
  final Agent agent;
  volatile HashMap<Integer, Item> items;
  private int biddingKey;
  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  private Item itemToBid;
  private Integer amountToBid;

  private String success;

  AgentAuctionHouse(int biddingKey, String address, int port, Agent agent)
  {
    this.agent = agent;
    this.biddingKey = biddingKey;
    new Thread(() -> connectToHouse(address, port)).start();
  }

  void setItemToBid(Item item)
  {
    this.itemToBid = item;
  }

  void setAmountToBid(int bidAmount)
  {
    this.amountToBid = bidAmount;
  }

  public void connectToHouse(String address, int port)
  {
    try
    {
      final Socket houseSocket = new Socket(address, port);
      oos = new ObjectOutputStream(houseSocket.getOutputStream());
      ois = new ObjectInputStream(houseSocket.getInputStream());
      oos.writeObject(new AgentInfoMessage(biddingKey));
      ItemListMessage itemsMessage = ((ItemListMessage) ois.readObject());
      items = itemsMessage.getItemList();
    }

    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  String getSuccess() {
    return success;
  }

  void startAuction()
  {
    try
    {
      while (true)
      {
        if (itemToBid != null)
        {
          oos.writeObject(new BidPlacedMessage(biddingKey, itemToBid.getID(), amountToBid));
          itemToBid = null;
        }
        Object readMessage = ois.readObject();
        if (readMessage instanceof ItemListMessage)
        {
          ItemListMessage listMessage = (ItemListMessage) readMessage;
          items = listMessage.getItemList();
          //agent.setItems(items);
        } else if (readMessage instanceof SuccessfulBidMessage) handleMessage((SuccessfulBidMessage) readMessage);
        else if (readMessage instanceof BidResultMessage) handleMessage((BidResultMessage) readMessage);
        else if (readMessage instanceof NoItemLeftMessage)
        {
          oos.writeObject(new CloseConnectionMessage());
          break;
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  HashMap<Integer, Item> getItems()
  {
    while(items == null)
    {

    }
    return items;
  }

  private void handleMessage(SuccessfulBidMessage message)
  {
    success = "You Won!";
  }

  private void handleMessage(BidResultMessage message)
  {
    String result;
    BidResultMessage.BidResult bidResult = message.getResult();
    switch(bidResult)
    {
      case BID_IS_TOO_LOW:
        result = "Bid is too low!";
        break;
      case SUCCESS:
        result = "Bid Placed!";
        break;
      case INSUFFICIENT_FUNDS:
        result = "Insufficient Funds in Your Account :(";
        break;
      case NOT_IN_STOCK:
        result = "Item Not In Stock";
        break;
      default:
        result = "Here Be Monsters";
        break;
    }
    Platform.runLater(() -> new AgentSuccessGUI(result));
  }
}
