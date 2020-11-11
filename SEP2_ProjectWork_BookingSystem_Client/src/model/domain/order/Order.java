package model.domain.order;

import model.domain.hotel.OptionServiceList;
import model.domain.hotel.RoomInformation;
import model.domain.room.Room;
import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;
import model.domain.user.BasicInformation;

public class Order implements OrderInterface
{
  private int orderNumber;
  private Room room;
  private RoomInformation roomInformation;
  private BasicInformation basicInformation;
  private OptionServiceList userOptionServiceList;
  private TimeRangeList timeRangeList;
  private OrderState orderState;

  public Order(int orderNumber, RoomInterface room, RoomInformation roomInformation, BasicInformation basicInformation, OptionServiceList userOptionServiceList, TimeRangeList timeRangeList)
  {
    this.orderNumber = orderNumber;
    this.room = null;
    if (room!=null)
    {
      this.room = room.copy();
    }
    this.roomInformation = roomInformation;
    this.basicInformation = basicInformation;
    this.userOptionServiceList = userOptionServiceList;
    this.timeRangeList = timeRangeList;
    orderState = new OrderConfirm();
  }

  public Order(RoomInterface room, RoomInformation roomInformation, BasicInformation basicInformation, OptionServiceList userOptionServiceList, TimeRangeList timeRangeList)
  {
    this(-1, room, roomInformation, basicInformation, userOptionServiceList, timeRangeList);
  }

  public Order(RoomInformation roomInformation, BasicInformation basicInformation, OptionServiceList userOptionServiceList, TimeRangeList timeRangeList)
  {
    this(-1, null, roomInformation, basicInformation, userOptionServiceList, timeRangeList);
  }

  public int getOrderNumber()
  {
    return orderNumber;
  }

  public RoomInterface getRoom()
  {
    return room;
  }

  public RoomInformation getRoomInformation()
  {
    return roomInformation;
  }

  public BasicInformation getBasicInformation()
  {
    return basicInformation;
  }

  public OptionServiceList getUserOptionServiceList()
  {
    return userOptionServiceList;
  }

  public TimeRangeList getTimeRangeList()
  {
    return timeRangeList;
  }

  public OrderState getOrderState()
  {
    return orderState;
  }

  public void setTimeRangeList(TimeRangeList timeRangeList)
  {
    if (!orderState.getState().equals("Cancel"))
    {
      this.timeRangeList = timeRangeList;
    }
  }

  public void setOrderNumber(int orderNumber)
  {
    if (!orderState.getState().equals("Cancel"))
    {
      this.orderNumber = orderNumber;
    }
  }

  public void setRoom(RoomInterface room)
  {
    if (!orderState.getState().equals("Cancel")&&room!=null)
    {
      this.room = room.copy();
    }
  }

  public void setRoomInformation(RoomInformation roomInformation)
  {
    if (!orderState.getState().equals("Cancel"))
    {
      this.roomInformation = roomInformation;
    }
  }

  public void setBasicInformation(BasicInformation basicInformation)
  {
    if (!orderState.getState().equals("Cancel"))
    {
      this.basicInformation = basicInformation.copy();
    }
  }

  public void setUserOptionServiceList(OptionServiceList userOptionServiceList)
  {
    if (!orderState.getState().equals("Cancel"))
    {
      this.userOptionServiceList = userOptionServiceList;
    }
  }

  public void setOrderState(OrderState orderState)
  {
    if (!this.orderState.getState().equals("Cancel"))
    {
      this.orderState = orderState;
    }
  }

  public double getTotalPrice()
  {
    return roomInformation.getPrice() * timeRangeList.getSize() + userOptionServiceList.getTotalPrice();
  }

  public void cancelOrder()
  {
    orderState.changeState(this);
  }

  @Override public String toString()
  {
    if (orderState.getState().equals("Cancel"))
    {
      return "[" + orderNumber + "] - " + room.getType() + " : " + getTotalPrice() + " DKK [Cancelled]";
    }
    else
    {
      return "[" + orderNumber + "] - " + room.getType() + " : " + getTotalPrice() + " DKK";
    }
  }

  @Override public boolean equals(Object obj)
  {
    if (obj instanceof Order)
    {
      if (((Order) obj).getOrderNumber() == orderNumber)
      {
        return true;
      }
    }
    return false;
  }

  public Order copy()
  {
    Order other;
    if (room==null)
    {
      other = new Order(orderNumber,null,roomInformation.copy(),basicInformation.copy(),userOptionServiceList.copy(),timeRangeList.copy());
    }
    else
    {
      other = new Order(orderNumber,room.copy(),roomInformation.copy(),basicInformation.copy(),userOptionServiceList.copy(),timeRangeList.copy());
    }
    other.setOrderState(orderState.copy());
    return other;
  }
}
