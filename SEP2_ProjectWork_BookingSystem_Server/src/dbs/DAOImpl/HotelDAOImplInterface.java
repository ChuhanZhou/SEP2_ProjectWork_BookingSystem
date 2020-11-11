package dbs.DAOImpl;

import model.domain.hotel.HotelInterface;

import java.sql.SQLException;

public interface HotelDAOImplInterface
{
  void updateConnection(String url,String user,String password);
  void clear() throws SQLException;
  HotelInterface create(HotelInterface newHotel) throws SQLException;
  HotelInterface read() throws SQLException;
  HotelInterface update(HotelInterface oldHotel,HotelInterface newHotel) throws SQLException;
  void delete(HotelInterface hotel) throws SQLException;
}
