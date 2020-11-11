package dbs.DAOImpl;

import dbs.DAOModelManager;
import model.domain.RoomList;
import model.domain.hotel.HotelInterface;
import model.domain.room.Room;
import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;
import org.postgresql.Driver;

import java.sql.*;

public class RoomDAOImpl implements RoomDAOImplInterface
{
  private static RoomDAOImpl instance;
  private TimeRangeDAOImplInterface timeRangeDAOImpl;
  private String url;
  private String user;
  private String password;

  private RoomDAOImpl(String url,String user,String password,TimeRangeDAOImplInterface timeRangeDAOImpl) throws
      SQLException
  {
    this.url = url;
    this.user = user;
    this.password = password;
    this.timeRangeDAOImpl = timeRangeDAOImpl;
    DriverManager.registerDriver(new Driver());
    checkTableAndDomain();
  }

  public static RoomDAOImpl getInstance(String url,String user,String password,TimeRangeDAOImplInterface timeRangeDAOImpl)
  {
    if (instance==null)
    {
      try
      {
        instance = new RoomDAOImpl(url,user,password,timeRangeDAOImpl);
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
    //try to create all table
    String[] tables = {"CREATE TABLE \"Room\"(\n"
        + "roomId \"id\" NOT NULL,\n"
        + "roomNumber INT NOT NULL,\n"
        + "roomType \"roomType\" NOT NULL,\n"
        + "name \"Name\",\n"
        + "PRIMARY KEY(roomId),\n"
        + "FOREIGN KEY(name)REFERENCES \"Hotel\"(name));",
        "CREATE TABLE \"TimeRange_Room\"(\n"
            + "timeRangeID \"id\" NOT NULL,\n"
            + "roomId \"id\" NOT NULL,\n"
            + "PRIMARY KEY(timeRangeID,roomId),\n"
            + "FOREIGN KEY(timeRangeID)REFERENCES \"TimeRange\"(timeRangeID),\n"
            + "FOREIGN KEY(roomId)REFERENCES \"Room\"(roomId));"};
    for (int x=0;x<tables.length;x++)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement(tables[x]);
        statement.executeUpdate();
      }
      catch (SQLException e)
      {
        //System.out.println(e.getMessage());
      }
    }
    connection.close();
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(url,user,password);
  }

  @Override public void updateConnection(String url, String user, String password)
  {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  @Override public void clear() throws SQLException
  {
    Connection connection = getConnection();
    String[] domains = {
        "DROP TABLE \"TimeRange_Room\";",
        "DROP TABLE \"Room\";",
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
    timeRangeDAOImpl.clear();
    HotelDAOImpl.getInstance(url,user,password).clear();
  }

  @Override public RoomInterface create(int roomID, RoomInterface newRoom, HotelInterface hotel) throws SQLException
  {
    boolean check = false;
    Connection connection = getConnection();
    while (true)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Room\"(roomId,roomNumber,roomType,name)VALUES(?,?,?,?);");
        statement.setInt(1,roomID);
        statement.setInt(2,newRoom.getRoomNumber());
        statement.setString(3,newRoom.getType());
        statement.setString(4,hotel.getName());
        statement.executeUpdate();
        for (int x=0;x<newRoom.getBookedTimeRangeList().getSize();x++)
        {
          if (timeRangeDAOImpl.readID(newRoom.getBookedTimeRangeList().getTimeRangeByIndex(x))==-1)
          {
            while (timeRangeDAOImpl.create(DAOModelManager.getRandomID(),newRoom.getBookedTimeRangeList().getTimeRangeByIndex(x))==null);
          }

          statement = connection.prepareStatement("INSERT INTO \"TimeRange_Room\"(roomId,timeRangeID)VALUES(?,?);");
          statement.setInt(1,roomID);
          statement.setInt(2,timeRangeDAOImpl.readID(newRoom.getBookedTimeRangeList().getTimeRangeByIndex(x)));
          statement.executeUpdate();
        }
        break;
      }
      catch (SQLException e)
      {
        if (check)
        {
          System.out.println(e.getMessage());
          newRoom = null;
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
    return newRoom;
  }

  @Override public int readID(RoomInterface room, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    int id = -1;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT roomId FROM \"Room\" WHERE (roomNumber = ? AND roomType = ? AND name = ?);");
      statement.setInt(1,room.getRoomNumber());
      statement.setString(2,room.getType());
      if (hotel!=null)
      {
        statement.setString(3,hotel.getName());
      }
      else
      {
        statement.setString(3,null);
      }
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        id = resultSet.getInt("roomId");
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return id;
  }

  @Override public RoomInterface readByID(int roomID) throws SQLException
  {
    Connection connection = getConnection();
    RoomInterface room = null;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Room\" WHERE (roomId = ?);");
      statement.setInt(1,roomID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        TimeRangeList timeRangeList = new TimeRangeList();
        PreparedStatement statement1 = connection.prepareStatement("SELECT timeRangeID FROM \"TimeRange_Room\" WHERE (roomId = ?);");
        statement1.setInt(1,roomID);
        ResultSet resultSet1 = statement1.executeQuery();
        while (true)
        {
          if (resultSet1.next())
          {
            timeRangeList.addTimeRange(timeRangeDAOImpl.readByID(resultSet1.getInt("timeRangeID")));
          }
          else
          {
            break;
          }
        }
        room = new Room(resultSet.getInt("roomNumber"),resultSet.getString("roomType"));
        room.setBookedTimeRangeList(timeRangeList);
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return room;
  }

  @Override public RoomList readByHotel(HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    RoomList roomList = new RoomList();
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Room\" WHERE (name = ?);");
      statement.setString(1,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      while (true)
      {
        if (resultSet.next())
        {
          TimeRangeList timeRangeList = new TimeRangeList();
          PreparedStatement statement1 = connection.prepareStatement("SELECT timeRangeID FROM \"TimeRange_Room\" WHERE (roomId = ?);");
          statement1.setInt(1,resultSet.getInt("roomId"));
          ResultSet resultSet1 = statement1.executeQuery();
          while (true)
          {
            if (resultSet1.next())
            {
              timeRangeList.addTimeRange(timeRangeDAOImpl.readByID(resultSet1.getInt("timeRangeID")));
            }
            else
            {
              break;
            }
          }
          Room room = new Room(resultSet.getInt("roomNumber"),resultSet.getString("roomType"));
          room.setBookedTimeRangeList(timeRangeList);
          roomList.addNewRoom(room);
        }
        else
        {
          break;
        }
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return roomList;
  }

  @Override public RoomInterface updateByID(int roomID, RoomInterface newRoom, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement;
      if (hotel!=null)
      {
        statement = connection.prepareStatement("UPDATE \"Room\" SET roomNumber=?,roomType=?,name=? WHERE roomId=?;");
        statement.setInt(1,newRoom.getRoomNumber());
        statement.setString(2,newRoom.getType());
        statement.setString(3,hotel.getName());
        statement.setInt(4,roomID);
        statement.executeUpdate();
      }
      else
      {
        statement = connection.prepareStatement("UPDATE \"Room\" SET name=? WHERE roomId=?;");
        statement.setString(1,null);
        statement.setInt(2,roomID);
        statement.executeUpdate();
      }

      statement = connection.prepareStatement("DELETE FROM \"TimeRange_Room\" WHERE (roomId=?);");
      statement.setInt(1,roomID);
      statement.executeUpdate();

      for (int x=0;x<newRoom.getBookedTimeRangeList().getSize();x++)
      {
        if (timeRangeDAOImpl.readID(newRoom.getBookedTimeRangeList().getTimeRangeByIndex(x))==-1)
        {
          while (timeRangeDAOImpl.create(DAOModelManager.getRandomID(),newRoom.getBookedTimeRangeList().getTimeRangeByIndex(x))==null);
        }

        statement = connection.prepareStatement("INSERT INTO \"TimeRange_Room\"(roomId,timeRangeID)VALUES(?,?);");
        statement.setInt(1,roomID);
        statement.setInt(2,timeRangeDAOImpl.readID(newRoom.getBookedTimeRangeList().getTimeRangeByIndex(x)));
        statement.executeUpdate();
      }
    }
    catch (SQLException e)
    {
      newRoom = null;
      System.out.println(e.getMessage());
    }
    connection.close();
    return newRoom;
  }

  @Override public void updateHotelByHotel(HotelInterface oldHotel, HotelInterface newHotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"Room\" SET name=? WHERE name=?;");
      if (newHotel!=null)
      {
        statement.setString(1,newHotel.getName());
      }
      else
      {
        statement.setString(1,null);
      }
      if (oldHotel!=null)
      {
        statement.setString(2,oldHotel.getName());
      }
      else
      {
        statement.setString(2,null);
      }
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
  }

  @Override public void delete(int roomID) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM \"TimeRange_Room\" WHERE (roomId=?);");
      statement.setInt(1,roomID);
      statement.executeUpdate();

      statement = connection.prepareStatement("DELETE FROM \"Room\" WHERE (roomId=?);");
      statement.setInt(1,roomID);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      //System.out.println(e.getMessage());
    }
    connection.close();
  }

  @Override public boolean checkBeUsed(int roomID) throws SQLException
  {
    Connection connection = getConnection();
    boolean beUsed = false;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Order\" WHERE (roomId=?);");
      statement.setInt(1,roomID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        beUsed =  true;
      }

      //statement = connection.prepareStatement("SELECT * FROM \"TimeRange_Room\" WHERE (roomId=?);");
      //statement.setInt(1,roomID);
      //resultSet = statement.executeQuery();
      //if (resultSet.next())
      //{
      //  beUsed =  true;
      //}
    }
    catch (SQLException e)
    {
      //e.printStackTrace();
    }
    connection.close();
    return beUsed;
  }
}
