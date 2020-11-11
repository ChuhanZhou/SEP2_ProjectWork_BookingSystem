package viewmodel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.OrderList;
import model.domain.order.OrderInterface;
import utility.doubleClick.Mouse;
import utility.doubleClick.VirtualMouse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BookingsViewModel implements PropertyChangeListener
{
  private BookingModel model;
  private VirtualMouse mouse;
  private ObservableList<String> listView;
  private OrderList orderList;

  public BookingsViewModel(BookingModel model)
  {
    this.model = model;
    mouse = new Mouse();
    listView = FXCollections.observableArrayList();
    this.model.addListener("addOrder",this);
    this.model.addListener("updateOrder",this);
    this.model.addListener("cancelOrder",this);
  }

  public void setInformation()
  {
    orderList = model.getOrders(model.getUser()).copy();
    Platform.runLater(()->{
      listView.clear();
      for (int x=0;x<orderList.getSize();x++)
      {
        listView.add(orderList.getOrderByIndex(x).toString());
      }
    });
  }

  public void clear()
  {
    listView.clear();
  }

  public boolean clickOrderList()
  {
    mouse.click();
    return mouse.getClickTime()==2;
  }

  public ObservableList<String> getListView()
  {
    return listView;
  }

  public OrderInterface getOrder(int index)
  {
    return orderList.getOrderByIndex(index);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "addOrder":
        case "updateOrder":
        case "cancelOrder":
          setInformation();
          break;
      }
    });
  }
}
