package Bank;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class BankGui extends Stage
{
  private final ObservableList<AccountNode> boxList;
  final private PlaceHolderNode placeHolder = new PlaceHolderNode();

  private class AccountNode extends BorderPane
  {
    final private BankAccount account;
    final private Fund fund;

    private AccountNode(final BankAccount account, final Fund fund)
    {
      this.account = account;
      this.fund = fund;
    }

    void init()
    {
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

  private class PlaceHolderNode extends AccountNode
  {
    private PlaceHolderNode()
    {
      super(null, null);
      setLeft(new Text("No accounts active."));
    }

    @Override
    void updateFund(){}
  }

  BankGui(final Bank bank)
  {

    setOnCloseRequest(e -> bank.shutdown());
    boxList = FXCollections.observableList(new LinkedList<>());
    boxList.add(placeHolder);
    final ListView<AccountNode> boxView = new ListView<>(boxList);

    setScene(new Scene(new StackPane(boxView)));
    final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    setWidth(d.getWidth()/2);
    setHeight(d.getHeight()/2);
    setTitle("Bank");
    show();

    boxView.setOnMouseClicked(e -> openHistory(boxView));

    final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    service.scheduleAtFixedRate(this::refreshFunds, 0, 100, TimeUnit.MILLISECONDS);
  }

  void addAccount(final BankAccount account)
  {
    if(boxList.contains(placeHolder)) boxList.remove(placeHolder);
    final Fund f = account.getFund();
    final AccountNode box = new AccountNode(account, f);
    box.init();
    boxList.add(box);
  }

  private void openHistory(final ListView<AccountNode> boxView)
  {
    new TransactionHistoryGUI(boxView.getSelectionModel().getSelectedItem().getFund().getTransactionHistory());
  }

  private void refreshFunds()
  {
    Platform.runLater(() -> boxList.forEach(b -> b.updateFund()));
  }
}
