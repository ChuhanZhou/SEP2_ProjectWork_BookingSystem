package dbs.DAOImpl;

import model.domain.room.timeRange.Time;
import model.domain.room.timeRange.TimeRange;
import model.domain.room.timeRange.TimeRangeInterface;
import org.postgresql.Driver;

import java.sql.*;

public class TimeRangeDAOImpl implements TimeRangeDAOImplInterface
{
  private static TimeRangeDAOImpl instance;
  private String url;
  private String user;
  private String password;

  private TimeRangeDAOImpl(String url,String user,String password) throws
      SQLException
  {
    this.url = url;
    this.user = user;
    this.password = password;
    DriverManager.registerDriver(new Driver());
    checkTableAndDomain();
  }

  public void updateConnection(String url,String user,String password)
  {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  @Override public void clear() throws SQLException
  {
    Connection connection = getConnection();
    String[] domains = {
        "DROP TABLE \"TimeRange\";",
        "DROP DOMAIN \"gender\";",
        "DROP DOMAIN \"id\";",
        "DROP DOMAIN \"serviceType\";",
        "DROP DOMAIN \"orderNumber\";",
        "DROP DOMAIN \"email\";",
        "DROP DOMAIN \"hour\";",
        "DROP DOMAIN \"roomType\";",
        "DROP DOMAIN \"day\";",
        "DROP DOMAIN \"month\";",
        "DROP DOMAIN \"year\";",
        "DROP DOMAIN \"Name\";"};
    for (int x=0;x<domains.length;x++)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement(domains[x]);
        statement.executeUpdate();
      }
      catch (SQLException e)
      {
        //System.out.println(e.getMessage());
      }
    }
    connection.close();
  }

  public static TimeRangeDAOImpl getInstance(String url,String user,String password)
  {
    if (instance==null)
    {
      try
      {
        instance = new TimeRangeDAOImpl(url,user,password);
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return instance;
  }

  private void checkTableAndDomain() throws SQLException
  {
    Connection connection = getConnection();
    //try to create all domain
    String[] domains = {
        "CREATE DOMAIN \"year\" As INT CHECK(value>=1 AND value<=9999 );",
        "CREATE DOMAIN \"month\" AS INT CHECK(value>0 and value<=12);",
        "CREATE DOMAIN \"day\" AS INT CHECK(value>0 and value<32);",
        "CREATE DOMAIN \"roomType\" AS  VARCHAR(100);",
        "CREATE DOMAIN \"hour\" AS INT CHECK(value>=0 and value <24);",
        "CREATE DOMAIN \"email\" AS varchar(100);",
        "CREATE DOMAIN \"orderNumber\" AS Int CHECK(NOT NULL);",
        "CREATE DOMAIN \"serviceType\" AS varchar(100);",
        "CREATE DOMAIN \"id\" AS INT;",
        "CREATE DOMAIN \"gender\" AS VARCHAR(5)CHECK(value IN ('F','M',''));",
        "CREATE DOMAIN \"Name\" VARCHAR(40);"};
    for (int x=0;x<domains.length;x++)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement(domains[x]);
        statement.executeUpdate();
      }
      catch (SQLException e)
      {
        //System.out.println(e.getMessage());
      }
    }
    //try to create table
    try
    {
      PreparedStatement statement = connection.prepareStatement("CREATE TABLE \"TimeRange\"(\n"
          + "timeRangeID \"id\" NOT NULL,\n"
          + "checkinYear \"year\" NOT NULL,\n"
          + "checkinMonth \"month\" NOT NULL,\n"
          + "checkinDay \"day\" NOT NULL,\n"
          + "checkinHour \"hour\" NOT NULL,\n"
          + "checkoutYear \"year\" NOT NULL,\n"
          + "checkoutMonth \"month\" NOT NULL,\n"
          + "checkoutDay \"day\" NOT NULL,\n"
          + "checkoutHour \"hour\" NOT NULL,\n"
          + "PRIMARY KEY(timeRangeID));");
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      //System.out.println(e.getMessage());
    }
    connection.close();
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(url,user,password);
  }

  @Override public TimeRangeInterface create(int timeRangeID, TimeRangeInterface newTimeRange) throws SQLException
  {
    boolean check = false;
    Connection connection = getConnection();
    while (true)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO \"TimeRange\"(timeRangeID,checkinYear,checkinMonth,checkinDay,checkinHour,checkoutYear,checkoutMonth,checkoutDay,checkoutHour)VALUES(?,?,?,?,?,?,?,?,?);");
        statement.setInt(1,timeRangeID);
        statement.setInt(2,newTimeRange.getStartTime().getDate().getYear());
        statement.setInt(3,newTimeRange.getStartTime().getDate().getMonth());
        statement.setInt(4,newTimeRange.getStartTime().getDate().getDay());
        statement.setInt(5,newTimeRange.getStartTime().getClock().getHour());
        statement.setInt(6,newTimeRange.getEndTime().getDate().getYear());
        statement.setInt(7,newTimeRange.getEndTime().getDate().getMonth());
        statement.setInt(8,newTimeRange.getEndTime().getDate().getDay());
        statement.setInt(9,newTimeRange.getEndTime().getClock().getHour());
        statement.executeUpdate();
        break;
      }
      catch (SQLException e)
      {
        if (check)
        {
          System.out.println(e.getMessage());
          newTimeRange = null;
          break;
        }
        else
        {
          checkTableAndDomain();
          check = true;
        }
      }
    }
    connection.close();
    return newTimeRange;
  }

  @Override public int readID(TimeRangeInterface timeRange) throws SQLException
  {
    Connection connection = getConnection();
    int id = -1;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT timeRangeID FROM \"TimeRange\" WHERE (checkinYear = ? AND checkinMonth = ? AND checkinDay = ? AND checkinHour = ? AND checkoutYear = ? AND checkoutMonth = ? AND checkoutDay = ? AND checkoutHour = ?);");
      statement.setInt(1,timeRange.getStartTime().getDate().getYear());
      statement.setInt(2,timeRange.getStartTime().getDate().getMonth());
      statement.setInt(3,timeRange.getStartTime().getDate().getDay());
      statement.setInt(4,timeRange.getStartTime().getClock().getHour());
      statement.setInt(5,timeRange.getEndTime().getDate().getYear());
      statement.setInt(6,timeRange.getEndTime().getDate().getMonth());
      statement.setInt(7,timeRange.getEndTime().getDate().getDay());
      statement.setInt(8,timeRange.getEndTime().getClock().getHour());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        id = resultSet.getInt("timeRangeID");
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return id;
  }

  @Override public TimeRangeInterface readByID(int timeRangeID) throws SQLException
  {
    Connection connection = getConnection();
    TimeRangeInterface timeRange = null;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"TimeRange\" WHERE (timeRangeID = ?);");
      statement.setInt(1,timeRangeID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        Time checkInTime = new Time(resultSet.getInt("checkinDay"),resultSet.getInt("checkinMonth"),resultSet.getInt("checkinYear"),resultSet.getInt("checkinHour"),0);
        Time checkOutTime = new Time(resultSet.getInt("checkoutDay"),resultSet.getInt("checkoutMonth"),resultSet.getInt("checkoutYear"),resultSet.getInt("checkoutHour"),0);
        timeRange = new TimeRange(checkInTime,checkOutTime);
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return timeRange;
  }

  @Override public void delete(int timeRangeID) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM \"TimeRange\" WHERE (timeRangeId=?);");
      statement.setInt(1,timeRangeID);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      //System.out.println(e.getMessage());
    }
    connection.close();
  }
}
