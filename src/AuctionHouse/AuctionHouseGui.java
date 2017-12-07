package AuctionHouse;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.LinkedList;

class AuctionHouseGui extends Stage
{
  // GUI SHOULD PRINT OUT THE LIST OF AUCTION HOUSE ITEMS AND ITS UPDATE
  private final ObservableList<AuctionHouseGui.ItemNode> boxList;
  final private PlaceHolderNode placeHolder = new PlaceHolderNode();
  private final double WIDTH = 500;
  private final double HEIGHT = 500;


  private class ItemNode extends BorderPane
  {
    final private Item item;
    final private Integer highestBid;

    ItemNode(final Item item, Integer highestBid)
    {
      this.item = item;
      this.highestBid = highestBid;
    }

    void init()
    {
      setLeft(new Text(item.getItem()));
      setRight(new Text(highestBid.toString()));
    }

    Integer getHighestBid()
    {
      return highestBid;
    }

    void updateBid()
    {
      setRight(new Text(Integer.toString(item.getHighestBid())));
    }
  }

  private class PlaceHolderNode extends ItemNode
  {
    private PlaceHolderNode()
    {
      super(null, 0);
      setLeft(new Text("No auctionHouse active."));
    }
  @Override
  void updateBid(){}
  }


  AuctionHouseGui(final AuctionHouse auctionHouse)
  {
    boxList = FXCollections.observableList(new LinkedList<>());
    boxList.add(placeHolder);
    final ListView<ItemNode> boxView = new ListView<>(boxList);

    setScene(new Scene(new StackPane(boxView)));
    final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    setWidth(d.getWidth()/2);
    setHeight(d.getHeight()/2);
    setTitle("Auction House");
    show();

    final Timeline timeline = new Timeline();
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1/60d), e -> refreshItems()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.playFromStart();
  }


  void addItem(final Item item)
  {
    if(boxList.contains(placeHolder)) boxList.remove(placeHolder);
    int highestBid;
    if(item.getHighestBid() == item.getPrice()) highestBid = item.getPrice();
    else highestBid = item.getHighestBid();
    final ItemNode box = new ItemNode(item, highestBid);
    box.init();
    boxList.add(box);
  }


  private void refreshItems()
  {
    boxList.forEach(b -> b.updateBid());
  }
}