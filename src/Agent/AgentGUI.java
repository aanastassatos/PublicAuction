package Agent;

import AuctionHouse.AuctionHouse;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class AgentGUI extends Stage
{
  private String hostname;
  private String name;
  private int depositAmount;
  private boolean finished = false;
  private Button proceed;

  private int auctionHouse;

  Agent agent;

  String getHostname() { return hostname; }

  String getName() { return name; }

  int getDepositAmount() { return depositAmount; }

  private VBox connectToBank()
  {
    proceed = new Button("Proceed");
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
      setFinished(true);
      hide();
      agent.activateBank(hostname, name, depositAmount);
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
    Label housesList = new Label();
    Button showItems = new Button("Open House.");
    Collection<String> names = houseMap.values();
    int i = 1;
    StringBuilder sB = new StringBuilder();
    for(String name : names)
    {
      sB.append(String.format("Available House %d: %s\n", i, name));
      i++;
    }
    housesList.setText(sB.toString());

    showItems.setOnAction(e -> {
      auctionHouse = Integer.parseInt(houseChoice.getText()) - 1;
      auctionCentral.selectHouse(auctionHouse);
      agent.activateHouse(auctionCentral);
    });

    VBox vBox = new VBox(housesList, houseChoice, showItems);
    Scene sceneBox = new Scene(vBox);
    sceneBox.setOnKeyPressed(e -> {
      if(e.getCode() == KeyCode.ENTER) showItems.fire();
    });
    setScene(sceneBox);
    show();
  }

  void openAuctionHouse()
  {
    new AgentHouseGUI(agent.getAgentAuctionHouse(), agent);
  }

  AgentGUI(Agent agent)
  {
    this.agent = agent;
    setTitle("Agent");
    Scene scene = new Scene(connectToBank());
    scene.setOnKeyPressed(e -> {
      if(e.getCode() == KeyCode.ENTER) proceed.fire();
    });
    setScene(scene);
    show();
  }
}
