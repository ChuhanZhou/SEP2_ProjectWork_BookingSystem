package model;

import dbs.DAOModel;
import dbs.DAOModelManager;
import model.domain.*;
import model.domain.error.ErrorInterface;
import model.domain.error.HasError;
import model.domain.error.NoError;
import model.domain.hotel.*;
import model.domain.order.OrderInterface;
import model.domain.room.Room;
import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;
import model.domain.room.timeRange.TimeRangeInterface;
import model.domain.user.AccountInformation;
import model.domain.user.User;
import model.domain.user.UserInterface;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BookingModelManager implements BookingModel
{
  private HotelInterface hotel;
  private LocalError error;
  private PropertyChangeSupport property;
  private DomainListReaderWriterInterface domainListReaderWriterInterface;
  private DAOModel daoModel;

  public BookingModelManager()
  {
    error = LocalError.getLocalError();
    property = new PropertyChangeSupport(this);
    domainListReaderWriterInterface = new DomainListReaderWriter(new UserList(),new RoomList(),new OrderList());
    daoModel = new DAOModelManager();
    connectToDBS();
  }

  public BookingModelManager(String url,String user,String password)
  {
    error = LocalError.getLocalError();
    property = new PropertyChangeSupport(this);
    domainListReaderWriterInterface = new DomainListReaderWriter(new UserList(),new RoomList(),new OrderList());
    daoModel = new DAOModelManager(url,user,password);
    connectToDBS();
    daoModel.clear();
  }

  private void connectToDBS()
  {
    if (daoModel.haveData())
    {
      Hotel.setHotel(daoModel.getHotel().copy());
      hotel = Hotel.getHotel();
      hotel.setOptionServiceList(daoModel.getAllOptionService());
      hotel.setRoomInformationList(daoModel.getAllRoomInformation());
      RoomList dbsRoomList = daoModel.getAllRoom();
      for (int x=0;x<dbsRoomList.getSize();x++)
      {
        domainListReaderWriterInterface.acquireWriteRoomList().addNewRoom(dbsRoomList.getRoomByIndex(x));
        domainListReaderWriterInterface.releaseWrite();
      }
      UserList dbsUserList = daoModel.getAllUser();
      for (int x=0;x<dbsUserList.getSize();x++)
      {
        domainListReaderWriterInterface.acquireWriteUserList().addNewUser(dbsUserList.getUserByIndex(x));
        domainListReaderWriterInterface.releaseWrite();
      }
      OrderList dbsOrderList = daoModel.getAllOrder();
      for (int x=0;x<dbsOrderList.getSize();x++)
      {
        domainListReaderWriterInterface.acquireWriteOrderList().addNewOrder(dbsOrderList.getOrderByIndex(x));
        domainListReaderWriterInterface.releaseWrite();
      }
    }
    else
    {
      hotel = Hotel.getHotel();
      daoModel.addHotel(hotel);
    }
  }

  @Override public void updateError(String error)
  {
    this.error.updateError(error);
    property.firePropertyChange("error",null,this.error);
  }

  @Override public ErrorInterface addNewUser(AccountInformation accountInformation)
  {
    UserList userList = domainListReaderWriterInterface.acquireWriteUserList();
    if (userList.getUserByEmail(accountInformation.getEmail())!=null)
    {
      domainListReaderWriterInterface.releaseWrite();
      return new HasError("This email has been registered.");
    }
    else
    {
      UserInterface user = new User(accountInformation.getEmail(),accountInformation.getPassword());
      daoModel.addUser(user.copy());
      userList.addNewUser(user);
      property.firePropertyChange("user",null,user);
      domainListReaderWriterInterface.releaseWrite();
      return new NoError();
    }
  }

  @Override public ErrorInterface updateUser(UserInterface newUser)
  {
    UserList userList = domainListReaderWriterInterface.acquireWriteUserList();
    if (userList.getUserByEmail(newUser.getAccountInformation().getEmail())!=null)
    {
      daoModel.updateUser(userList.getUserByEmail(newUser.getAccountInformation().getEmail()).copy(),newUser.copy());
      userList.updateUserAccountInformation(newUser.getAccountInformation());
      userList.updateUserBasicInformation(newUser.getBasicInformation());
      property.firePropertyChange("user",null,newUser);
      domainListReaderWriterInterface.releaseWrite();
      return new NoError();
    }
    else
    {
      domainListReaderWriterInterface.releaseWrite();
      return new HasError("Can't find this user!");
    }
  }

  @Override public UserInterface getUser(String email)
  {
    UserList userList = domainListReaderWriterInterface.acquireReadUserList();
    domainListReaderWriterInterface.releaseRead();
    return userList.getUserByEmail(email);
  }

  @Override public UserInterface getUser(int index)
  {
    UserList userList = domainListReaderWriterInterface.acquireReadUserList();
    domainListReaderWriterInterface.releaseRead();
    return userList.getUserByIndex(index);
  }

  @Override public UserList getUserList()
  {
    UserList userList = domainListReaderWriterInterface.acquireReadUserList();
    domainListReaderWriterInterface.releaseRead();
    return userList;
  }

  @Override public void addNewRoom(int roomNumber, String type)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    if (roomList.getRoomByRoomNumber(roomNumber)==null)
    {
      RoomInterface newRoom = new Room(roomNumber,type);
      roomList.addNewRoom(newRoom);
      daoModel.addRoom(newRoom);
      updateError(null);
      property.firePropertyChange("newRoom",null,roomList.getRoomByRoomNumber(roomNumber));
    }
    else
    {
      updateError("This room number has been used!");
    }
    domainListReaderWriterInterface.releaseWrite();
  }

  @Override public void updateRoom(RoomInterface oldRoom,RoomInterface newRoom)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    if (roomList.getRoom(oldRoom)==null)
    {
      updateError("Wrong room!");
    }
    else
    {
      if (oldRoom.getRoomNumber()==newRoom.getRoomNumber()||roomList.getRoom(newRoom)==null)
      {
        daoModel.updateRoom(oldRoom.copy(),newRoom.copy());
        roomList.updateRoom(oldRoom,newRoom);
        updateError(null);
        property.firePropertyChange("updateRoom",newRoom.getRoomNumber(),newRoom);
      }
      else
      {
        updateError("This room number has been used!");
      }
    }
    domainListReaderWriterInterface.releaseWrite();
  }

  @Override public void updateRoomType(RoomInterface room, String newType)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    if (roomList.getRoom(room)==null)
    {
      updateError("Wrong room!");
    }
    else
    {
      RoomInterface newRoom = new Room(room.getRoomNumber(),newType);
      daoModel.updateRoom(roomList.getRoomByRoomNumber(room.getRoomNumber()),newRoom);
      roomList.updateRoomType(newRoom);
      updateError(null);
      property.firePropertyChange("updateRoom",room.getRoomNumber(),roomList.getRoomByRoomNumber(room.getRoomNumber()));
    }
    domainListReaderWriterInterface.releaseWrite();
  }

  @Override public ErrorInterface updateRoomBookedTimeRange(RoomInterface room, TimeRangeList timeRangeList)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    RoomInterface chosenRoom = roomList.getRoom(room);
    if (chosenRoom==null)
    {
      domainListReaderWriterInterface.releaseWrite();
      return new HasError("Wrong room!");
    }
    else
    {
      RoomInterface newRoom = chosenRoom.copy();
      newRoom.setBookedTimeRangeList(timeRangeList);
      daoModel.updateRoom(chosenRoom,newRoom);
      chosenRoom.setBookedTimeRangeList(timeRangeList);
      property.firePropertyChange("updateRoom",room.getRoomNumber(),roomList.getRoomByRoomNumber(room.getRoomNumber()));
      domainListReaderWriterInterface.releaseWrite();
      return new NoError();
    }
  }

  @Override public RoomInterface getRoom(RoomInterface room)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireReadRoomList();
    domainListReaderWriterInterface.releaseRead();
    return roomList.getRoom(room);
  }

  @Override public RoomInterface getRoomByRoomNumber(int roomNumber)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireReadRoomList();
    domainListReaderWriterInterface.releaseRead();
    return roomList.getRoomByRoomNumber(roomNumber);
  }

  @Override public RoomInterface getRoomByIndex(int index)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireReadRoomList();
    domainListReaderWriterInterface.releaseRead();
    return roomList.getRoomByIndex(index);
  }

  @Override public RoomList getRoomsByType(String type)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireReadRoomList();
    domainListReaderWriterInterface.releaseRead();
    return roomList.getRoomsByType(type);
  }

  @Override public RoomList getRoomsByFreeTimeRangeList(TimeRangeList timeRangeList)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireReadRoomList();
    domainListReaderWriterInterface.releaseRead();
    return roomList.getRoomsByFreeTimeRangeList(timeRangeList);
  }

  @Override public RoomList getRooms()
  {
    RoomList roomList = domainListReaderWriterInterface.acquireReadRoomList();
    domainListReaderWriterInterface.releaseRead();
    return roomList;
  }

  @Override public void removeRoom(RoomInterface room)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    daoModel.removeRoom(roomList.getRoom(room).copy());
    roomList.removeRoom(room);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("removeRoom",null,"");
  }

  @Override public void removeRoomByRoomNumber(int roomNumber)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    daoModel.removeRoom(roomList.getRoomByRoomNumber(roomNumber).copy());
    roomList.removeRoomByRoomNumber(roomNumber);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("removeRoom",null,"");
  }

  @Override public void removeRoomByIndex(int index)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    daoModel.removeRoom(roomList.getRoomByIndex(index).copy());
    roomList.removeRoomByIndex(index);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("removeRoom",null,"");
  }

  @Override public void removeRoomsByType(String type)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    for (int x=0;x<roomList.getRoomsByType(type).getSize();x++)
    {
      daoModel.removeRoom(roomList.getRoom(roomList.getRoomsByType(type).getRoomByIndex(x)).copy());
    }
    roomList.removeRoomsByType(type);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("removeRoom",null,"");
  }

  @Override public ErrorInterface addNewOrder(OrderInterface newOrder)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    RoomList otherRoomList = roomList.getRoomsByType(newOrder.getRoomInformation().getType());
    newOrder.setRoom(null);
    RoomInterface chooseRoom = null;
    for (int i=0;i<otherRoomList.getSize();i++)
    {
      for (int x=0;x<newOrder.getTimeRangeList().getSize();x++)
      {
        if (otherRoomList.getRoomByIndex(i).getBookedTimeRangeList().hasTimeRange(newOrder.getTimeRangeList().getTimeRangeByIndex(x)))
        {
          break;
        }
        else
        {
          if (x==newOrder.getTimeRangeList().getSize()-1)
          {
            newOrder.setRoom(otherRoomList.getRoomByIndex(i).copy());
            chooseRoom = otherRoomList.getRoomByIndex(i).copy();
          }
        }
      }
      if (newOrder.getRoom()!=null)
      {
        break;
      }
    }
    if (newOrder.getRoom()==null)
    {
      domainListReaderWriterInterface.releaseWrite();
      return new HasError("All rooms in this time range has been ordered!");
    }
    for (int x=0;x<newOrder.getTimeRangeList().getSize();x++)
    {
      roomList.getRoom(newOrder.getRoom()).addBookedTimeRange(newOrder.getTimeRangeList().getTimeRangeByIndex(x));
    }
    daoModel.updateRoom(chooseRoom,roomList.getRoom(newOrder.getRoom()).copy());
    domainListReaderWriterInterface.releaseWrite();
    OrderList orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    int newOrderNumber = (int) (Math.random() * (10 - 1) + 1) * 100000000 + (int) (Math.random() * (100000000 - 1) + 1);
    while (orderList.getOrderByOrderNumber(newOrderNumber) != null)
    {
      newOrderNumber = (int) (Math.random() * (10 - 1) + 1) * 100000000 + (int) (Math.random() * (100000000 - 1) + 1);
    }
    newOrder.setOrderNumber(newOrderNumber);
    daoModel.addOrder(newOrder.copy());
    orderList.addNewOrder(newOrder);
    property.firePropertyChange("addOrder",null,newOrder);
    domainListReaderWriterInterface.releaseWrite();
    return new NoError();
  }

  @Override public ErrorInterface updateOrder(OrderInterface newOrder)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    if (orderList.getOrderByOrderNumber(newOrder.getOrderNumber())!=null)
    {
      for (int x=0;x<newOrder.getTimeRangeList().getSize();x++)
      {
        if (!orderList.getOrderByOrderNumber(newOrder.getOrderNumber()).getTimeRangeList().hasTimeRange(newOrder.getTimeRangeList().getTimeRangeByIndex(x)))
        {
          RoomList roomList = domainListReaderWriterInterface.acquireReadRoomList();
          domainListReaderWriterInterface.releaseRead();
          if (roomList.getRoomByRoomNumber(newOrder.getRoom().getRoomNumber()).getBookedTimeRangeList().hasTimeRange(newOrder.getTimeRangeList().getTimeRangeByIndex(x)))
          {
            domainListReaderWriterInterface.releaseRead();
            return new HasError(newOrder.getTimeRangeList().showDateByIndex(x) + " has been ordered!");
          }
        }
      }
      domainListReaderWriterInterface.releaseRead();
      orderList = domainListReaderWriterInterface.acquireWriteOrderList();
      orderList.updateOrder(newOrder);
      daoModel.updateOrder(orderList.getOrderByOrderNumber(newOrder.getOrderNumber()).copy());
      domainListReaderWriterInterface.releaseWrite();
      property.firePropertyChange("updateOrder",null,newOrder);
      return new NoError();
    }
    domainListReaderWriterInterface.releaseRead();
    return new HasError("Can't find this order!");
  }

  @Override public OrderInterface getOrderByIndex(int index)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    return orderList.getOrderByIndex(index);
  }

  @Override public OrderInterface getOrder(int orderNumber)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    return orderList.getOrderByOrderNumber(orderNumber);
  }

  @Override public OrderList getOrders()
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    return orderList;
  }

  @Override public OrderList getOrders(UserInterface user)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    return orderList.getOrdersByUser(user);
  }

  @Override public OrderList getOrders(RoomInterface room)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    return orderList.getOrdersByRoom(room);
  }

  @Override public OrderList getOrders(RoomInformation roomInformation)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    return orderList.getOrdersByRoomInformation(roomInformation);
  }

  @Override public OrderList getOrders(TimeRangeInterface timeRange)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    return orderList.getOrdersByTimeRange(timeRange);
  }

  @Override public OrderList getOrders(TimeRangeList timeRangeList)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    return orderList.getOrdersByTimeRangeList(timeRangeList);
  }

  private void removeRoomBookedTimeRangeListByOrder(OrderInterface order)
  {
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    RoomInterface oldRoom = roomList.getRoomByRoomNumber(order.getRoom().getRoomNumber()).copy();
    for (int x=0;x<order.getTimeRangeList().getSize();x++)
    {
      roomList.getRoomByRoomNumber(order.getRoom().getRoomNumber()).removeBookedTimeRange(order.getTimeRangeList().getTimeRangeByIndex(x));
    }
    daoModel.updateRoom(oldRoom,roomList.getRoomByRoomNumber(oldRoom.getRoomNumber()).copy());
    domainListReaderWriterInterface.releaseWrite();
    roomList = domainListReaderWriterInterface.acquireReadRoomList();
    property.firePropertyChange("updateRoom",order.getRoom().getRoomNumber(),roomList.getRoom(order.getRoom()));
    domainListReaderWriterInterface.releaseRead();
  }

  private void removeRoomBookedTimeRangeListByOrderList(OrderList orderList)
  {
    for (int x=0;x<orderList.getSize();x++)
    {
      OrderInterface order = orderList.getOrderByIndex(x);
      removeRoomBookedTimeRangeListByOrder(order);
    }
  }

  @Override public void cancelOrderByIndex(int index)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    OrderInterface order = orderList.getOrderByIndex(index);
    removeRoomBookedTimeRangeListByOrder(order);
    orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    orderList.cancelOrderByIndex(index);
    daoModel.updateOrder(orderList.getOrderByIndex(index).copy());
    domainListReaderWriterInterface.releaseWrite();
    orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    property.firePropertyChange("cancelOrder",null,orderList.getOrderByOrderNumber(index));
  }

  @Override public void cancelOrder(int orderNumber)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    OrderInterface canceledOrder = orderList.getOrderByOrderNumber(orderNumber);
    removeRoomBookedTimeRangeListByOrder(canceledOrder);
    orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    orderList.cancelOrderByOrderNumber(orderNumber);
    daoModel.updateOrder(orderList.getOrderByOrderNumber(orderNumber).copy());
    domainListReaderWriterInterface.releaseWrite();
    orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    property.firePropertyChange("cancelOrder",null,orderList.getOrderByOrderNumber(orderNumber));
  }

  @Override public void cancelOrders(UserInterface user)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    OrderList canceledOrderList = orderList.getOrdersByUser(user);
    removeRoomBookedTimeRangeListByOrderList(canceledOrderList);
    orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    orderList.cancelOrdersByUser(user);
    for (int x=0;x<orderList.getOrdersByUser(user).getSize();x++)
    {
      daoModel.updateOrder(orderList.getOrdersByUser(user).getOrderByIndex(x).copy());
    }
    domainListReaderWriterInterface.releaseWrite();
    orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    property.firePropertyChange("cancelOrders",null,orderList.getOrdersByUser(user));
  }

  @Override public void cancelOrders(RoomInterface room)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    OrderList canceledOrderList = orderList.getOrdersByRoom(room);
    removeRoomBookedTimeRangeListByOrderList(canceledOrderList);
    orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    orderList.cancelOrdersByRoom(room);
    for (int x=0;x<orderList.getOrdersByRoom(room).getSize();x++)
    {
      daoModel.updateOrder(orderList.getOrdersByRoom(room).getOrderByIndex(x).copy());
    }
    domainListReaderWriterInterface.releaseWrite();
    orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    property.firePropertyChange("cancelOrders",null,orderList.getOrdersByRoom(room));
  }

  @Override public void cancelOrders(RoomInformation roomInformation)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    OrderList canceledOrderList = orderList.getOrdersByRoomInformation(roomInformation);
    removeRoomBookedTimeRangeListByOrderList(canceledOrderList);
    orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    orderList.cancelOrdersByRoomInformation(roomInformation);
    for (int x=0;x<orderList.getOrdersByRoomInformation(roomInformation).getSize();x++)
    {
      daoModel.updateOrder(orderList.getOrdersByRoomInformation(roomInformation).getOrderByIndex(x).copy());
    }
    domainListReaderWriterInterface.releaseWrite();
    orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    property.firePropertyChange("cancelOrders",null,orderList.getOrdersByRoomInformation(roomInformation));
  }

  @Override public void cancelOrders(TimeRangeInterface timeRange)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireReadOrderList();
    domainListReaderWriterInterface.releaseRead();
    OrderList canceledOrderList = orderList.getOrdersByTimeRange(timeRange).copy();
    removeRoomBookedTimeRangeListByOrderList(canceledOrderList);

    orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    orderList.cancelOrdersByOrderList(canceledOrderList);
    for (int x=0;x<canceledOrderList.getSize();x++)
    {
      daoModel.updateOrder(canceledOrderList.getOrderByIndex(x).copy());
    }
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("cancelOrders",null,domainListReaderWriterInterface.acquireReadOrderList().getOrdersByTimeRange(timeRange));
    domainListReaderWriterInterface.releaseRead();
  }

  private void updateCheckInAndCheckOutHourInOrdersAndRooms(int checkInHour,int checkOutHour)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    for (int x=0;x<orderList.getSize();x++)
    {
      if(orderList.getOrderByIndex(x).getOrderState().getState()!="Cancel")
      {
        for (int i=0;i<orderList.getOrderByIndex(x).getTimeRangeList().getSize();i++)
        {
          orderList.getOrderByIndex(x).getTimeRangeList().getTimeRangeByIndex(i).getStartTime().setClock(checkInHour,0);
          orderList.getOrderByIndex(x).getTimeRangeList().getTimeRangeByIndex(i).getEndTime().setClock(checkOutHour,0);
        }
        daoModel.updateOrder(orderList.getOrderByIndex(x).copy());
        property.firePropertyChange("updateOrder",null,orderList.getOrderByIndex(x).copy());
      }
    }
    domainListReaderWriterInterface.releaseWrite();
    RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
    for (int x=0;x<roomList.getSize();x++)
    {
      RoomInterface oldRoom = roomList.getRoomByIndex(x).copy();
      for (int i=0;i<roomList.getRoomByIndex(x).getBookedTimeRangeList().getSize();i++)
      {
        roomList.getRoomByIndex(x).getBookedTimeRangeList().getTimeRangeByIndex(i).getStartTime().setClock(checkInHour,0);
        roomList.getRoomByIndex(x).getBookedTimeRangeList().getTimeRangeByIndex(i).getEndTime().setClock(checkOutHour,0);
      }
      daoModel.updateRoom(oldRoom,roomList.getRoomByIndex(x).copy());
      property.firePropertyChange("updateRoom",oldRoom.getRoomNumber(),roomList.getRoomByIndex(x).copy());
    }
    domainListReaderWriterInterface.releaseWrite();
  }

  @Override public void updateHotel(String name,String address,String contactNumber,String facilities,String characteristic,String description,String rules,int checkIn,int checkOut)
  {
    Hotel oldHotel = hotel.copy();
    hotel.setName(name);
    hotel.setAddress(address);
    hotel.setContactNumber(contactNumber);
    hotel.setFacilities(facilities);
    hotel.setCharacteristic(characteristic);
    hotel.setDescription(description);
    hotel.setRules(rules);
    hotel.setCheckInHour(checkIn);
    hotel.setCheckOutHour(checkOut);
    daoModel.updateHotel(oldHotel,hotel);
    updateCheckInAndCheckOutHourInOrdersAndRooms(checkIn,checkOut);
    updateError(null);
    property.firePropertyChange("hotel",null,hotel);
  }

  @Override public HotelInterface getHotel()
  {
    return hotel;
  }

  @Override public RoomInformationList getRoomInformationList()
  {
    return hotel.getRoomInformationList();
  }

  @Override public RoomInformation getRoomInformationByIndex(int index)
  {
    return hotel.getRoomInformationList().getRoomInformationByIndex(index);
  }

  @Override public void addRoomInformation(RoomInformation roomInformation)
  {
    if (roomInformation!=null)
    {
      if (hotel.getRoomInformationList().getRoomInformationByType(roomInformation.getType())==null)
      {
        hotel.getRoomInformationList().addNewRoomInformation(roomInformation);
        daoModel.addRoomInformation(roomInformation);
        updateError(null);
      }
      else
      {
        updateError("This room type has been used!");
      }
    }
    else
    {
      updateError("Illegal add RoomInformation!");
    }
  }

  @Override public void updateRoomInformation(RoomInformation oldRoomInformation, RoomInformation newRoomInformation)
  {
    if (oldRoomInformation!=null&&newRoomInformation!=null)
    {
      if (hotel.getRoomInformationList().getRoomInformationByType(oldRoomInformation.getType())==null)
      {
        updateError("Can't find this room type!");
      }
      else
      {
        if (oldRoomInformation.getType().equals(newRoomInformation.getType())||hotel.getRoomInformationList().getRoomInformationByType(newRoomInformation.getType())==null)
        {
          RoomList roomList = domainListReaderWriterInterface.acquireWriteRoomList();
          daoModel.updateRoomInformation(oldRoomInformation,newRoomInformation);
          for (int x=0;x<roomList.getSize();x++)
          {
            if (roomList.getRoomByIndex(x).getType().equals(oldRoomInformation.getType()))
            {
              RoomInterface oldRoom = roomList.getRoomByIndex(x).copy();
              roomList.getRoomByIndex(x).setType(newRoomInformation.getType());
              RoomInterface newRoom = roomList.getRoomByIndex(x).copy();
              daoModel.updateRoom(oldRoom,newRoom);
              property.firePropertyChange("updateRoom",null,roomList.getRoomByIndex(x));
            }
          }
          domainListReaderWriterInterface.releaseWrite();
          hotel.getRoomInformationList().getRoomInformationByType(oldRoomInformation.getType()).setSupplyForFree(newRoomInformation.getSupplyForFree().copy());
          hotel.getRoomInformationList().getRoomInformationByType(oldRoomInformation.getType()).setFacilities(newRoomInformation.getFacilities());
          hotel.getRoomInformationList().getRoomInformationByType(oldRoomInformation.getType()).setSize(newRoomInformation.getSize());
          hotel.getRoomInformationList().getRoomInformationByType(oldRoomInformation.getType()).setPrice(newRoomInformation.getPrice());
          hotel.getRoomInformationList().getRoomInformationByType(oldRoomInformation.getType()).setType(newRoomInformation.getType());
          updateError(null);
        }
        else
        {
          updateError("This room type has been used!");
        }
      }
    }
    else
    {
      updateError("Illegal update RoomInformation!");
    }
  }

  @Override public void removeRoomInformation(RoomInformation roomInformation)
  {
    daoModel.removeRoomInformation(hotel.getRoomInformationList().getRoomInformationByType(roomInformation.getType()));
    hotel.getRoomInformationList().removeRoomInformation(roomInformation);
    property.firePropertyChange("hotel",null,hotel);
  }

  @Override public void removeRoomInformation(String type)
  {
    daoModel.removeRoomInformation(hotel.getRoomInformationList().getRoomInformationByType(type));
    hotel.getRoomInformationList().removeRoomInformationByType(type);
    property.firePropertyChange("hotel",null,hotel);
  }

  @Override public void addOptionService(OptionService optionService)
  {
    if (optionService!=null)
    {
      if (hotel.getOptionServiceList().getOptionServiceByServiceType(optionService.getServiceType())==null)
      {
        hotel.getOptionServiceList().addOptionService(optionService);
        daoModel.addOptionService(optionService);
        property.firePropertyChange("hotel",null,hotel);
        updateError(null);
      }
      else
      {
        updateError("This service type has been used!");
      }
    }
    else
    {
      updateError("Illegal add OptionService!");
    }
  }

  @Override public void updateOptionService(OptionService oldOptionService, OptionService newOptionService)
  {
    if (oldOptionService!=null&&newOptionService!=null)
    {
      if (hotel.getOptionServiceList().getOptionServiceByServiceType(oldOptionService.getServiceType())!=null)
      {
        if (newOptionService.getServiceType().equals(oldOptionService.getServiceType()))
        {
          for (int x=0;x<hotel.getRoomInformationList().getSize();x++)
          {
            RoomInformation oldRoomInformation = hotel.getRoomInformationList().getRoomInformationByIndex(x).copy();
            hotel.getRoomInformationList().getRoomInformationByIndex(x).getSupplyForFree().updatePrice(newOptionService);
            daoModel.updateRoomInformation(oldRoomInformation,hotel.getRoomInformationList().getRoomInformationByIndex(x));
          }
          hotel.getOptionServiceList().updatePrice(newOptionService);
        }
        else
        {
          if (hotel.getOptionServiceList().getOptionServiceByServiceType(newOptionService.getServiceType())==null)
          {
            for (int x=0;x<hotel.getRoomInformationList().getSize();x++)
            {
              RoomInformation oldRoomInformation = hotel.getRoomInformationList().getRoomInformationByIndex(x).copy();
              hotel.getRoomInformationList().getRoomInformationByIndex(x).getSupplyForFree().updateOptionService(oldOptionService,newOptionService);
              daoModel.updateRoomInformation(oldRoomInformation,hotel.getRoomInformationList().getRoomInformationByIndex(x));
            }
            hotel.getOptionServiceList().updateOptionService(oldOptionService,newOptionService);
          }
          else
          {
            updateError("This service type has been used!");
          }
        }
      }
      else
      {
        updateError("Can't find this service type has been used!");
      }
      daoModel.updateOptionService(oldOptionService, newOptionService);
      property.firePropertyChange("hotel",null,hotel);
      updateError(null);
    }
    else
    {
      updateError("Illegal update OptionService!");
    }
  }

  @Override public void removeOptionService(OptionService optionService)
  {
    for (int x=0;x<hotel.getRoomInformationList().getSize();x++)
    {
      RoomInformation oldRoomInformation = hotel.getRoomInformationList().getRoomInformationByIndex(x).copy();
      hotel.getRoomInformationList().getRoomInformationByIndex(x).getSupplyForFree().removeOptionService(optionService);
      daoModel.updateRoomInformation(oldRoomInformation,hotel.getRoomInformationList().getRoomInformationByIndex(x));
    }
    daoModel.removeOptionService(hotel.getOptionServiceList().getOptionServiceByServiceType(optionService.getServiceType()));
    hotel.getOptionServiceList().removeOptionService(optionService);
    property.firePropertyChange("hotel",null,hotel);
  }

  @Override public void removeOptionService(int index)
  {
    for (int x=0;x<hotel.getRoomInformationList().getSize();x++)
    {
      RoomInformation oldRoomInformation = hotel.getRoomInformationList().getRoomInformationByIndex(x).copy();
      hotel.getRoomInformationList().getRoomInformationByIndex(x).getSupplyForFree().removeOptionService(hotel.getOptionServiceList().getOptionServiceByIndex(index));
      daoModel.updateRoomInformation(oldRoomInformation,hotel.getRoomInformationList().getRoomInformationByIndex(x));
    }
    daoModel.removeOptionService(hotel.getOptionServiceList().getOptionServiceByIndex(index));
    hotel.getOptionServiceList().removeOptionServiceByIndex(index);
    property.firePropertyChange("hotel",null,hotel);
  }

  @Override public void removeOptionService(String serviceType)
  {
    for (int x=0;x<hotel.getRoomInformationList().getSize();x++)
    {
      RoomInformation oldRoomInformation = hotel.getRoomInformationList().getRoomInformationByIndex(x).copy();
      hotel.getRoomInformationList().getRoomInformationByIndex(x).getSupplyForFree().removeOptionService(hotel.getOptionServiceList().getOptionServiceByServiceType(serviceType));
      daoModel.updateRoomInformation(oldRoomInformation,hotel.getRoomInformationList().getRoomInformationByIndex(x));
    }
    daoModel.removeOptionService(hotel.getOptionServiceList().getOptionServiceByServiceType(serviceType));
    hotel.getOptionServiceList().removeOptionServiceByServiceType(serviceType);
    property.firePropertyChange("hotel",null,hotel);
  }

  @Override public OptionServiceList getOptionServiceList()
  {
    return hotel.getOptionServiceList();
  }

  @Override public OptionService getOptionServiceByIndex(int index)
  {
    return hotel.getOptionServiceList().getOptionServiceByIndex(index);
  }

  @Override public OptionService getOptionServiceByServiceType(
      String serviceType)
  {
    return hotel.getOptionServiceList().getOptionServiceByServiceType(serviceType);
  }

  @Override public void addListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.addPropertyChangeListener(propertyName,listener);
  }

  @Override public void removeListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(propertyName,listener);
  }
}
