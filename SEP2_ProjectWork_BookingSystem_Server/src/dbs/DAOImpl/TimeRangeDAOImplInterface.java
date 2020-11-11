package dbs.DAOImpl;

import model.domain.room.timeRange.TimeRangeInterface;

import java.sql.SQLException;

public interface TimeRangeDAOImplInterface
{
  void updateConnection(String url,String user,String password);
  void clear() throws SQLException;
  TimeRangeInterface create(int timeRangeID,TimeRangeInterface newTimeRange) throws SQLException;
  int readID(TimeRangeInterface timeRange) throws SQLException;
  TimeRangeInterface readByID(int timeRangeID) throws SQLException;
  void delete(int timeRangeID) throws SQLException;
}
