package model.domain;

import model.domain.hotel.RoomInformation;
import model.domain.order.Order;
import model.domain.order.OrderInterface;
import model.domain.order.OrderState;
import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;
import model.domain.room.timeRange.TimeRangeInterface;
import model.domain.user.UserInterface;

import java.util.ArrayList;

public class OrderList
{
  private ArrayList<Order> orderList;

  public OrderList()
  {
    orderList = new ArrayList<>();
  }

  public void addNewOrder(OrderInterface newOrder)
  {
    if (newOrder!=null)
    {
      if (getOrderByOrderNumber(newOrder.getOrderNumber())==null)
      {
        orderList.add(newOrder.copy());
      }
    }
  }

  public void updateOrder(OrderInterface newOrder)
  {
    if (newOrder!=null)
    {
      for (int x = 0; x < orderList.size(); x++)
      {
        if (orderList.get(x).getOrderNumber() == newOrder.getOrderNumber())
        {
          orderList.set(x, newOrder.copy());
          break;
        }
      }
    }
  }

  public OrderInterface getOrderByIndex(int index)
  {
    if (index<orderList.size()&&index>=0)
    {
      return orderList.get(index);
    }
    return null;
  }

  public OrderInterface getOrderByOrderNumber(int orderNumber)
  {
    for (int i = 0; i < orderList.size(); i++)
    {
      if (orderList.get(i).getOrderNumber() == orderNumber)
      {
        return orderList.get(i);
      }
    }
    return null;
  }

  public OrderList getOrdersByUser(UserInterface user)
  {
    OrderList list = new OrderList();
    if (user!=null)
    {
      for (int i = 0; i < orderList.size(); i++)
      {
        if (orderList.get(i).getBasicInformation().getEmail().equals(user.getBasicInformation().getEmail()))
        {
          list.addNewOrder(orderList.get(i));
        }
      }
    }
    return list;
  }

  public OrderList getOrdersByStatus(OrderState orderState)
  {
    OrderList list = new OrderList();
    if (orderState!=null)
    {
      for (int x=0;x<orderList.size();x++)
      {
        if (orderList.get(x).getOrderState().getState().equals(orderState.getState()))
        {
          list.addNewOrder(orderList.get(x));
        }
      }
    }
    return list;
  }

  public OrderList getOrdersByRoom(RoomInterface room)
  {
    OrderList list = new OrderList();
    if (room!=null)
    {
      for (int i = 0; i < orderList.size(); i++)
      {
        if (orderList.get(i).getRoom().equals(room))
        {
          list.addNewOrder(orderList.get(i));
        }
      }
    }
    return list;
  }

  public OrderList getOrdersByRoomInformation(RoomInformation roomInformation)
  {
    OrderList list = new OrderList();
    if (roomInformation!=null)
    {
      for (int i = 0; i < orderList.size(); i++)
      {
        if (orderList.get(i).getRoomInformation().equals(roomInformation))
        {
          list.addNewOrder(orderList.get(i));
        }
      }
    }
    return list;
  }

  public OrderList getOrdersByTimeRange(TimeRangeInterface timeRange)
  {
    TimeRangeList timeRangeList = new TimeRangeList();
    timeRangeList.addTimeRange(timeRange);
    return getOrdersByTimeRangeList(timeRangeList);
  }

  public OrderList getOrdersByTimeRangeList(TimeRangeList timeRangeList)
  {
    OrderList list = new OrderList();
    for (int i=0;i<orderList.size();i++)
    {
      for (int x=0;x<timeRangeList.getSize();x++)
      {
        if (orderList.get(i).getTimeRangeList().hasTimeRange(timeRangeList.getTimeRangeByIndex(x)))
        {
          list.addNewOrder(orderList.get(i));
          break;
        }
      }
    }
    return list;
  }

  public void cancelOrderByIndex(int index)
  {
    if (index<orderList.size()&&index>=0)
    {
      orderList.get(index).cancelOrder();
    }
  }

  public void cancelOrderByOrderNumber(int orderNumber)
  {
    if(getOrderByOrderNumber(orderNumber)!=null)
    {
      getOrderByOrderNumber(orderNumber).cancelOrder();
    }
  }

  public void cancelOrdersByUser(UserInterface user)
  {
    if (user!=null)
    {
      for (int i=0;i<orderList.size();i++)
      {
        if (orderList.get(i).getBasicInformation().getEmail().equals(user.getBasicInformation().getEmail()))
        {
          orderList.get(i).cancelOrder();
        }
      }
    }
  }

  public void cancelOrdersByRoom(RoomInterface room)
  {
    for (int i=0;i<orderList.size();i++)
    {
      if (orderList.get(i).getRoom().equals(room))
      {
        orderList.get(i).cancelOrder();
      }
    }
  }

  public void cancelOrdersByRoomInformation(RoomInformation roomInformation)
  {
    for (int i=0;i<orderList.size();i++)
    {
      if (orderList.get(i).getRoomInformation().equals(roomInformation))
      {
        orderList.get(i).cancelOrder();
      }
    }
  }

  public void cancelOrdersByTimeRange(TimeRangeInterface timeRange)
  {
    for (int i=0;i<orderList.size();i++)
    {
      if (orderList.get(i).getTimeRangeList().hasTimeRange(timeRange))
      {
        orderList.get(i).cancelOrder();
      }
    }
  }

  public void cancelOrdersByOrderList(OrderList orderList)
  {
    if (orderList!=null)
    {
      for (int i=0;i<this.orderList.size();i++)
      {
        if (orderList.getOrderByOrderNumber(this.orderList.get(i).getOrderNumber())!=null)
        {
          this.orderList.get(i).cancelOrder();
        }
      }
    }
  }

  public int getSize()
  {
    return orderList.size();
  }

  public OrderList copy()
  {
    OrderList other = new OrderList();
    for (int x=0;x<orderList.size();x++)
    {
      other.addNewOrder(orderList.get(x).copy());
    }
    return other;
  }
}
