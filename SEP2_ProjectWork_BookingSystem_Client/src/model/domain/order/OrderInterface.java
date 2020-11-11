package model.domain.order;

import model.domain.hotel.OptionServiceList;
import model.domain.hotel.RoomInformation;
import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;
import model.domain.user.BasicInformation;

public interface OrderInterface
{
  int getOrderNumber();
  RoomInterface getRoom();
  RoomInformation getRoomInformation();
  BasicInformation getBasicInformation();
  OptionServiceList getUserOptionServiceList();
  TimeRangeList getTimeRangeList();
  OrderState getOrderState();
  void setTimeRangeList(TimeRangeList timeRangeList);
  void setOrderNumber(int orderNumber);
  void setRoom(RoomInterface room);
  void setRoomInformation(RoomInformation roomInformation);
  void setBasicInformation(BasicInformation basicInformation);
  void setUserOptionServiceList(OptionServiceList userOptionServiceList);
  void setOrderState(OrderState orderState);
  double getTotalPrice();
  void cancelOrder();
  Order copy();
}
