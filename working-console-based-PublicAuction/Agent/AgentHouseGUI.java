package Agent;

import AuctionHouse.Item;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AgentHouseGUI extends Stage
{
//  private Agent agent;
//
//  AgentHouseGUI(AgentAuctionHouse agentAuctionHouse, Agent agent)
//  {
//    this.agent = agent;
//    HashMap<Integer, Item> itemMap = agentAuctionHouse.getItems();
//    Collection<Item> items = itemMap.values();
//    ArrayList<Item> itemsArrayList = new ArrayList<>(items);
//    Label itemText = new Label();
//    Label itemNumber = new Label("Item Number to Bid");
//    TextField item = new TextField();
//    Label bidAmount = new Label("Bid Amount:");
//    TextField bidText = new TextField();
//    Button placeBid = new Button("Place Bid");
//
//    StringBuilder sB = new StringBuilder();
//    sB.append("Item       Price\n");
//    for(Item cat : items)
//    {
//      sB.append(cat.getItem() + "     ");
//      sB.append(cat.getPrice().toString() + "     ");
//      sB.append('\n');
//    }
//    itemText.setText(sB.toString());
//
//    placeBid.setOnAction(e -> {
//      int itemNum = Integer.parseInt(item.getText()) - 1;
//      agentAuctionHouse.setItemToBid(itemsArrayList.get(itemNum));
//      agentAuctionHouse.setAmountToBid(Integer.parseInt(bidText.getText()));
//      agentAuctionHouse.startAuction();
//      //new AgentSuccessGUI("Success!");
//    });
//
//    VBox vBox = new VBox(itemText, itemNumber, item, bidAmount, bidText, placeBid);
//    setScene(new Scene(vBox));
//    show();
//  }
}
