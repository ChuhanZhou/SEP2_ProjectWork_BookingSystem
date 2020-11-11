package model;

import model.domain.OrderList;
import model.domain.error.ErrorInterface;
import model.domain.hotel.*;
import model.domain.order.OrderInterface;
import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;
import model.domain.room.timeRange.TimeRangeInterface;
import model.domain.user.AccountInformation;
import model.domain.user.UserInterface;
import utility.NamedPropertyChangeSubject;

public interface BookingModel extends NamedPropertyChangeSubject
{
  boolean connectToServer(int port,String host);
  void login(String action,UserInterface user);
  void signOut();
  void sendUserPackage(UserInterface user);
  void sendOrderPackage(OrderInterface order);
  void sendSearchPackage(RoomInformationList roomInformationList, TimeRangeList timeRangeList);
  void updateError(String error);
  UserInterface getUser();
  void updateUser(UserInterface user);
  ErrorInterface addNewOrder(OrderInterface newOrder);
  ErrorInterface updateOrder(OrderInterface newOrder);
  OrderInterface getOrderByIndex(int index);
  OrderInterface getOrder(int orderNumber);
  OrderList getOrders(UserInterface user);
  OrderList getOrders(RoomInterface room);
  OrderList getOrders(RoomInformation roomInformation);
  OrderList getOrders(TimeRangeInterface timeRange);
  OrderList getOrders(TimeRangeList timeRangeList);
  void cancelOrderByIndex(int index);
  void cancelOrder(int orderNumber);
  void cancelOrders(UserInterface user);
  void cancelOrders(RoomInterface room);
  void cancelOrders(RoomInformation roomInformation);
  void cancelOrders(TimeRangeInterface timeRange);
  void updateHotel(HotelInterface hotel);
  HotelInterface getHotel();
  RoomInformationList getRoomInformationList();
  RoomInformation getRoomInformationByIndex(int index);
  ErrorInterface addOptionService(OptionService optionService);
  ErrorInterface updateOptionService(OptionService oldOptionService,OptionService newOptionService);
  void removeOptionService(OptionService optionService);
  void removeOptionService(int index);
  void removeOptionService(String serviceType);
  OptionServiceList getOptionServiceList();
  OptionService getOptionServiceByIndex(int index);
  OptionService getOptionServiceByServiceType(String serviceType);
  void searchRoomInformationListFromServer(RoomInformationList roomInformationList,TimeRangeList timeRangeList);
  void updateSearchInformation(RoomInformationList roomInformationList);
}
