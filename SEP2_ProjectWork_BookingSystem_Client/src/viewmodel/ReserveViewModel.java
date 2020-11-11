package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.hotel.RoomInformationList;
import model.domain.room.TimeRangeList;
import utility.doubleClick.Mouse;
import utility.doubleClick.VirtualMouse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class ReserveViewModel implements PropertyChangeListener
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
  private StringProperty MYValue;
  private StringProperty roomTypeValue;
  private ObservableList<String> searchListView;
  private ObservableList<String> roomType;
  private ObservableList<String> MY;
  private ObjectProperty<LocalDate> checkIn;
  private ObjectProperty<LocalDate> checkOut;
  private RoomInformationList searchInformationList;
  private VirtualMouse mouse;

  public ReserveViewModel(BookingModel model)
  {
    mouse = new Mouse();
    searchInformationList = new RoomInformationList();
    this.model = model;
    MYValue = new SimpleStringProperty();
    roomTypeValue = new SimpleStringProperty();
    hotelName = new SimpleStringProperty();
    address = new SimpleStringProperty();
    description = new SimpleStringProperty();
    characteristics = new SimpleStringProperty();
    facilities = new SimpleStringProperty();
    checkInTime = new SimpleStringProperty();
    checkOutTime = new SimpleStringProperty();
    rules = new SimpleStringProperty();
    contactNumber = new SimpleStringProperty();
    searchListView = FXCollections.observableArrayList();
    roomType = FXCollections.observableArrayList();
    MY = FXCollections.observableArrayList();
    checkIn = new SimpleObjectProperty<>();
    checkOut = new SimpleObjectProperty<>();
    checkIn.setValue(LocalDate.now());
    checkOut.setValue(LocalDate.now().plusDays(1));
    this.model.addListener("search",this);
    this.model.addListener("hotel",this);
  }

  public void setInformation()
  {
    Platform.runLater(()->{
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
      roomType.add("All");
      roomTypeValue.setValue("All");
      for (int x=0;x<model.getHotel().getRoomInformationList().getSize();x++)
      {
        roomType.add(model.getRoomInformationList().getRoomInformationByIndex(x).getType());
      }
      MY.clear();
      MY.addAll(model.getUser().getBasicInformation().getEmail(),"Profile","Bookings");
      MYValue.setValue(model.getUser().getBasicInformation().getEmail());
      search();
    });
  }

  public void init()
  {
    checkIn.setValue(LocalDate.now());
    checkOut.setValue(LocalDate.now().plusDays(1));
  }

  public void search()
  {
    Platform.runLater(()->{
      RoomInformationList roomInformationList = new RoomInformationList();
      if (roomTypeValue.getValue().equals("All")||roomTypeValue.getValue()==null)
      {
        roomInformationList = model.getRoomInformationList();
      }
      else
      {
        roomInformationList.addNewRoomInformation(model.getRoomInformationList().getRoomInformationByType(roomTypeValue.getValue()));
      }
      model.sendSearchPackage(roomInformationList,getTimeRangeListFromCheckInAndCheckOut());
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

  private void updateSearchList(RoomInformationList roomInformationList)
  {
    Platform.runLater(()->{
      searchListView.clear();
      searchInformationList = new RoomInformationList();
      if (roomInformationList!=null)
      {
        searchInformationList = roomInformationList.copy();
        if (searchInformationList!=null&&searchInformationList.getSize()>0)
        {
          for (int x=0;x<searchInformationList.getSize();x++)
          {
            searchListView.add("[" + searchInformationList.getRoomInformationByIndex(x).getType() + "] " + searchInformationList.getRoomInformationByIndex(x).getPrice() + " DKK");
          }
        }
        else
        {
          searchListView.add("No empty room!");
        }
      }
    });
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
    MYValue.setValue(model.getUser().getAccountInformation().getEmail());
    roomTypeValue.setValue("All");
  }

  public void clickCheckIn()
  {
    Platform.runLater(()->{
      if (checkIn.get().isBefore(LocalDate.now()))
      {
        checkIn.setValue(LocalDate.now());
      }
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
      clickCheckIn();
    });
    search();
  }

  public boolean clickSearchListView()
  {
    mouse.click();
    return mouse.getClickTime()==2;
  }

  public StringProperty roomTypeValueProperty()
  {
    return roomTypeValue;
  }

  public RoomInformationList getSearchInformationList()
  {
    return searchInformationList;
  }

  public ObjectProperty<LocalDate> checkInProperty()
  {
    return checkIn;
  }

  public ObjectProperty<LocalDate> checkOutProperty()
  {
    return checkOut;
  }

  public ObservableList<String> getSearchListView()
  {
    return searchListView;
  }

  public ObservableList<String> getRoomType()
  {
    return roomType;
  }

  public ObservableList<String> getMY()
  {
    return MY;
  }

  public StringProperty addressProperty()
  {
    return address;
  }

  public StringProperty characteristicsProperty()
  {
    return characteristics;
  }

  public StringProperty checkInTimeProperty()
  {
    return checkInTime;
  }

  public StringProperty checkOutTimeProperty()
  {
    return checkOutTime;
  }

  public StringProperty contactNumberProperty()
  {
    return contactNumber;
  }

  public StringProperty descriptionProperty()
  {
    return description;
  }

  public StringProperty facilitiesProperty()
  {
    return facilities;
  }

  public StringProperty hotelNameProperty()
  {
    return hotelName;
  }

  public StringProperty rulesProperty()
  {
    return rules;
  }

  public StringProperty MYValueProperty()
  {
    return MYValue;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "search":
          updateSearchList((RoomInformationList) evt.getNewValue());
          break;
        case "hotel":
          setInformation();
          break;
      }
    });
  }
}
