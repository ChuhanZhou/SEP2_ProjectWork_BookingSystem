package dbs;

import model.domain.OrderList;
import model.domain.RoomList;
import model.domain.UserList;
import model.domain.hotel.*;
import model.domain.order.OrderInterface;
import model.domain.room.RoomInterface;
import model.domain.user.UserInterface;

import java.sql.SQLException;

public interface DAOModel
{
  void clear();
  boolean haveData();
  HotelInterface addHotel(HotelInterface hotel);
  HotelInterface getHotel();
  HotelInterface updateHotel(HotelInterface oldHotel,HotelInterface newHotel);

  OptionService addOptionService(OptionService newOptionService);
  OptionServiceList getAllOptionService();
  OptionService updateOptionService(OptionService oldOptionService,OptionService newOptionService);
  void removeOptionService(OptionService optionService);

  RoomInformation addRoomInformation(RoomInformation newRoomInformation);
  RoomInformationList getAllRoomInformation();
  RoomInformation updateRoomInformation(RoomInformation oldRoomInformation,RoomInformation newRoomInformation);
  void removeRoomInformation(RoomInformation roomInformation);

  RoomInterface addRoom(RoomInterface newRoom);
  RoomList getAllRoom();
  RoomInterface updateRoom(RoomInterface oldRoom,RoomInterface newRoom);
  void removeRoom(RoomInterface room);

  UserInterface addUser(UserInterface newUser);
  UserList getAllUser();
  UserInterface updateUser(UserInterface oldUser,UserInterface newUser);
  void removeUser(UserInterface user);

  OrderInterface addOrder(OrderInterface newOrder);
  OrderList getAllOrder();
  OrderInterface updateOrder(OrderInterface newOrder);
  void removeOrder(OrderInterface order);
}
