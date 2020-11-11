package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.OrderList;
import model.domain.order.OrderInterface;

public class EndViewModel
{
  private BookingModel model;
  private IntegerProperty orderNum;
  private IntegerProperty roomNum;
  private StringProperty name;
  private StringProperty type;
  private StringProperty checkInDate;
  private StringProperty checkOutDate;
  private StringProperty email;
  private StringProperty price;
  private ObservableList<String> services;

  public EndViewModel(BookingModel model)
  {
    this.model = model;
    orderNum = new SimpleIntegerProperty();
    roomNum = new SimpleIntegerProperty();
    name = new SimpleStringProperty();
    type = new SimpleStringProperty();
    checkInDate = new SimpleStringProperty();
    checkOutDate = new SimpleStringProperty();
    email = new SimpleStringProperty();
    price = new SimpleStringProperty();
    services = FXCollections.observableArrayList();
  }

  public void setInformation(OrderInterface order)
  {
    Platform.runLater(()->{
      OrderInterface localOrder = null;
      while (localOrder==null)
      {
        OrderList orderList = model.getOrders(order.getRoomInformation());
        localOrder = orderList.getOrderByIndex(orderList.getSize()-1);
      }
      orderNum.set(localOrder.getOrderNumber());
      roomNum.set(localOrder.getRoom().getRoomNumber());
      name.set(localOrder.getBasicInformation().getFirstName() + " " + localOrder.getBasicInformation().getLastName());
      type.set(localOrder.getRoomInformation().getType());
      checkInDate.set(localOrder.getTimeRangeList().getStartTime().toString());
      checkOutDate.set(localOrder.getTimeRangeList().getEndTime().toString());
      email.set(localOrder.getBasicInformation().getEmail());
      price.set(localOrder.getTotalPrice() + " DKK");
      services.clear();
      for (int x=0;x<localOrder.getUserOptionServiceList().getSize();x++)
      {
        services.add(localOrder.getUserOptionServiceList().getOptionServiceByIndex(x).toString());
      }
    });
  }

  public void clear()
  {
    orderNum.set(0);
    roomNum.set(0);
    name.set(null);
    type.set(null);
    checkInDate.set(null);
    checkOutDate.set(null);
    email.set(null);
    price.set(null);
    services.clear();
  }

  public StringProperty nameProperty()
  {
    return name;
  }

  public StringProperty typeProperty()
  {
    return type;
  }

  public StringProperty emailProperty()
  {
    return email;
  }

  public StringProperty checkInDateProperty()
  {
    return checkInDate;
  }

  public StringProperty checkOutDateProperty()
  {
    return checkOutDate;
  }

  public IntegerProperty orderNumProperty()
  {
    return orderNum;
  }

  public IntegerProperty roomNumProperty()
  {
    return roomNum;
  }

  public StringProperty priceProperty()
  {
    return price;
  }

  public ObservableList<String> getServices()
  {
    return services;
  }
}
