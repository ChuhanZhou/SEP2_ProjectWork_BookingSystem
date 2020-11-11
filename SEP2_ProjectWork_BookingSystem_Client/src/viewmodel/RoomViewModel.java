package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.hotel.HotelInterface;
import model.domain.hotel.OptionServiceList;
import model.domain.hotel.RoomInformation;
import model.domain.order.Order;
import model.domain.order.OrderInterface;
import model.domain.room.TimeRangeList;
import utility.NamedPropertyChangeSubject;
import utility.doubleClick.Mouse;
import utility.doubleClick.VirtualMouse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RoomViewModel implements PropertyChangeListener,
    NamedPropertyChangeSubject
{
  private BookingModel model;
  private IntegerProperty sizeLabel;
  private StringProperty serviceTypeLabel;
  private StringProperty facilitiesLabel;
  private StringProperty errorLabel;
  private ObservableList<String> freeServiceListView;
  private ObservableList<String> allServiceListView;
  private ObservableList<String> choiceListView;
  private RoomInformation roomInformation;
  private OptionServiceList optionServiceList;
  private OptionServiceList choiceOptionServiceList;
  private VirtualMouse optionServiceListVirtualMouse;
  private VirtualMouse choiceListVirtualMouse;
  private OrderInterface order;
  private TimeRangeList timeRangeList;
  private PropertyChangeSupport property;

  public RoomViewModel(BookingModel model)
  {
    order = null;
    optionServiceListVirtualMouse = new Mouse();
    choiceListVirtualMouse = new Mouse();
    this.model = model;
    sizeLabel = new SimpleIntegerProperty();
    serviceTypeLabel = new SimpleStringProperty();
    facilitiesLabel = new SimpleStringProperty();
    freeServiceListView = FXCollections.observableArrayList();
    allServiceListView = FXCollections.observableArrayList();
    choiceListView = FXCollections.observableArrayList();
    optionServiceList = this.model.getOptionServiceList().copy();
    choiceOptionServiceList = new OptionServiceList();
    errorLabel = new SimpleStringProperty();
    this.model.addListener("hotel",this);
    this.model.addListener("error",this);
    property = new PropertyChangeSupport(this);
    clear();
  }

  public void setInformation(TimeRangeList timeRangeList,RoomInformation roomInformation)
  {
    clear();
    OptionServiceList newOptionServiceList = new OptionServiceList();
    OptionServiceList newChoiceServiceList = new OptionServiceList();
    this.timeRangeList = timeRangeList.copy();
    this.roomInformation = roomInformation.copy();
    if (roomInformation!=null)
    {
      sizeLabel.set(this.roomInformation.getSize());
      serviceTypeLabel.set(this.roomInformation.getType());
      facilitiesLabel.set(this.roomInformation.getFacilities());
      for (int x=0;x<this.roomInformation.getSupplyForFree().getSize();x++)
      {
        freeServiceListView.add(this.roomInformation.getSupplyForFree().getOptionServiceByIndex(x).getServiceType());
      }
      for (int x=0;x<choiceOptionServiceList.getSize();x++)
      {
        if (this.roomInformation.getSupplyForFree().getOptionServiceByServiceType(choiceOptionServiceList.getOptionServiceByIndex(x).getServiceType())==null)
        {
          newChoiceServiceList.addOptionService(choiceOptionServiceList.getOptionServiceByIndex(x));
        }
      }
      for (int x=0;x<optionServiceList.getSize();x++)
      {
        if (this.roomInformation.getSupplyForFree().getOptionServiceByServiceType(optionServiceList.getOptionServiceByIndex(x).getServiceType())==null)
        {
          if (newChoiceServiceList.getOptionServiceByServiceType(optionServiceList.getOptionServiceByIndex(x).getServiceType())==null)
          {
            newOptionServiceList.addOptionService(optionServiceList.getOptionServiceByIndex(x));
          }
        }
      }
    }
    choiceOptionServiceList = newChoiceServiceList;
    optionServiceList = newOptionServiceList;
    updateChoiceServices();
    order = new Order(this.roomInformation,model.getUser().getBasicInformation(),choiceOptionServiceList,this.timeRangeList);
  }

  public void clear()
  {
    sizeLabel.set(0);
    serviceTypeLabel.set(null);
    facilitiesLabel.set(null);
    freeServiceListView.clear();
    allServiceListView.clear();
    choiceListView.clear();
    optionServiceList = this.model.getOptionServiceList().copy();
  }

  public void setOrder(OrderInterface order)
  {
    this.order = order;
    choiceOptionServiceList = this.order.getUserOptionServiceList();
    setInformation(this.order.getTimeRangeList(),this.order.getRoomInformation());
  }

  public OrderInterface getOrder()
  {
    return order;
  }

  public StringProperty serviceTypeLabelProperty()
  {
    return serviceTypeLabel;
  }

  public StringProperty facilitiesLabelProperty()
  {
    return facilitiesLabel;
  }

  public IntegerProperty sizeLabelProperty()
  {
    return sizeLabel;
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  public ObservableList<String> getFreeServiceListView()
  {
    return freeServiceListView;
  }

  public ObservableList<String> getAllServiceListView()
  {
    return allServiceListView;
  }

  public ObservableList<String> getChoiceListView()
  {
    return choiceListView;
  }

  private void updateChoiceServices()
  {
    choiceListView.clear();
    for (int x=0;x<choiceOptionServiceList.getSize();x++)
    {
      choiceListView.add(choiceOptionServiceList.getOptionServiceByIndex(x).toString());
    }
    allServiceListView.clear();
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      allServiceListView.add(optionServiceList.getOptionServiceByIndex(x).toString());
    }
  }

  public void clickAllServiceListView(int index)
  {
    optionServiceListVirtualMouse.click();
    if (optionServiceListVirtualMouse.getClickTime()==2)
    {
      choiceOptionServiceList.addOptionService(optionServiceList.getOptionServiceByIndex(index));
      optionServiceList.removeOptionServiceByIndex(index);
      updateChoiceServices();
    }
  }

  public void clickChoiceListView(int index)
  {
    choiceListVirtualMouse.click();
    if (choiceListVirtualMouse.getClickTime()==2)
    {
      optionServiceList.addOptionService(choiceOptionServiceList.getOptionServiceByIndex(index));
      choiceOptionServiceList.removeOptionServiceByIndex(index);
      updateChoiceServices();
    }
  }

  public boolean clickReservebtn()
  {
    order = new Order(roomInformation,model.getUser().getBasicInformation().copy(),choiceOptionServiceList,timeRangeList);
    if (!model.getUser().getBasicInformation().hasNull())
    {
      model.sendOrderPackage(order);
    }
    return model.getUser().getBasicInformation().hasNull();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "hotel":
          HotelInterface hotel = (HotelInterface) evt.getNewValue();
          if (roomInformation!=null)
          {
            setInformation(timeRangeList,hotel.getRoomInformationList().getRoomInformationByType(roomInformation.getType()));
          }
          break;
        case "error":
          errorLabel.set(((LocalError) evt.getNewValue()).getError());
          property.firePropertyChange("error",-1,errorLabel.getValue());
          break;
      }
    });
  }

  @Override public void addListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.addPropertyChangeListener(propertyName, listener);
  }

  @Override public void removeListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(propertyName,listener);
  }
}
