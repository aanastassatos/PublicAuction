package Bank;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.LinkedList;

public class TransactionHistoryGUI extends Stage
{
  TransactionHistoryGUI(LinkedList<String> list)
  {
    StringBuilder sb = new StringBuilder();
    list.forEach(s -> sb.append(s + '\n'));
    setScene(new Scene(new StackPane(new Text(sb.toString()))));
    setTitle("Transaction History");
    show();
  }
}
