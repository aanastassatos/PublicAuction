package Bank;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;

class BankGui extends Stage
{
  private final ObservableList<BankAccount> accountList;
  private final ObservableList<Fund> infoList;
  private final ObservableList<Button> transactionHistoryList;

  BankGui()
  {
    accountList = FXCollections.observableList(new LinkedList<>());
    infoList = FXCollections.observableList(new LinkedList<>());
    transactionHistoryList = FXCollections.observableList(new LinkedList<>());

    SplitPane sp = new SplitPane();
    ListView<BankAccount> alv = new ListView<>(accountList);
    ListView<Fund> ilv = new ListView<>(infoList);
    ListView<Button> thlv = new ListView<>(transactionHistoryList);

//    ilv.setPrefWidth(2 * 1080/5);
//    alv.setPrefWidth(2 * 1080/5);
//    thlv.setPrefWidth(1 * 1080/5);
//    sp.setPrefWidth(1080);
    sp.getItems().addAll(alv, thlv, ilv);
    setScene(new Scene(sp));
    setWidth(1080);
    setHeight(720);
    setAlwaysOnTop(true);
    setTitle("Bank");
    show();

    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1/60d), event -> refreshFunds()));
    timeline.playFromStart();

    ScrollBar alvsb = (ScrollBar)alv.lookup(".scroll-bar");
    ScrollBar ilvsb = (ScrollBar)ilv.lookup(".scroll-bar");
    ScrollBar thlvsb = (ScrollBar)ilv.lookup(".scroll-bar");
    alvsb.valueProperty().bindBidirectional(ilvsb.valueProperty());
    alvsb.valueProperty().bindBidirectional(thlvsb.valueProperty());
    alvsb.setStyle("-fx-scale-x: 0");
    ilvsb.setStyle("-fx-scale-x: 0");
  }

  private void refreshFunds()
  {
    infoList.clear();
    accountList.forEach(x ->
    {
      infoList.add(x.getFund());
    });
  }

  void addAccount(final BankAccount account)
  {
    accountList.add(account);
    infoList.add(account.getFund());
    final Button b = new Button("History");
    b.setPrefHeight(14);
    b.setOnMouseClicked(e -> new TransactionHistoryGUI(account.getFund().getTransactionHistory()));
    transactionHistoryList.add(b);
  }
}
