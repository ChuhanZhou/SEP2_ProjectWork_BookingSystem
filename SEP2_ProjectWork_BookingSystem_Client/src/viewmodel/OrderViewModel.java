package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.OrderList;
import model.domain.order.OrderInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OrderViewModel implements PropertyChangeListener
{
  private BookingModel bookingModel;
  private OrderInterface order;
  private IntegerProperty numLabel;
  private StringProperty nameLabel;
  private StringProperty typeLabel;
  private IntegerProperty rnumLabel;
  private StringProperty inLabel;
  private StringProperty outLabel;
  private StringProperty priceLabel;
  private StringProperty errorLabel;
  private StringProperty emailLabel;
  private ObservableList<String> servicesList;

  public OrderViewModel(BookingModel bookingModel)
  {
    this.bookingModel = bookingModel;
    numLabel = new SimpleIntegerProperty();
    nameLabel = new SimpleStringProperty();
    typeLabel = new SimpleStringProperty();
    rnumLabel = new SimpleIntegerProperty();
    inLabel = new SimpleStringProperty();
    outLabel = new SimpleStringProperty();
    priceLabel = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    servicesList = FXCollections.observableArrayList();
    emailLabel = new SimpleStringProperty();
    this.bookingModel.addListener("error",this);
    this.bookingModel.addListener("updateOrder",this);
    this.bookingModel.addListener("cancelOrder",this);
    this.bookingModel.addListener("cancelOrders",this);
  }

  public void setInformation(OrderInterface order)
  {
    Platform.runLater(()->{
      this.order = bookingModel.getOrder(order.getOrderNumber());
      numLabel.set(this.order.getRoom().getRoomNumber());
      nameLabel.set(this.order.getBasicInformation().getFirstName() + " " + this.order.getBasicInformation().getLastName());
      typeLabel.set(this.order.getRoomInformation().getType());
      emailLabel.set(this.order.getBasicInformation().getEmail());
      servicesList.clear();
      if (this.order.getRoomInformation().getSupplyForFree().getSize() + this.order.getUserOptionServiceList().getSize()==0)
      {
        servicesList.add("No service!");
      }
      for (int x=0;x<this.order.getRoomInformation().getSupplyForFree().getSize();x++)
      {
        if (x==0)
        {
          servicesList.add("Free service:");
        }
        servicesList.add("[" + this.order.getRoomInformation().getSupplyForFree().getOptionServiceByIndex(x).getServiceType() + "]");
      }
      for (int x=0;x<this.order.getUserOptionServiceList().getSize();x++)
      {
        if (x==0)
        {
          servicesList.add("Additional service:");
        }
        servicesList.add(this.order.getUserOptionServiceList().getOptionServiceByIndex(x).toString());
      }
      rnumLabel.set(this.order.getRoom().getRoomNumber());
      inLabel.set(this.order.getTimeRangeList().getStartTime().toString());
      outLabel.set(this.order.getTimeRangeList().getEndTime().toString());
      priceLabel.set(this.order.getTotalPrice() + " DKK");
      if (this.order.getOrderState().getState().equals("Cancel"))
      {
        errorLabel.set("Cancel");
      }
    });
  }

  public void clear()
  {
    numLabel.set(0);
    nameLabel.set(null);
    typeLabel.set(null);
    servicesList.clear();
    rnumLabel.set(0);
    inLabel.set(null);
    outLabel.set(null);
    priceLabel.set(null);
    errorLabel.set(null);
    errorLabel.set(null);
  }

  public OrderInterface getOrder()
  {
    return order;
  }

  public StringProperty getErrorLabelProperty()
  {
    return errorLabel;
  }

  public StringProperty getInLabelProperty()
  {
    return inLabel;
  }

  public StringProperty getEmailLabelProperty()
  {
    return emailLabel;
  }

  public StringProperty getNameLabelProperty()
  {
    return nameLabel;
  }

  public IntegerProperty getNumLabelProperty()
  {
    return numLabel;
  }

  public StringProperty getOutLabelProperty()
  {
    return outLabel;
  }

  public IntegerProperty getRnumLabelProperty()
  {
    return rnumLabel;
  }

  public StringProperty getPriceLabelProperty()
  {
    return priceLabel;
  }

  public ObservableList<String> getServicesList()
  {
    return servicesList;
  }

  public StringProperty getTypeLabelProperty()
  {
    return typeLabel;
  }

  public boolean clickCancelBtn()
  {
    if (order.getOrderState().getState().equals("Cancel"))
    {
      errorLabel.set("This order has been canceled!");
    }
    return !order.getOrderState().getState().equals("Cancel");
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        errorLabel.set(((LocalError) evt.getNewValue()).getError());
        break;
      case "updateOrder":
      case "cancelOrder":
        OrderInterface updateOrder = (OrderInterface) evt.getNewValue();
        if (order!=null)
        {
          if (updateOrder.getOrderNumber()==order.getOrderNumber())
          {
            setInformation(updateOrder);
          }
        }
        break;
      case "cancelOrders":
        OrderList orderList = (OrderList) evt.getNewValue();
        OrderInterface order = orderList.getOrderByOrderNumber(this.order.getOrderNumber());
        if (order!=null)
        {
          setInformation(order);
        }
        break;
    }
  }
}
