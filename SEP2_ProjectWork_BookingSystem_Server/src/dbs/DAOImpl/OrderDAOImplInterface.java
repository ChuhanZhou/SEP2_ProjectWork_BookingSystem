package dbs.DAOImpl;

import model.domain.OrderList;
import model.domain.RoomList;
import model.domain.hotel.HotelInterface;
import model.domain.order.OrderInterface;
import model.domain.room.RoomInterface;

import java.sql.SQLException;

public interface OrderDAOImplInterface
{
  void updateConnection(String url,String user,String password);
  void clear() throws SQLException;
  OrderInterface create(int orderNumber,OrderInterface newOrder, HotelInterface hotel) throws SQLException;
  OrderInterface readByOrderNumber(int orderNumber) throws SQLException;
  OrderList readByHotel(HotelInterface hotel) throws SQLException;
  OrderInterface updateByOrderNumber(int orderNumber,OrderInterface newOrder, HotelInterface hotel) throws SQLException;
  void updateHotelByHotel(HotelInterface oldHotel,HotelInterface newHotel) throws SQLException;
  void delete(int orderNumber) throws SQLException;
  boolean checkBeUsed(int orderNumber) throws SQLException;
}
