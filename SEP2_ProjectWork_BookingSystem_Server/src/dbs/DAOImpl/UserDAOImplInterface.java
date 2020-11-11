package dbs.DAOImpl;

import model.domain.UserList;
import model.domain.hotel.HotelInterface;
import model.domain.user.UserInterface;

import java.sql.SQLException;

public interface UserDAOImplInterface
{
  void updateConnection(String url,String user,String password);
  void clear() throws SQLException;
  UserInterface create(int userID,UserInterface newUser,HotelInterface hotel) throws SQLException;
  int readID(UserInterface user,HotelInterface hotel) throws SQLException;
  int readID(String email,HotelInterface hotel) throws SQLException;
  UserInterface readByID(int userID) throws SQLException;
  UserList readByHotel(HotelInterface hotel) throws SQLException;
  UserInterface updateByID(int userID,UserInterface newUser,HotelInterface hotel) throws SQLException;
  void updateHotelByHotel(HotelInterface oldHotel,HotelInterface newHotel) throws SQLException;
  void delete(int userID) throws SQLException;
  boolean checkBeUsed(int userID) throws SQLException;
}
