package dbs.DAOImpl;

import model.domain.hotel.HotelInterface;
import model.domain.hotel.OptionService;
import model.domain.hotel.OptionServiceList;
import model.domain.user.UserInterface;
import org.postgresql.Driver;

import java.sql.*;

public class OptionServiceDAOImpl implements OptionServiceDAOImplInterface
{
  private static OptionServiceDAOImpl instance;
  private String url;
  private String user;
  private String password;

  private OptionServiceDAOImpl(String url,String user,String password) throws SQLException
  {
    this.url = url;
    this.user = user;
    this.password = password;
    DriverManager.registerDriver(new Driver());
    checkTableAndDomain();
  }

  public static OptionServiceDAOImpl getInstance(String url,String user,String password)
  {
    if (instance==null)
    {
      try
      {
        instance = new OptionServiceDAOImpl(url,user,password);
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
      PreparedStatement statement = connection.prepareStatement("CREATE TABLE \"OptionService\"(\n"
          + "optionServiceId \"id\" NOT NULL,\n"
          + "serviceType \"serviceType\" NOT NULL,\n"
          + "price FLOAT NOT NULL,\n"
          + "name \"Name\",\n"
          + "PRIMARY KEY(optionServiceId),\n"
          + "FOREIGN KEY(name)REFERENCES \"Hotel\"(name));");
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
        "DROP TABLE \"OptionService\";",
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
    HotelDAOImpl.getInstance(url,user,password).clear();
  }

  @Override public OptionService create(int optionServiceID, OptionService newOptionService, HotelInterface hotel) throws SQLException
  {
    boolean check = false;
    Connection connection = getConnection();
    while (true)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO \"OptionService\"(optionServiceId,serviceType,price,name)VALUES(?,?,?,?);");
        statement.setInt(1,optionServiceID);
        statement.setString(2,newOptionService.getServiceType());
        statement.setDouble(3,newOptionService.getPrice());
        statement.setString(4,hotel.getName());
        statement.executeUpdate();
        break;
      }
      catch (SQLException e)
      {
        if (check)
        {
          System.out.println(e.getMessage());
          newOptionService = null;
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
    return newOptionService;
  }

  @Override public int readID(OptionService optionService, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    int id = -1;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT optionServiceId FROM \"OptionService\" WHERE (serviceType = ? AND price = ? AND name = ?);");
      statement.setString(1,optionService.getServiceType());
      statement.setDouble(2,optionService.getPrice());
      statement.setString(3,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        id = resultSet.getInt("optionServiceId");
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return id;
  }

  @Override public OptionService readByID(int optionServiceID) throws SQLException
  {
    Connection connection = getConnection();
    OptionService optionService = null;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"OptionService\" WHERE (optionServiceId = ?);");
      statement.setInt(1,optionServiceID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        optionService = new OptionService(resultSet.getString("serviceType"),resultSet.getDouble("price"));
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return optionService;
  }

  @Override public OptionServiceList readByHotel(HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    OptionServiceList optionServiceList = new OptionServiceList();
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"OptionService\" WHERE (name = ?);");
      statement.setString(1,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      while (true)
      {
        if (resultSet.next())
        {
          optionServiceList.addOptionService(new OptionService(resultSet.getString("serviceType"),resultSet.getDouble("price")));
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
    return optionServiceList;
  }

  @Override public OptionService updateByID(int optionServiceID,OptionService newOptionService, HotelInterface hotel)
      throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"OptionService\" SET serviceType=?,price=?,name=? WHERE optionServiceId=?;");
      statement.setString(1,newOptionService.getServiceType());
      statement.setDouble(2,newOptionService.getPrice());
      if (hotel!=null)
      {
        statement.setString(3,hotel.getName());
      }
      else
      {
        statement.setString(3,null);
      }
      statement.setInt(4,optionServiceID);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      newOptionService = null;
      System.out.println(e.getMessage());
    }
    connection.close();
    return newOptionService;
  }

  @Override public void updateHotelByHotel(HotelInterface oldHotel, HotelInterface newHotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"OptionService\" SET name=? WHERE name=?;");
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

  @Override public void delete(int optionServiceID) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Order_OptionService\" WHERE (optionServiceId=?);");
      statement.setInt(1,optionServiceID);
      statement.executeUpdate();

      statement = connection.prepareStatement("DELETE FROM \"OptionService_RoomInformation\" WHERE (optionServiceId=?);");
      statement.setInt(1,optionServiceID);
      statement.executeUpdate();

      statement = connection.prepareStatement("DELETE FROM \"OptionService\" WHERE (optionServiceId=?);");
      statement.setInt(1,optionServiceID);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
  }

  @Override public boolean checkBeUsed(int optionServiceID) throws SQLException
  {
    Connection connection = getConnection();
    boolean beUsed = false;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Order_OptionService\" WHERE (optionServiceId=?);");
      statement.setInt(1,optionServiceID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        beUsed =  true;
      }

      statement = connection.prepareStatement("SELECT * FROM \"OptionService_RoomInformation\" WHERE (optionServiceId=?);");
      statement.setInt(1,optionServiceID);
      resultSet = statement.executeQuery();
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
