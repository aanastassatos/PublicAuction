package ChatServer;

import ChatClient.ChatClient;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatServerWindow extends Application
{
  private int port;
  private ChatServer server;
  
  @Override
  public void start(Stage stage) throws Exception
  {
    Label portLabel = new Label("Enter port number: ");
    TextField portField = new TextField();
    FlowPane portPane = new FlowPane();
  
    Button submit = new Button("Submit");
    submit.setOnAction(e->
    {
      port = Integer.parseInt(portField.getText());
      Task<Thread> task = new Task<Thread>()
      {
      
        @Override
        protected Thread call() throws Exception
        {
          try
          {
            server = new ChatServer(port);
          } catch (IOException ex) {
            System.err.println("Something happened");
            ex.printStackTrace();
          }
          return null;
        }
      };
    
      new Thread(task).start();
    });
    
    portPane.getChildren().addAll(portLabel,portField, submit);
  
    Scene scene = new Scene(portPane);
    stage.setScene(scene);
    stage.show();
  }
}
