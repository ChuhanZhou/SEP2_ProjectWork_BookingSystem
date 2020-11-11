package model;

import mediator.Client;
import mediator.ClientModel;
import model.domain.*;
import model.domain.error.ErrorInterface;
import model.domain.error.HasError;
import model.domain.error.NoError;
import model.domain.hotel.*;
import model.domain.order.Order;
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
  private UserInterface user;
  private HotelInterface hotel;
  private LocalError error;
  private PropertyChangeSupport property;
  private DomainListReaderWriterInterface domainListReaderWriterInterface;
  private ClientModel clientModel;

  public BookingModelManager()
  {
    user = new User(null,null);
    hotel = Hotel.getHotel();
    error = LocalError.getLocalError();
    property = new PropertyChangeSupport(this);
    domainListReaderWriterInterface = new DomainListReaderWriter(new OrderList());
  }

  @Override public boolean connectToServer(int port, String host)
  {
    if (port==0)
    {
      if (host==null)
      {
        clientModel = new Client();
      }
      else
      {
        clientModel = new Client(host);
      }
    }
    else
    {
      if (host==null)
      {
        clientModel = new Client(port);
      }
      else
      {
        clientModel = new Client(port,host);
      }
    }
    return clientModel.connect(this);
  }

  @Override public void login(String action, UserInterface user)
  {
    Thread client = new Thread(()->clientModel.start(action,user.copy()));
    client.setDaemon(true);
    client.start();
  }

  @Override public void signOut()
  {
    clientModel.disconnect();
    user = new User(null,null);
    hotel = Hotel.getHotel(null,null,null,null,null,null,null);
    domainListReaderWriterInterface = new DomainListReaderWriter(new OrderList());
  }

  @Override public void sendUserPackage(UserInterface user)
  {
    clientModel.sendUserPackage(user);
  }

  @Override public void sendOrderPackage(OrderInterface order)
  {
    clientModel.sendOrderPackage(order);
  }

  @Override public void sendSearchPackage(RoomInformationList roomInformationList, TimeRangeList timeRangeList)
  {
    clientModel.sendSearchPackage(roomInformationList, timeRangeList);
  }

  @Override public void updateError(String error)
  {
    this.error.updateError(error);
    property.firePropertyChange("error",null,this.error);
  }

  @Override public UserInterface getUser()
  {
    return user.copy();
  }

  @Override public void updateUser(UserInterface user)
  {
    if (this.user.getAccountInformation().getEmail()==null)
    {
      this.user = user.copy();
    }
    else
    {
      this.user.setPassword(user.getAccountInformation());
      this.user.setBasicInformation(user.getBasicInformation());
    }
    property.firePropertyChange("updateUser",null,user);
  }

  @Override public ErrorInterface addNewOrder(OrderInterface newOrder)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    orderList.addNewOrder(newOrder);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("addOrder",null,newOrder);
    return new NoError();
  }

  @Override public ErrorInterface updateOrder(OrderInterface newOrder)
  {
    OrderList orderList = domainListReaderWriterInterface.acquireWriteOrderList();
    if (orderList.getOrderByOrderNumber(newOrder.getOrderNumber())!=null)
    {
      orderList.updateOrder(newOrder);
      domainListReaderWriterInterface.releaseWrite();
      property.firePropertyChange("updateOrder",null,domainListReaderWriterInterface.acquireReadOrderList().getOrderByOrderNumber(newOrder.getOrderNumber()));
      domainListReaderWriterInterface.releaseRead();
      return new NoError();
    }
    domainListReaderWriterInterface.releaseWrite();
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

  @Override public void cancelOrderByIndex(int index)
  {
    domainListReaderWriterInterface.acquireWriteOrderList().cancelOrderByIndex(index);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("cancelOrder",null,domainListReaderWriterInterface.acquireReadOrderList());
    domainListReaderWriterInterface.releaseRead();
  }

  @Override public void cancelOrder(int orderNumber)
  {
    domainListReaderWriterInterface.acquireWriteOrderList().cancelOrderByOrderNumber(orderNumber);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("cancelOrder",null,domainListReaderWriterInterface.acquireReadOrderList());
    domainListReaderWriterInterface.releaseRead();
  }

  @Override public void cancelOrders(UserInterface user)
  {
    domainListReaderWriterInterface.acquireWriteOrderList().cancelOrdersByUser(user);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("cancelOrder",null,domainListReaderWriterInterface.acquireReadOrderList());
    domainListReaderWriterInterface.releaseRead();
  }

  @Override public void cancelOrders(RoomInterface room)
  {
    domainListReaderWriterInterface.acquireWriteOrderList().cancelOrdersByRoom(room);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("cancelOrder",null,domainListReaderWriterInterface.acquireReadOrderList());
    domainListReaderWriterInterface.releaseRead();
  }

  @Override public void cancelOrders(RoomInformation roomInformation)
  {
    domainListReaderWriterInterface.acquireWriteOrderList().cancelOrdersByRoomInformation(roomInformation);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("cancelOrder",null,domainListReaderWriterInterface.acquireReadOrderList());
    domainListReaderWriterInterface.releaseRead();
  }

  @Override public void cancelOrders(TimeRangeInterface timeRange)
  {
    domainListReaderWriterInterface.acquireWriteOrderList().cancelOrdersByTimeRange(timeRange);
    domainListReaderWriterInterface.releaseWrite();
    property.firePropertyChange("cancelOrder",null,domainListReaderWriterInterface.acquireReadOrderList());
    domainListReaderWriterInterface.releaseRead();
  }

  @Override public void updateHotel(HotelInterface hotel)
  {
    this.hotel = hotel;
    property.firePropertyChange("hotel",null,this.hotel);
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

  @Override public ErrorInterface addOptionService(OptionService optionService)
  {
    if (hotel.getOptionServiceList().getOptionServiceByServiceType(optionService.getServiceType())==null)
    {
      hotel.getOptionServiceList().addOptionService(optionService);
      property.firePropertyChange("hotel",null,hotel);
      return new NoError();
    }
    else
    {
      return new HasError("This service type has been used!");
    }
  }

  @Override public ErrorInterface updateOptionService(OptionService oldOptionService, OptionService newOptionService)
  {
    if (hotel.getOptionServiceList().getOptionServiceByServiceType(oldOptionService.getServiceType())!=null)
    {
      if (newOptionService.getServiceType().equals(oldOptionService.getServiceType()))
      {
        hotel.getOptionServiceList().updatePrice(newOptionService);
      }
      else
      {
        if (hotel.getOptionServiceList().getOptionServiceByServiceType(newOptionService.getServiceType())==null)
        {
          hotel.getOptionServiceList().updateOptionService(oldOptionService,newOptionService);
        }
        else
        {
          return new HasError("This service type has been used!");
        }
      }
    }
    else
    {
      return new HasError("Can't find this service type has been used!");
    }
    property.firePropertyChange("hotel",null,hotel);
    return new NoError();
  }

  @Override public void removeOptionService(OptionService optionService)
  {
    hotel.getOptionServiceList().removeOptionService(optionService);
    property.firePropertyChange("hotel",null,hotel);
  }

  @Override public void removeOptionService(int index)
  {
    hotel.getOptionServiceList().removeOptionServiceByIndex(index);
    property.firePropertyChange("hotel",null,hotel);
  }

  @Override public void removeOptionService(String serviceType)
  {
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

  @Override public void searchRoomInformationListFromServer(RoomInformationList roomInformationList, TimeRangeList timeRangeList)
  {
    clientModel.sendSearchPackage(roomInformationList, timeRangeList);
  }

  @Override public void updateSearchInformation(RoomInformationList roomInformationList)
  {
    property.firePropertyChange("search",null,roomInformationList);
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
