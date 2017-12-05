package AuctionCentral;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AuctionCentralGui extends Application
{
  private final double WIDTH = 500;
  private final double HEIGHT = 500;
  private ObservableList<AgentNode> agentList;
  private ObservableList<AuctionHouseNode> auctionHouseList;
  
  private class AgentNode
  {
    private String name;
    private int biddingKey;
    private int bankKey;
    
    
  }
  
  AuctionCentralGui()
  {
  }
  
  @Override
  public void start(Stage stage) throws Exception
  {
  }
}
