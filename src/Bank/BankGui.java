package Bank;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;

public class BankGui extends Stage
{
  private final ObservableList<BankAccount> accountList;
  private final ObservableList<String> infoList;
  private final Text accountInfo = new Text();
  private final Timeline timeline = new Timeline();

  private BankAccount currentlySelected;

  BankGui(final List<BankAccount> accounts)
  {
    accountList = FXCollections.observableList(accounts);
    infoList = FXCollections.observableList(new LinkedList<>());

    accountInfo.setFont(new Font(24));

    BorderPane bp = new BorderPane();
    ListView<BankAccount> alv = new ListView<>(accountList);
    ListView<String> ilv = new ListView<>(infoList);

    ilv.setPrefWidth(1080/2);
    bp.setCenter(alv);
    bp.setRight(ilv);
    setScene(new Scene(bp));
    setWidth(1080);
    setHeight(720);
    setAlwaysOnTop(true);
    setTitle("Bank");
    show();

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1/60d), event -> updateText()));
    timeline.playFromStart();

    ScrollBar alvsb = (ScrollBar)alv.lookup(".scroll-bar");
    ScrollBar ilvsb = (ScrollBar)ilv.lookup(".scroll-bar");
    alvsb.valueProperty().bindBidirectional(ilvsb.valueProperty());
    alvsb.setStyle("-fx-scale-x: 0");

  }

  private void updateText()
  {
    infoList.clear();
    accountList.forEach(x -> infoList.add(x.getFund().toString()));
  }

  void addAccount(final BankAccount account)
  {
    accountList.add(account);

  }
}
