package dbs.DAOImpl;

import model.domain.hotel.*;

import java.sql.SQLException;

public interface RoomInformationDAOImplInterface
{
  void updateConnection(String url,String user,String password);
  void clear() throws SQLException;
  RoomInformation create(int roomInformationID,RoomInformation newRoomInformation,HotelInterface hotel) throws SQLException;
  int readID(RoomInformation roomInformation,HotelInterface hotel) throws SQLException;
  RoomInformation readByID(int roomInformationID) throws SQLException;
  RoomInformationList readByHotel(HotelInterface hotel) throws SQLException;
  RoomInformation updateByID(int roomInformationID,RoomInformation newRoomInformation,HotelInterface hotel) throws SQLException;
  void updateHotelByHotel(HotelInterface oldHotel,HotelInterface newHotel) throws SQLException;
  void delete(int roomInformationID) throws SQLException;
  boolean checkBeUsed(int roomInformationID) throws SQLException;
}
