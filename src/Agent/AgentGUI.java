package Agent;

import AuctionHouse.AuctionHouse;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class AgentGUI extends Stage
{
  private String hostname;
  private String name;
  private int depositAmount;
  private boolean finished = false;

  private int auctionHouse;
  private boolean housePicked = false;

  Agent agent;

  String getHostname() { return hostname; }

  String getName() { return name; }

  int getDepositAmount() { return depositAmount; }

  private VBox connectToBank()
  {
    Button proceed = new Button("Proceed");
    TextField hostNameText = new TextField();
    TextField screenNameText = new TextField();
    TextField depositText = new TextField();

    Label hostNameLabel = new Label("Enter Host Name: ");
    Label screenNameLabel = new Label("Enter Screen Name: ");
    Label depositLabel = new Label("Deposit Amount: ");
    proceed.setOnAction(e -> {
      hostname = hostNameText.getText();
      name = screenNameText.getText();
      depositAmount = Integer.parseInt(depositText.getText());
      finished = true;
      agent.activateBank(hostname, name, depositAmount);
      //hide();
    });
    return new VBox(hostNameLabel, hostNameText, screenNameLabel, screenNameText,
            depositLabel, depositText, proceed);
  }

  boolean isFinished() { return finished; }

  void setFinished(boolean finished) { this.finished = finished; }

  void showAuctionHouses(AgentAuctionCentral auctionCentral)
  {
    HashMap<Integer, String> houseMap = auctionCentral.getHouses();
    setTitle("Auction Houses");
    TextField houseChoice = new TextField();
    TextArea housesList = new TextArea();
    Button showItems = new Button("Open House.");
    Collection<String> names = houseMap.values();
    int i = 1;
    for(String name : names)
    {
      housesList.appendText(String.format("Available House %d: ", i));
      housesList.appendText(name);
      housesList.appendText("\n");
      i++;
    }

    showItems.setOnAction(e -> {
      auctionHouse = Integer.parseInt(houseChoice.getText()) - 1;
      housePicked = true;
      auctionCentral.selectHouse(auctionHouse);
      agent.activateHouse(auctionCentral);
    });

    VBox vBox = new VBox(housesList, houseChoice, showItems);
    setScene(new Scene(vBox));
    show();
  }

  AgentGUI(Agent agent)
  {
    this.agent = agent;
    setTitle("Agent");
    setScene(new Scene(connectToBank()));
    show();
  }
}
