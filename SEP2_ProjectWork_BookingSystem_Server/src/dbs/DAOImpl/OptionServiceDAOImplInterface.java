package dbs.DAOImpl;

import model.domain.hotel.HotelInterface;
import model.domain.hotel.OptionService;
import model.domain.hotel.OptionServiceList;

import java.sql.SQLException;

public interface OptionServiceDAOImplInterface
{
  void updateConnection(String url,String user,String password);
  void clear() throws SQLException;
  OptionService create(int optionServiceID,OptionService newOptionService,HotelInterface hotel) throws SQLException;
  int readID(OptionService optionService,HotelInterface hotel) throws SQLException;
  OptionService readByID(int optionServiceID) throws SQLException;
  OptionServiceList readByHotel(HotelInterface hotel) throws SQLException;
  OptionService updateByID(int optionServiceID,OptionService newOptionService,HotelInterface hotel) throws SQLException;
  void updateHotelByHotel(HotelInterface oldHotel,HotelInterface newHotel) throws SQLException;
  void delete(int optionServiceID) throws SQLException;
  boolean checkBeUsed(int optionServiceID) throws SQLException;
}
