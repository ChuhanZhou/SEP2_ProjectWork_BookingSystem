package dbs.DAOImpl;

import model.domain.hotel.*;
import org.postgresql.Driver;

import java.sql.*;

public class RoomInformationDAOImpl implements RoomInformationDAOImplInterface
{
  private static RoomInformationDAOImpl instance;
  private OptionServiceDAOImplInterface optionServiceDAOImpl;
  private String url;
  private String user;
  private String password;

  private RoomInformationDAOImpl(String url,String user,String password,OptionServiceDAOImplInterface optionServiceDAOImpl) throws
      SQLException
  {
    this.url = url;
    this.user = user;
    this.password = password;
    this.optionServiceDAOImpl = optionServiceDAOImpl;
    DriverManager.registerDriver(new Driver());
    checkTableAndDomain();
  }

  public static RoomInformationDAOImpl getInstance(String url,String user,String password,OptionServiceDAOImplInterface optionServiceDAOImpl)
  {
    if (instance==null)
    {
      try
      {
        instance = new RoomInformationDAOImpl(url,user,password,optionServiceDAOImpl);
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
    String[] tables = {"CREATE TABLE \"RoomInformation\"(\n"
        + "roomInformationId \"id\" NOT NULL,\n"
        + "roomType \"roomType\" NOT NULL,\n"
        + "price FLOAT NOT NULL,\n"
        + "facilities VARCHAR(100),\n"
        + "roomSize INT NOT NULL,\n"
        + "name \"Name\",\n"
        + "PRIMARY KEY(roomInformationId),\n"
        + "FOREIGN KEY(name)REFERENCES \"Hotel\"(name));",
        "CREATE TABLE \"OptionService_RoomInformation\"(\n"
        + "optionServiceId \"id\" NOT NULL,\n"
        + "roomInformationId \"id\" NOT NULL,\n"
        + "PRIMARY KEY(optionServiceId,roomInformationId),\n"
        + "FOREIGN KEY(optionServiceId)REFERENCES \"OptionService\"(optionServiceId),\n"
        + "FOREIGN KEY(roomInformationId)REFERENCES \"RoomInformation\"(roomInformationId));"};
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
        "DROP TABLE \"OptionService_RoomInformation\";",
        "DROP TABLE \"RoomInformation\";",
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
    optionServiceDAOImpl.clear();
    HotelDAOImpl.getInstance(url,user,password).clear();
  }

  @Override public RoomInformation create(int roomInformationID, RoomInformation newRoomInformation, HotelInterface hotel) throws SQLException
  {
    boolean check = false;
    Connection connection = getConnection();
    while (true)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO \"RoomInformation\"(roomInformationId,roomType,price,facilities,roomSize,name)VALUES(?,?,?,?,?,?);");
        statement.setInt(1,roomInformationID);
        statement.setString(2,newRoomInformation.getType());
        statement.setDouble(3,newRoomInformation.getPrice());
        statement.setString(4,newRoomInformation.getFacilities());
        statement.setInt(5,newRoomInformation.getSize());
        statement.setString(6,hotel.getName());
        statement.executeUpdate();
        for (int x=0;x<newRoomInformation.getSupplyForFree().getSize();x++)
        {
          statement = connection.prepareStatement("INSERT INTO \"OptionService_RoomInformation\"(optionServiceId,roomInformationId)VALUES(?,?);");
          statement.setInt(1,optionServiceDAOImpl.readID(newRoomInformation.getSupplyForFree().getOptionServiceByIndex(x),hotel));
          statement.setInt(2,roomInformationID);
          statement.executeUpdate();
        }
        break;
      }
      catch (SQLException e)
      {
        if (check)
        {
          System.out.println(e.getMessage());
          newRoomInformation = null;
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
    return newRoomInformation;
  }

  @Override public int readID(RoomInformation roomInformation, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    int id = -1;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT roomInformationId FROM \"RoomInformation\" WHERE (roomType = ? AND name = ?);");
      statement.setString(1,roomInformation.getType());
      statement.setString(2,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        id = resultSet.getInt("roomInformationId");
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return id;
  }

  @Override public RoomInformation readByID(int roomInformationID) throws SQLException
  {
    Connection connection = getConnection();
    RoomInformation roomInformation = null;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"RoomInformation\" WHERE (roomInformationId = ?);");
      statement.setInt(1,roomInformationID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        OptionServiceList optionServiceList = new OptionServiceList();
        PreparedStatement statement1 = connection.prepareStatement("SELECT optionServiceId FROM \"OptionService_RoomInformation\" WHERE (roomInformationId = ?);");
        statement1.setInt(1,roomInformationID);
        ResultSet resultSet1 = statement1.executeQuery();
        while (true)
        {
          if (resultSet1.next())
          {
            optionServiceList.addOptionService(optionServiceDAOImpl.readByID(resultSet1.getInt("optionServiceId")));
          }
          else
          {
            break;
          }
        }
        roomInformation = new RoomInformation(resultSet.getString("roomType"),resultSet.getDouble("price"),optionServiceList,resultSet.getString("facilities"),resultSet.getInt("roomSize"));
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return roomInformation;
  }

  @Override public RoomInformationList readByHotel(HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    RoomInformationList roomInformationList = new RoomInformationList();
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"RoomInformation\" WHERE (name = ?);");
      statement.setString(1,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      while (true)
      {
        if (resultSet.next())
        {
          OptionServiceList optionServiceList = new OptionServiceList();
          PreparedStatement statement1 = connection.prepareStatement("SELECT optionServiceId FROM \"OptionService_RoomInformation\" WHERE (roomInformationId = ?);");
          statement1.setInt(1,resultSet.getInt("roomInformationId"));
          ResultSet resultSet1 = statement1.executeQuery();
          while (true)
          {
            if (resultSet1.next())
            {
              optionServiceList.addOptionService(optionServiceDAOImpl.readByID(resultSet1.getInt("optionServiceId")));
            }
            else
            {
              break;
            }
          }
          roomInformationList.addNewRoomInformation(new RoomInformation(resultSet.getString("roomType"),resultSet.getDouble("price"),optionServiceList,resultSet.getString("facilities"),resultSet.getInt("roomSize")));
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
    return roomInformationList;
  }

  @Override public RoomInformation updateByID(int roomInformationID, RoomInformation newRoomInformation, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"RoomInformation\" SET roomType=?,price=?,facilities=?,roomSize=?,name=? WHERE roomInformationId=?;");
      statement.setString(1,newRoomInformation.getType());
      statement.setDouble(2,newRoomInformation.getPrice());
      statement.setString(3,newRoomInformation.getFacilities());
      statement.setInt(4,newRoomInformation.getSize());
      if (hotel!=null)
      {
        statement.setString(5,hotel.getName());
      }
      else
      {
        statement.setString(5,null);
      }
      statement.setInt(6,roomInformationID);
      statement.executeUpdate();
      if (hotel!=null)
      {
        statement = connection.prepareStatement("DELETE FROM \"OptionService_RoomInformation\" WHERE (roomInformationId=?);");
        statement.setInt(1,roomInformationID);
        statement.executeUpdate();

        for (int x=0;x<newRoomInformation.getSupplyForFree().getSize();x++)
        {
          statement = connection.prepareStatement("INSERT INTO \"OptionService_RoomInformation\"(optionServiceId,roomInformationId)VALUES(?,?);");
          statement.setInt(1,optionServiceDAOImpl.readID(newRoomInformation.getSupplyForFree().getOptionServiceByIndex(x),hotel));
          statement.setInt(2,roomInformationID);
          statement.executeUpdate();
        }
      }
    }
    catch (SQLException e)
    {
      newRoomInformation = null;
      System.out.println(e.getMessage());
    }
    connection.close();
    return newRoomInformation;
  }

  @Override public void updateHotelByHotel(HotelInterface oldHotel, HotelInterface newHotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"RoomInformation\" SET name=? WHERE name=?;");
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

  @Override public void delete(int roomInformationID) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Order\" WHERE (roomInformationId=?);");
      statement.setInt(1,roomInformationID);
      statement.executeUpdate();

      statement = connection.prepareStatement("DELETE FROM \"OptionService_RoomInformation\" WHERE (roomInformationId=?);");
      statement.setInt(1,roomInformationID);
      statement.executeUpdate();

      statement = connection.prepareStatement("DELETE FROM \"RoomInformation\" WHERE (roomInformationId=?);");
      statement.setInt(1,roomInformationID);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      //System.out.println(e.getMessage());
    }
    connection.close();
  }

  @Override public boolean checkBeUsed(int roomInformationID) throws SQLException
  {
    Connection connection = getConnection();
    boolean beUsed = false;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Order\" WHERE (roomInformationId=?);");
      statement.setInt(1,roomInformationID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        beUsed =  true;
      }
    }
    catch (SQLException e)
    {
      //e.printStackTrace();
    }
    connection.close();
    return beUsed;
  }
}
