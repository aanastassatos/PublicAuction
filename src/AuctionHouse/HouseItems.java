package AuctionHouse;

import Messages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


class HouseItems extends Thread
{
  private List<Item> itemL = new ArrayList();
  private List<Item> houseItems = new ArrayList();
  private final Socket socket;
  private final AuctionHouseCentral auctionHouse;

  HouseItems(int numItems, Socket socket, AuctionHouseCentral auctionHouse)
  {
    this.socket = socket;
    this.auctionHouse = auctionHouse;
    makeItems();
    houseItems = getNitems(numItems);
  }

  private void makeItems()
  {
    for (char i = 'A'; i < 'z'; i++)
    {
      final String anItem = Character.toString(i);
      final Item item = new Item(anItem);
      itemL.add(item);
    }
  }

  private List<Item> getNitems(int n)
  {
    makeItems();
    List<Item> copy = new ArrayList<>(itemL);
    Collections.shuffle(copy);
    return copy.subList(0, n);
  }

}
