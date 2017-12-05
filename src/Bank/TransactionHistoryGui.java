package Bank;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.awt.*;
import java.util.LinkedList;

class TransactionHistoryGui extends Stage
{
  TransactionHistoryGui(LinkedList<String> list)
  {
    final ListView<String> lv = new ListView<>(FXCollections.observableList(list));
    setScene(new Scene(lv));
    setTitle("Transaction History");
    final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    setWidth(d.getWidth()/4);
    setHeight(d.getHeight()/4);
    show();
  }
}
