package Bank;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
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

  BankGui()
  {
    accountList = FXCollections.observableList(new LinkedList<>());
    infoList = FXCollections.observableList(new LinkedList<>());

    SplitPane sp = new SplitPane();
    ListView<BankAccount> alv = new ListView<>(accountList);
    ListView<Fund> ilv = new ListView<>(infoList);

    ilv.setPrefWidth(1080/2);
    sp.getItems().addAll(alv, ilv);
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
    alvsb.valueProperty().bindBidirectional(ilvsb.valueProperty());
    alvsb.setStyle("-fx-scale-x: 0");

  }

  private void refreshFunds()
  {
    infoList.clear();
    accountList.forEach(x -> infoList.add(x.getFund()));
  }

  void addAccount(final BankAccount account)
  {
    accountList.add(account);

  }
}
