package dbs.DAOImpl;

import model.domain.hotel.Hotel;
import model.domain.hotel.HotelInterface;
import org.postgresql.Driver;

import java.sql.*;

public class HotelDAOImpl implements HotelDAOImplInterface
{
  private static HotelDAOImpl instance;
  private RoomInformationDAOImplInterface roomInformationDAOImpl;
  private OptionServiceDAOImplInterface optionServiceDAOImpl;
  private String url;
  private String user;
  private String password;

  private HotelDAOImpl(String url,String user,String password) throws SQLException
  {
    this.url = url;
    this.user = user;
    this.password = password;
    DriverManager.registerDriver(new Driver());
    checkTableAndDomain();
    optionServiceDAOImpl = OptionServiceDAOImpl.getInstance(url,user,password);
    roomInformationDAOImpl = RoomInformationDAOImpl.getInstance(url,user,password,optionServiceDAOImpl);
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
        "DROP TABLE \"Hotel\";",
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

  public static HotelDAOImpl getInstance(String url,String user,String password)
  {
    if (instance==null)
    {
      try
      {
        instance = new HotelDAOImpl(url,user,password);
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
      PreparedStatement statement = connection.prepareStatement("CREATE TABLE \"Hotel\"(\n"
          + "name Name,\n"
          + "address VARCHAR(100),\n"
          + "contactNumber VARCHAR(50),\n"
          + "characteristics VARCHAR(100),\n"
          + "description VARCHAR(100),\n"
          + "checkinHour hour NOT NULL,\n"
          + "checkoutHour hour NOT NULL,\n"
          + "facilities VARCHAR(100),\n"
          + "rules VARCHAR(100),\n"
          + "PRIMARY KEY(name));");
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

  @Override public HotelInterface create(HotelInterface newHotel) throws SQLException
  {
    boolean check = false;
    Connection connection = getConnection();
    while (true)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Hotel\"(name,address,contactNumber,characteristics,description,checkinHour,checkoutHour,facilities,rules)VALUES(?,?,?,?,?,?,?,?,?);");
        statement.setString(1,newHotel.getName());
        statement.setString(2,newHotel.getAddress());
        statement.setString(3,newHotel.getContactNumber());
        statement.setString(4,newHotel.getCharacteristic());
        statement.setString(5,newHotel.getDescription());
        statement.setInt(6,newHotel.getCheckInHour());
        statement.setInt(7,newHotel.getCheckOutHour());
        statement.setString(8,newHotel.getFacilities());
        statement.setString(9,newHotel.getRules());
        statement.executeUpdate();

        //for (int x=0;x<newHotel.getOptionServiceList().getSize();x++)
        //{
        //  while (optionServiceDAOImpl.create(DAOModelManager.getRandomID(),newHotel.getOptionServiceList().getOptionServiceByIndex(x),newHotel)==null);
        //}
//
        //for (int x=0;x<newHotel.getRoomInformationList().getSize();x++)
        //{
        //  while (roomInformationDAOImpl.create(DAOModelManager.getRandomID(),newHotel.getRoomInformationList().getRoomInformationByIndex(x),newHotel)==null);
        //}

        break;
      }
      catch (SQLException e)
      {
        if (check)
        {
          //System.out.println(e.getMessage());
          newHotel = null;
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
    return newHotel;
  }

  @Override public HotelInterface read() throws SQLException
  {
    Connection connection = getConnection();
    Hotel hotel = null;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Hotel\" ;");
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        hotel = Hotel.getHotel().copy();
        hotel.setName(resultSet.getString("name"));
        hotel.setAddress(resultSet.getString("address"));
        hotel.setContactNumber(resultSet.getString("contactNumber"));
        hotel.setCharacteristic(resultSet.getString("characteristics"));
        hotel.setFacilities(resultSet.getString("facilities"));
        hotel.setDescription(resultSet.getString("description"));
        hotel.setRules(resultSet.getString("rules"));
        hotel.setCheckInHour(resultSet.getInt("checkinHour"));
        hotel.setCheckOutHour(resultSet.getInt("checkoutHour"));
        hotel.setRoomInformationList(roomInformationDAOImpl.readByHotel(hotel.copy()));
        hotel.setOptionServiceList(optionServiceDAOImpl.readByHotel(hotel.copy()));
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return hotel;
  }

  @Override public HotelInterface update(HotelInterface oldHotel,HotelInterface newHotel)throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"Hotel\" SET name=?,address=?,contactNumber=?,characteristics=?,description=?,checkinHour=?,checkoutHour=?,facilities=?,rules=? WHERE name=?;");
      statement.setString(1,newHotel.getName());
      statement.setString(2,newHotel.getAddress());
      statement.setString(3,newHotel.getContactNumber());
      statement.setString(4,newHotel.getCharacteristic());
      statement.setString(5,newHotel.getDescription());
      statement.setInt(6,newHotel.getCheckInHour());
      statement.setInt(7,newHotel.getCheckOutHour());
      statement.setString(8,newHotel.getFacilities());
      statement.setString(9,newHotel.getRules());
      statement.setString(10,oldHotel.getName());
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      newHotel = null;
      System.out.println(e.getMessage());
    }
    connection.close();
    return newHotel;
  }

  @Override public void delete(HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Hotel\" WHERE (name=?);");
      statement.setString(1,hotel.getName());
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      //System.out.println(e.getMessage());
    }
    connection.close();
  }
}
