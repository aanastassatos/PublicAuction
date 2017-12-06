package AuctionHouse;

import javafx.collections.ObservableList;

public class AuctionHouseGui
{
  private final double WIDTH = 500;
  private final double HEIGHT = 500;
  private ObservableList<AuctionHouseNode> agentList;

  private class AuctionHouseNode
  {
    private String name;
    private int biddingKey;
    private int bankKey;
  }

}
