package Bank;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.LinkedList;

class TransactionHistoryGui extends Stage
{
  private ObservableList<String> observableList = FXCollections.observableList(new LinkedList<>());
  private final Fund fund;

  TransactionHistoryGui(final Fund fund)
  {
    this.fund = fund;
    observableList.addAll(fund.getTransactionHistory());
    final ListView<String> lv = new ListView<>(observableList);
    setScene(new Scene(lv));
    setTitle("Transaction History");
    final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    setWidth(d.getWidth()/4);
    setHeight(d.getHeight()/4);
    show();

    final Timeline timeline = new Timeline();
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1/60d), e -> refreshHistory()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.playFromStart();
  }

  private void refreshHistory()
  {
    fund.getTransactionHistory()
            .stream()
            .filter(s -> !observableList.contains(s))
            .forEach(observableList::add);
  }
}
