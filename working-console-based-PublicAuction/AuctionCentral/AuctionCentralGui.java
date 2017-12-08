/**
 * Created by Alex Anastassatos
 *
 * Makes a simple gui
 */

package AuctionCentral;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AuctionCentralGui extends Application
{
  private final double WIDTH = 500;
  private final double HEIGHT = 500;
  private String bankAddress;
  private Stage primaryStage;
//  private ObservableList<AgentNode> agentList;
//  private ObservableList<AuctionHouseNode> auctionHouseList;
  
//  private class AgentNode
//  {
//    private String name;
//    private int biddingKey;
//    private int bankKey;
//
//
//  }
//
//  private class AuctionHouseNode
//  {
//    private String name;
//    private int publicID;
//  }
  
  @Override
  public void start(Stage stage) throws Exception
  {
    primaryStage = stage;
    makeGui();
  }
  
  /**
   * Makes a super simple gui
   */
  private void makeGui()
  {
    Label addressLabel = new Label("Enter bank address (use localhost if server is on same computer as AuctionCentral): ");
    TextField addressField = new TextField();
    Button submit = new Button("Submit");
    submit.setOnAction(e->
    {
      try
      {
        AuctionCentral auctionCentral = new AuctionCentral(addressField.getText());
        submit.setText("Auction Central is Running");
        submit.setDisable(true);
        addressField.setDisable(true);
        new Alert(Alert.AlertType.INFORMATION, auctionCentral.printInfo()).showAndWait();
      } catch (IOException e1)
      {
        e1.printStackTrace();
      }
    });
  
    VBox vBox = new VBox();
    FlowPane addressPane = new FlowPane();
    addressPane.getChildren().addAll(addressLabel, addressField);
    vBox.getChildren().addAll(addressPane, submit);
    Scene scene = new Scene(vBox);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  public static void main(String [] args)
  {
    launch(args);
  }
}
