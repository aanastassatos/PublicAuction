package Agent;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AgentSuccessGUI extends Stage
{
  AgentSuccessGUI(String message)
  {
    setWidth(200);
    setHeight(200);
    Label dispMessage = new Label(message);
    Button hideScene = new Button("OK.");
    hideScene.setOnAction(e -> {

    });
    VBox vBox = new VBox(dispMessage, hideScene);
    setScene(new Scene(vBox));
    show();
  }
}
