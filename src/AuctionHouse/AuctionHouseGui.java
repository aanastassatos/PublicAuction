/*package AuctionHouse;

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
import java.util.Collection;
import java.util.Iterator;
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
    final private int highestBid;

    ItemNode(final Item item, int highestBid)
    {
      this.item = item;
      this.highestBid = highestBid;
    }

    //*************************************************************************************
    //Each parameter's type and name: none
    //Method's return value : void
    //Description of what the method does.
    // - initialize the item and bid
    // ************************************************************************************
    void init()
    {
      //setCenter(new Text(Integer.toString(item.getTime())));
      setLeft(new Text(item.getItem()));
      setRight(new Text(Integer.toString(highestBid)));
    }

    //*************************************************************************************
    //Each parameter's type and name:
    //Method's return value : void
    //Description of what the method does.
    // - Returns highest bid
    // ************************************************************************************
    int getHighestBid()
    {
      return highestBid;
    }

    //*************************************************************************************
    //Each parameter's type and name: final BidPlacedMessage message
    //Method's return value : void
    //Description of what the method does.
    // - Update the bid
    // ************************************************************************************
    void updateBid()
    {
      setRight(new Text(Integer.toString(item.getHighestBid())));
    }

    void updateTime()
    {
      setCenter(new Text(Integer.toString(item.getTime())));
    }

    //HOW TO UPDATE ITEM
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

  //*************************************************************************************
  //Each parameter's type and name: final Item item
  //Method's return value : void
  //Description of what the method does.
  // - Show items
  // ************************************************************************************
  void addItem(final Collection<Item> items)
  {
    Iterator<Item> iter = items.iterator();
    while (iter.hasNext())
    {
      Item item = iter.next();
      if (boxList.contains(placeHolder)) boxList.remove(placeHolder);
      int highestBid;
      if (item.getHighestBid() == item.getPrice()) highestBid = item.getPrice();
      else highestBid = item.getHighestBid();
      final ItemNode box = new ItemNode(item, highestBid);
      box.init();
      boxList.add(box);
    }
  }

  //*************************************************************************************
  //Each parameter's type and name: none
  //Method's return value : void
  //Description of what the method does.
  // - refresh to see if there are new bid placed
  // - HAVE TO CHECK NEW ITEMS ADDED AS WELL
  // ************************************************************************************
  private void refreshItems()
  {
    boxList.forEach(b -> b.updateBid());
   // boxList.forEach(b->b.updateTime());
  }
}*/