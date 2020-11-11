package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.OrderList;
import model.domain.RoomList;
import model.domain.UserList;
import model.domain.hotel.OptionService;
import model.domain.hotel.OptionServiceList;
import model.domain.hotel.RoomInformation;
import model.domain.hotel.RoomInformationList;
import model.domain.order.OrderConfirm;
import model.domain.order.OrderInterface;
import model.domain.room.Room;
import model.domain.room.RoomInterface;
import utility.doubleClick.Mouse;
import utility.doubleClick.VirtualMouse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SearchViewModel implements PropertyChangeListener
{
  private BookingModel model;
  private StringProperty searchField;
  private StringProperty errorLabel;
  private StringProperty addButtonValue;
  private StringProperty deleteButtonValue;
  private BooleanProperty addButtonStyle;
  private BooleanProperty deleteButtonStyle;
  private StringProperty keyWordBoxValue;
  private StringProperty typeBoxValue;
  private ObservableList<String> keyWordBox;
  private ObservableList<String> typeBox;
  private ObservableList<String> listView;
  private UserList searchUserList;
  private OrderList searchOrderList;
  private RoomList searchRoomList;
  private RoomInformationList searchRoomInformationList;
  private OptionServiceList searchOptionServiceList;
  private VirtualMouse mouse;
  private VirtualMouse typeBoxMouse;

  public SearchViewModel(BookingModel model)
  {
    this.model = model;
    mouse = new Mouse();
    typeBoxMouse = new Mouse();
    searchField = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    addButtonValue = new SimpleStringProperty();
    addButtonStyle = new SimpleBooleanProperty();
    deleteButtonValue = new SimpleStringProperty();
    deleteButtonStyle = new SimpleBooleanProperty();
    keyWordBoxValue = new SimpleStringProperty();
    typeBoxValue = new SimpleStringProperty();
    keyWordBox = FXCollections.observableArrayList();
    typeBox = FXCollections.observableArrayList();
    listView = FXCollections.observableArrayList();
    initAllSearchList();
    this.model.addListener("error",this);
    this.model.addListener("user",this);
    this.model.addListener("newRoom",this);
    this.model.addListener("updateRoom",this);
    this.model.addListener("removeRoom",this);
    this.model.addListener("addOrder",this);
    this.model.addListener("updateOrder",this);
    this.model.addListener("cancelOrder",this);
    this.model.addListener("cancelOrders",this);
    this.model.addListener("hotel",this);
  }

  public void clickTypeBox()
  {
    setKeyWordBox(typeBoxValue.getValue());
    updateAddAndDeleteButtonStatus();
    search();
  }

  public void clickSearchButton()
  {
    search();
  }

  public String clickAddButton()
  {
    switch (typeBoxValue.getValue())
    {
      case "Room":
        return "RoomView";
      case "Room Type":
        return "RoomTypeUpdateView";
      case "Service":
        return "ServiceView";
    }
    return null;
  }

  public void clickDeleteButton(int index)
  {
    switch (typeBoxValue.getValue())
    {
      case "Order":
        if (searchOrderList.getOrderByIndex(index).getOrderState().getState()!="Cancel")
        {
          model.cancelOrder(searchOrderList.getOrderByIndex(index).getOrderNumber());
        }
        else
        {
          errorLabel.setValue("This order has been canceled!");
        }
        break;
      case "Room":
        int usedOrderNumber = model.getOrders(searchRoomList.getRoomByIndex(index)).getOrdersByStatus(new OrderConfirm()).getSize();
        if (usedOrderNumber==1)
        {
          errorLabel.setValue("There is an confirmed order has this room!");
        }
        else if (usedOrderNumber>1)
        {
          errorLabel.setValue("There are " + usedOrderNumber + " confirmed orders have this room!");
        }
        else
        {
          model.removeRoom(searchRoomList.getRoomByIndex(index));
        }
        break;
      case "Room Type":
        int usedRoomNumber = model.getRoomsByType(searchRoomInformationList.getRoomInformationByIndex(index).getType()).getSize();
        if (usedRoomNumber==1)
        {
          errorLabel.setValue("There is a room uses this room type!");
        }
        else if (usedRoomNumber>1)
        {
          errorLabel.setValue("There are " + usedRoomNumber + " rooms use this room type!");
        }
        else
        {
          model.removeRoomInformation(searchRoomInformationList.getRoomInformationByIndex(index));
        }
        break;
      case "Service":
        model.removeOptionService(searchOptionServiceList.getOptionServiceByIndex(index));
        break;
    }
  }

  public String clickListView(int index)
  {
    mouse.click();
    if (mouse.getClickTime()==2)
    {
      switch (typeBoxValue.getValue())
      {
        case "Order":
          if (searchOrderList.getOrderByIndex(index)!=null)
          {
            return "OrderView";
          }
          else
          {
            break;
          }
        case "Room":
          if (searchRoomList.getRoomByIndex(index)!=null)
          {
            return "RoomView";
          }
          else
          {
            break;
          }
        case "Room Type":
          if (searchRoomInformationList.getRoomInformationByIndex(index)!=null)
          {
            return "RoomTypeView";
          }
          else
          {
            break;
          }
        case "Service":
          if (searchOptionServiceList.getOptionServiceByIndex(index)!=null)
          {
            return "ServiceView";
          }
          else
          {
            break;
          }
      }
    }
    return null;
  }

  private void updateAddAndDeleteButtonStatus()
  {
    if (typeBoxValue.getValue()!=null)
    {
      switch (typeBoxValue.getValue())
      {
        case "User":
          addButtonValue.setValue("Add");
          addButtonStyle.setValue(true);
          deleteButtonValue.setValue("Delete");
          deleteButtonStyle.setValue(true);
          break;
        case "Order":
          addButtonValue.setValue("Add");
          addButtonStyle.setValue(true);
          deleteButtonValue.setValue("Cancel");
          deleteButtonStyle.setValue(false);
          break;
        case "Room":
          if (model.getRoomInformationList().getSize()==0)
          {
            addButtonValue.setValue("Add");
            addButtonStyle.setValue(true);
            deleteButtonValue.setValue("Delete");
            deleteButtonStyle.setValue(true);
          }
          else
          {
            addButtonValue.setValue("Add");
            addButtonStyle.setValue(false);
            deleteButtonValue.setValue("Delete");
            deleteButtonStyle.setValue(false);
          }
          break;
        case "Room Type":
        case "Service":
          addButtonValue.setValue("Add");
          addButtonStyle.setValue(false);
          deleteButtonValue.setValue("Delete");
          deleteButtonStyle.setValue(false);
          break;
      }
    }
  }

  private void initAllSearchList()
  {
    searchUserList = new UserList();
    searchOrderList = new OrderList();
    searchRoomList = new RoomList();
    searchRoomInformationList = new RoomInformationList();
    searchOptionServiceList = new OptionServiceList();
  }

  private void search()
  {
    initAllSearchList();
    if (typeBoxValue.getValue()!=null)
    {
      switch (typeBoxValue.getValue())
      {
        case "User":
          searchUser();
          break;
        case "Order":
          searchOrder();
          break;
        case "Room":
          searchRoom();
          break;
        case "Room Type":
          searchRoomType();
          break;
        case "Service":
          searchService();
          break;
      }
    }
  }

  private void searchUser()
  {
    Platform.runLater(()->{
      switch (keyWordBoxValue.getValue())
      {
        case "All":
          searchUserList = model.getUserList();
          errorLabel.setValue(null);
          break;
        case "Email":
          if (searchField.getValue()!=null)
          {
            searchUserList.addNewUser(model.getUser(searchField.getValue()));
            errorLabel.setValue(null);
          }
          else
          {
            errorLabel.setValue("Please write an email!");
          }
          break;
        case "Order Number":
          if (searchField.getValue()!=null)
          {
            try
            {
              searchUserList.addNewUser(model.getUser(model.getOrder(Integer.parseInt(searchField.getValue())).getBasicInformation().getEmail()));
              errorLabel.setValue(null);
            }
            catch (NumberFormatException e)
            {
              errorLabel.setValue("Order number is made by integer!");
            }
          }
          else
          {
            errorLabel.setValue("Please write an order number!");
          }
          break;
      }
      listView.clear();
      if (searchUserList.getSize()==0)
      {
        listView.add("Didn't find any user!");
      }
      for (int x=0;x<searchUserList.getSize();x++)
      {
        listView.add(searchUserList.getUserByIndex(x).getBasicInformation().toString());
      }
    });
  }

  private void searchOrder()
  {
    Platform.runLater(()->{
      switch (keyWordBoxValue.getValue())
      {
        case "All":
          searchOrderList = model.getOrders();
          errorLabel.setValue(null);
          break;
        case "Order Number":
          if (searchField.getValue()!=null)
          {
            try
            {
              searchOrderList.addNewOrder(model.getOrder(Integer.parseInt(searchField.getValue())));
              errorLabel.setValue(null);
            }
            catch (NumberFormatException e)
            {
              errorLabel.setValue("Order number is made by integer!");
            }
          }
          else
          {
            errorLabel.setValue("Please write an order number!");
          }
          break;
        case "Room Number":
          if (searchField.getValue()!=null)
          {
            try
            {
              searchOrderList = model.getOrders(new Room(Integer.parseInt(searchField.getValue()),null));
              errorLabel.setValue(null);
            }
            catch (NumberFormatException e)
            {
              errorLabel.setValue("Room number is made by integer!");
            }
          }
          else
          {
            errorLabel.setValue("Please write a room number!");
          }
          break;
        case "Room Type Name":
          if (searchField.getValue()!=null)
          {
            searchOrderList = model.getOrders(model.getRoomInformationList().getRoomInformationByType(searchField.getValue()));
            errorLabel.setValue(null);
          }
          else
          {
            errorLabel.setValue("Please write a room type name!");
          }
          break;
        case "Email":
          if (searchField.getValue()!=null)
          {
            searchOrderList = model.getOrders(model.getUser(searchField.getValue()));
            errorLabel.setValue(null);
          }
          else
          {
            errorLabel.setValue("Please write an email!");
          }
          break;
      }
      listView.clear();
      if (searchOrderList.getSize()==0)
      {
        listView.add("Didn't find any order!");
      }
      for (int x=0;x<searchOrderList.getSize();x++)
      {
        listView.add(searchOrderList.getOrderByIndex(x).toString());
      }
    });
  }

  private void searchRoom()
  {
    Platform.runLater(()->{
      switch (keyWordBoxValue.getValue())
      {
        case "All":
          searchRoomList = model.getRooms();
          errorLabel.setValue(null);
          break;
        case "Room Number":
          if (searchField.getValue()!=null)
          {
            try
            {
              searchRoomList.addNewRoom(model.getRoomByRoomNumber(Integer.parseInt(searchField.getValue())));
              errorLabel.setValue(null);
            }
            catch (NumberFormatException e)
            {
              errorLabel.setValue("Room number is made by integer!");
            }
          }
          else
          {
            errorLabel.setValue("Please write a room number!");
          }
          break;
        case "Room Type Name":
          if (searchField.getValue()!=null)
          {
            searchRoomList = model.getRoomsByType(searchField.getValue());
            errorLabel.setValue(null);
          }
          else
          {
            errorLabel.setValue("Please write a room type name!");
          }
          break;
      }
      listView.clear();
      if (searchRoomList.getSize()==0)
      {
        listView.add("Didn't find any room!");
      }
      for (int x=0;x<searchRoomList.getSize();x++)
      {
        listView.add(searchRoomList.getRoomByIndex(x).toString());
      }
    });
  }

  private void searchRoomType()
  {
    Platform.runLater(()->{
      switch (keyWordBoxValue.getValue())
      {
        case "All":
          searchRoomInformationList = model.getRoomInformationList();
          errorLabel.setValue(null);
          break;
        case "Room Type Name":
          if (searchField.getValue()!=null)
          {
            searchRoomInformationList.addNewRoomInformation(model.getRoomInformationList().getRoomInformationByType(searchField.getValue()));
            errorLabel.setValue(null);
          }
          else
          {
            errorLabel.setValue("Please write a room type name!");
          }
          break;
      }
      listView.clear();
      if (searchRoomInformationList.getSize()==0)
      {
        listView.add("Didn't find any room type!");
      }
      for (int x=0;x<searchRoomInformationList.getSize();x++)
      {
        listView.add(searchRoomInformationList.getRoomInformationByIndex(x).toString());
      }
    });
  }

  private void searchService()
  {
    Platform.runLater(()->{
      switch (keyWordBoxValue.getValue())
      {
        case "All":
          searchOptionServiceList = model.getOptionServiceList();
          errorLabel.setValue(null);
          break;
        case "Service Name":
          if (searchField.getValue()!=null)
          {
            System.out.println(searchField.getValue());
            searchOptionServiceList.addOptionService(model.getOptionServiceByServiceType(searchField.getValue()));
            errorLabel.setValue(null);
          }
          else
          {
            errorLabel.setValue("Please write a room type name!");
          }
          break;
      }
      listView.clear();
      if (searchOptionServiceList.getSize()==0)
      {
        listView.add("Didn't find any room service!");
      }
      for (int x=0;x<searchOptionServiceList.getSize();x++)
      {
        listView.add(searchOptionServiceList.getOptionServiceByIndex(x).toString());
      }
    });
  }

  public void setInformation()
  {
    setInformation("User");
  }

  public void setInformation(String type)
  {
    Platform.runLater(()->{
      if (type!=null)
      {
        typeBox.clear();
        typeBox.addAll("User", "Order", "Room", "Room Type", "Service");
        typeBoxValue.setValue(type);
        setKeyWordBox(type);
      }
    });
  }

  public void setKeyWordBox(String type)
  {
    if (type!=null)
    {
      switch (type)
      {
        case "User":
          keyWordBox.clear();
          keyWordBox.addAll("All","Email","Order Number");
          break;
        case "Order":
          keyWordBox.clear();
          keyWordBox.addAll("All","Order Number","Room Number","Room Type Name","Email");
          break;
        case "Room":
          keyWordBox.clear();
          keyWordBox.addAll("All","Room Number","Room Type Name");
          break;
        case "Room Type":
          keyWordBox.clear();
          keyWordBox.addAll("All","Room Type Name");
          break;
        case "Service":
          keyWordBox.clear();
          keyWordBox.addAll("All","Service Name");
          break;
      }
      keyWordBoxValue.setValue("All");
    }
  }

  public void clear()
  {
    Platform.runLater(()->{
      searchField.setValue(null);
      errorLabel.setValue(null);
      addButtonValue.setValue(null);
      addButtonStyle.setValue(null);
      deleteButtonValue.setValue(null);
      deleteButtonStyle.setValue(null);
      keyWordBoxValue.setValue(null);
      typeBoxValue.setValue(null);
      keyWordBox.clear();
      typeBox.clear();
      listView.clear();
    });
  }

  public OrderInterface getSearchOrderByIndex(int index)
  {
    return searchOrderList.getOrderByIndex(index);
  }

  public RoomInterface getSearchRoomByIndex(int index)
  {
    return searchRoomList.getRoomByIndex(index);
  }

  public RoomInformation getSearchRoomInformationByIndex(int index)
  {
    return searchRoomInformationList.getRoomInformationByIndex(index);
  }

  public OptionService getSearchOptionServiceByIndex(int index)
  {
    return searchOptionServiceList.getOptionServiceByIndex(index);
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  public BooleanProperty addButtonStyleProperty()
  {
    return addButtonStyle;
  }

  public StringProperty addButtonValueProperty()
  {
    return addButtonValue;
  }

  public BooleanProperty deleteButtonStyleProperty()
  {
    return deleteButtonStyle;
  }

  public StringProperty deleteButtonValueProperty()
  {
    return deleteButtonValue;
  }

  public StringProperty keyWordBoxValueProperty()
  {
    return keyWordBoxValue;
  }

  public StringProperty searchFieldProperty()
  {
    return searchField;
  }

  public StringProperty typeBoxValueProperty()
  {
    return typeBoxValue;
  }

  public ObservableList<String> getListView()
  {
    return listView;
  }

  public ObservableList<String> getKeyWordBox()
  {
    return keyWordBox;
  }

  public ObservableList<String> getTypeBox()
  {
    return typeBox;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "error":
          errorLabel.setValue(((LocalError) evt.getNewValue()).getError());
          break;

        case "user":
          if (typeBoxValue.getValue()!=null)
          {
            if (typeBoxValue.getValue().equals("User"))
            {
              searchUser();
            }
          }
          break;
        case "newRoom":
        case "updateRoom":
        case "removeRoom":
          if (typeBoxValue.getValue()!=null)
          {
            if (typeBoxValue.getValue().equals("Room"))
            {
              searchRoom();
            }
          }
          break;
        case "addOrder":
        case "updateOrder":
        case "cancelOrder":
        case "cancelOrders":
          if (typeBoxValue.getValue()!=null)
          {
            if (typeBoxValue.getValue().equals("Order"))
            {
              searchOrder();
            }
          }
          break;
        case "hotel":
          if (typeBoxValue.getValue()!=null)
          {
            if (typeBoxValue.getValue().equals("Room Type"))
            {
              searchRoomType();
            }
            else if (typeBoxValue.getValue().equals("Service"))
            {
              searchService();
            }
          }
          break;
      }
    });
  }
}
