package Bank;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.LinkedList;

class BankGui extends Stage
{
  private final ObservableList<AccountNode> boxList;
  private final HashSet<BankAccount> accountSet = new HashSet<>();

  private class AccountNode extends BorderPane
  {
    final private BankAccount account;
    private Fund fund;

    private AccountNode(final BankAccount account, final Fund fund)
    {
      this.account = account;
      this.fund = fund;

      setLeft(new Text(account.toString()));
      setRight(new Text(fund.toString()));
    }

    public BankAccount getAccount()
    {
      return account;
    }

    public Fund getFund()
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

    ListView<AccountNode> boxView = new ListView<>(boxList);

    setScene(new Scene(new StackPane(boxView)));
    setWidth(1080);
    setHeight(720);
    setTitle("Bank");
    show();

    boxView.setOnMouseClicked(e -> openHistory(boxView));

    Timeline timeline = new Timeline();
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1/60d), event -> refreshFunds()));
    timeline.playFromStart();
  }

  private void openHistory(final ListView<AccountNode> boxView)
  {
    new TransactionHistoryGUI(boxView.getSelectionModel().getSelectedItem().getFund().getTransactionHistory());
  }

  private void refreshFunds()
  {
    boxList.forEach(b -> b.updateFund());
  }

  void addAccount(final BankAccount account)
  {
    final Fund f = account.getFund();
    AccountNode box = new AccountNode(account, f);
    boxList.add(box);
    accountSet.add(account);
  }
}
