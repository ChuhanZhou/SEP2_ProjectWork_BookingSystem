package dbs.DAOImpl;

import model.domain.RoomList;
import model.domain.hotel.HotelInterface;
import model.domain.room.RoomInterface;

import java.sql.SQLException;

public interface RoomDAOImplInterface
{
  void updateConnection(String url,String user,String password);
  void clear() throws SQLException;
  RoomInterface create(int roomID,RoomInterface newRoom, HotelInterface hotel) throws SQLException;
  int readID(RoomInterface room,HotelInterface hotel) throws SQLException;
  RoomInterface readByID(int roomID) throws SQLException;
  RoomList readByHotel(HotelInterface hotel) throws SQLException;
  RoomInterface updateByID(int roomID,RoomInterface newRoom, HotelInterface hotel) throws SQLException;
  void updateHotelByHotel(HotelInterface oldHotel,HotelInterface newHotel) throws SQLException;
  void delete(int roomID) throws SQLException;
  boolean checkBeUsed(int roomID) throws SQLException;
}
