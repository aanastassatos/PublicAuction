package ChatClient;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class ChatClientWindow extends Application
{
  private Stage primaryStage;
  private String address;
  private int port;
  private String userName;
  private ChatClient client;
  private BufferedReader reader;
  
  public static void main(String [] args)
  {
    launch(args);
  }
  
  @Override
  public void start(Stage stage) throws Exception
  {
    primaryStage = stage;
    makeLogin();
  }
  
  private void makeLogin()
  {
    Label addressLabel = new Label("Enter server address (use localhost if server is on same computer as this client): ");
    Label portLabel = new Label("Enter port number: ");
    Label userNameLabel = new Label("Enter UserName: ");
    TextField addressField = new TextField();
    TextField portField = new TextField();
    TextField userNameField = new TextField();
    Button submit = new Button("Submit");
    submit.setOnAction(e->
    {
      address = addressField.getText();
      port = Integer.parseInt(portField.getText());
      userName = userNameField.getText();
      Task<Thread> task = new Task<Thread>()
      {
  
        @Override
        protected Thread call() throws Exception
        {
          try
          {
            client = new ChatClient(address, port, userName, true);
          } catch (IOException ex) {
            System.err.println("Something happened");
            ex.printStackTrace();
          }
            return null;
        }
      };
      
      new Thread(task).start();
      makeChatWindow();
    });
    
    VBox vBox = new VBox();
    FlowPane addressPane = new FlowPane();
    addressPane.getChildren().addAll(addressLabel, addressField);
    FlowPane portPane = new FlowPane();
    portPane.getChildren().addAll(portLabel,portField);
    FlowPane userNamePane = new FlowPane();
    userNamePane.getChildren().addAll(userNameLabel,userNameField);
    vBox.getChildren().addAll(addressPane, portPane, userNamePane, submit);
    Scene scene = new Scene(vBox);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  public void makeChatWindow()
  {
    reader = new BufferedReader(new InputStreamReader(System.in));
    TextArea chatBox = new TextArea();
    TextField chatField = new TextField();
    Button send = new Button("Send");
    chatBox.setEditable(false);
    Task task = new Task()
    {
      @Override
      protected Object call() throws Exception
      {
        while(true)
        {
          String line = reader.readLine();
          if (reader.readLine() != null) chatBox.setText(chatBox.getText() + "\n" +line);
  
          String temp = client.getReciever().getOutput();
          if (temp != null)
          {
            chatBox.appendText(temp + "\n");
            client.getReciever().setNullOutput();
          }
        }
      }
    };
  
    new Thread(task).start();

    send.setOnAction(e ->
    {
//      System.out.println(chatField.getText());
      String chatString = chatField.getText();
      client.getSender().setLine(chatString);
      chatField.clear();
    });

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask()
    {
      @Override
      public void run()
      {
        String temp = null;
        temp = client.getReciever().getOutput();
        
        if (temp != null)
        {
          chatBox.appendText(temp + "\n");
          client.getReciever().setNullOutput();
        }
      }
    }, 2000, 10);

    GridPane pane = new GridPane();
    pane.setAlignment(Pos.CENTER);
    GridPane.setConstraints(chatBox, 0,0);
    GridPane.setConstraints(chatField, 0,1);
    GridPane.setConstraints(send, 1,1);
    pane.getChildren().addAll(chatBox,chatField,send);
    Scene scene = new Scene(pane);
    primaryStage.setScene(scene);
    primaryStage.show();

  }
}
