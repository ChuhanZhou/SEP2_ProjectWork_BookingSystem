package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.OrderList;
import model.domain.order.OrderConfirm;
import model.domain.order.OrderInterface;
import model.domain.room.TimeRangeList;
import utility.doubleClick.Mouse;
import utility.doubleClick.VirtualMouse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class HotelViewModel implements PropertyChangeListener
{
  private BookingModel model;
  private StringProperty hotelName;
  private StringProperty address;
  private StringProperty description;
  private StringProperty characteristics;
  private StringProperty facilities;
  private StringProperty checkInTime;
  private StringProperty checkOutTime;
  private StringProperty rules;
  private StringProperty contactNumber;
  private StringProperty settingsValue;
  private StringProperty roomTypeValue;
  private ObservableList<String> searchListView;
  private ObservableList<String> roomType;
  private ObservableList<String> settings;
  private ObjectProperty<LocalDate> checkIn;
  private ObjectProperty<LocalDate> checkOut;
  private VirtualMouse mouse;
  private OrderList searchOrderList;

  public HotelViewModel(BookingModel model)
  {
    mouse = new Mouse();
    searchOrderList = new OrderList();
    this.model = model;
    hotelName = new SimpleStringProperty();
    address = new SimpleStringProperty();
    description = new SimpleStringProperty();
    characteristics = new SimpleStringProperty();
    facilities = new SimpleStringProperty();
    checkInTime = new SimpleStringProperty();
    checkOutTime = new SimpleStringProperty();
    rules = new SimpleStringProperty();
    contactNumber = new SimpleStringProperty();
    settingsValue = new SimpleStringProperty();
    roomTypeValue = new SimpleStringProperty();
    searchListView = FXCollections.observableArrayList();
    roomType = FXCollections.observableArrayList();
    settings = FXCollections.observableArrayList();
    checkIn = new SimpleObjectProperty<>();
    checkOut = new SimpleObjectProperty<>();
    this.model.addListener("hotel",this);
    this.model.addListener("addOrder",this);
    this.model.addListener("updateOrder",this);
    this.model.addListener("cancelOrder",this);
    checkIn.setValue(LocalDate.now());
    checkOut.setValue(LocalDate.now().plusDays(1));
    settings.addAll("Settings","Search","Manage Hotel");
    setInformation();
  }

  public void search()
  {
    Platform.runLater(()->{
      OrderList orderList = model.getOrders(getTimeRangeListFromCheckInAndCheckOut());
      orderList = orderList.getOrdersByStatus(new OrderConfirm());
      if (roomTypeValue.getValue()!=null)
      {
        if (!roomTypeValue.getValue().equals("All Orders"))
        {
          orderList = orderList.getOrdersByRoomInformation(model.getRoomInformationList().getRoomInformationByType(roomTypeValue.getValue()));
        }
      }
      updateSearchList(orderList);
    });
  }

  public TimeRangeList getTimeRangeListFromCheckInAndCheckOut()
  {
    TimeRangeList timeRangeList = new TimeRangeList();
    LocalDate day1 = checkIn.get();
    LocalDate day2 = day1.plusDays(1);
    for (int x=0;x<(checkOut.get().toEpochDay() - checkIn.get().toEpochDay());x++)
    {
      timeRangeList.addTimeRange(day1.getDayOfMonth(),day1.getMonthValue(),day1.getYear(),day2.getDayOfMonth(),day2.getMonthValue(),day2.getYear(),model.getHotel().getCheckInHour(),model.getHotel().getCheckOutHour());
      day1 = day2;
      day2 = day1.plusDays(1);
    }
    return timeRangeList;
  }

  private void updateSearchList(OrderList orderList)
  {
    Platform.runLater(()->{
      searchListView.clear();
      if (orderList!=null)
      {
        searchOrderList = orderList.copy();
        if (searchOrderList!=null&&searchOrderList.getSize()>0)
        {
          for (int x=0;x<searchOrderList.getSize();x++)
          {
            OrderInterface order = searchOrderList.getOrderByIndex(x);
            searchListView.add("[" + order.getOrderNumber() + "] - (Room " + order.getRoom().getRoomNumber() + ") " + order.getBasicInformation().getFirstName() + " " + order.getBasicInformation().getLastName());
          }
        }
        else
        {
          searchListView.add("Don't have any order!");
        }
      }
    });
  }

  public void setInformation()
  {
    Platform.runLater(()->{
      updateHotelInformation();
      settingsValue.setValue("Settings");
      search();
    });
  }

  private void updateHotelInformation()
  {
    hotelName.setValue(model.getHotel().getName());
    address.setValue(model.getHotel().getAddress());
    description.setValue(model.getHotel().getDescription());
    characteristics.setValue(model.getHotel().getCharacteristic());
    facilities.setValue(model.getHotel().getFacilities());
    checkInTime.setValue(model.getHotel().getCheckInHour() + ":00");
    checkOutTime.setValue(model.getHotel().getCheckOutHour() + ":00");
    rules.setValue(model.getHotel().getRules());
    contactNumber.setValue(model.getHotel().getContactNumber());
    roomType.clear();
    roomTypeValue.setValue("All Orders");
    roomType.add("All Orders");
    for (int x=0;x<model.getRoomInformationList().getSize();x++)
    {
      roomType.add(model.getRoomInformationList().getRoomInformationByIndex(x).getType());
    }

  }

  public void clickCheckIn()
  {
    Platform.runLater(()->{
      if (checkIn.get().isAfter(checkOut.get())||checkIn.get().isEqual(checkOut.get()))
      {
        checkOut.setValue(checkIn.get().plusDays(1));
      }
    });
    search();
  }

  public void clickCheckOut()
  {
    Platform.runLater(()->{
      if (checkIn.get().isAfter(checkOut.get())||checkIn.get().isEqual(checkOut.get()))
      {
        checkIn.setValue(checkOut.get().minusDays(1));
      }
    });
    search();
  }

  public boolean clickSearchListView()
  {
    mouse.click();
    return mouse.getClickTime()==2;
  }

  public void clear()
  {
    hotelName.setValue(null);
    address.setValue(null);
    description.setValue(null);
    characteristics.setValue(null);
    facilities.setValue(null);
    checkInTime.setValue(null);
    checkOutTime.setValue(null);
    rules.setValue(null);
    contactNumber.setValue(null);
    searchListView.clear();
    roomType.clear();
    roomTypeValue.setValue("All Orders");
  }

  public OrderInterface getOrderByIndex(int index)
  {
    return searchOrderList.getOrderByIndex(index);
  }

  public StringProperty rulesProperty()
  {
    return rules;
  }

  public StringProperty hotelNameProperty()
  {
    return hotelName;
  }

  public StringProperty facilitiesProperty()
  {
    return facilities;
  }

  public StringProperty descriptionProperty()
  {
    return description;
  }

  public StringProperty checkOutTimeProperty()
  {
    return checkOutTime;
  }

  public StringProperty checkInTimeProperty()
  {
    return checkInTime;
  }

  public StringProperty contactNumberProperty()
  {
    return contactNumber;
  }

  public StringProperty characteristicsProperty()
  {
    return characteristics;
  }

  public StringProperty addressProperty()
  {
    return address;
  }

  public StringProperty roomTypeValueProperty()
  {
    return roomTypeValue;
  }

  public StringProperty settingsValueProperty()
  {
    return settingsValue;
  }

  public ObjectProperty<LocalDate> checkOutProperty()
  {
    return checkOut;
  }

  public ObjectProperty<LocalDate> checkInProperty()
  {
    return checkIn;
  }

  public ObservableList<String> getRoomType()
  {
    return roomType;
  }

  public ObservableList<String> getSearchListView()
  {
    return searchListView;
  }

  public ObservableList<String> getSettings()
  {
    return settings;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "hotel":
          updateHotelInformation();
          break;
        case "addOrder":
        case "updateOrder":
        case "cancelOrder":
          search();
          break;
      }
    });
  }
}
