package Bank;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.LinkedList;

class BankGui extends Stage
{
  private final ObservableList<AccountNode> boxList;

  private class AccountNode extends BorderPane
  {
    final private BankAccount account;
    final private Fund fund;

    private AccountNode(final BankAccount account, final Fund fund)
    {
      this.account = account;
      this.fund = fund;

      setLeft(new Text(account.toString()));
      setRight(new Text(fund.toString()));
    }

    Fund getFund()
    {
      return fund;
    }

    void updateFund()
    {
      setRight(new Text(account.getFund().toString()));
    }
  }

  BankGui()
  {
    boxList = FXCollections.observableList(new LinkedList<>());

    final ListView<AccountNode> boxView = new ListView<>(boxList);

    setScene(new Scene(new StackPane(boxView)));
    final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    setWidth(d.getWidth()/2);
    setHeight(d.getHeight()/2);
    setTitle("Bank");
    show();

    boxView.setOnMouseClicked(e -> openHistory(boxView));

    final Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1/60d), event -> refreshFunds()));
    timeline.playFromStart();
  }

  void addAccount(final BankAccount account)
  {
    final Fund f = account.getFund();
    final AccountNode box = new AccountNode(account, f);
    boxList.add(box);
  }

  private void openHistory(final ListView<AccountNode> boxView)
  {
    new TransactionHistoryGUI(boxView.getSelectionModel().getSelectedItem().getFund().getTransactionHistory());
  }

  private void refreshFunds()
  {
    boxList.forEach(b -> b.updateFund());
  }
}
