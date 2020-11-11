package dbs.DAOImpl;

import model.domain.UserList;
import model.domain.hotel.HotelInterface;
import model.domain.user.User;
import model.domain.user.UserInterface;
import org.postgresql.Driver;

import java.sql.*;

public class UserDAOImpl implements UserDAOImplInterface
{
  private static UserDAOImpl instance;
  private String url;
  private String user;
  private String password;

  private UserDAOImpl(String url,String user,String password) throws SQLException
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
        "DROP TABLE \"User\";",
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

  public static UserDAOImpl getInstance(String url,String user,String password)
  {
    if (instance==null)
    {
      try
      {
        instance = new UserDAOImpl(url,user,password);
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(url,user,password);
  }

  private void checkTableAndDomain() throws SQLException
  {
    //try to create all domain
    Connection connection = getConnection();
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
      }
    }
    //try to create table
    try
    {
      PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE \"User\"(\n"
          + "userId \"id\",\n"
          + "email \"email\",\n"
          + "password varchar(50),\n"
          + "firstName \"Name\",\n"
          + "lastName \"Name\",\n"
          + "nationality varchar(50),\n"
          + "phoneNumber varchar(50),\n"
          + "gender \"gender\",\n"
          + "name \"Name\",\n"
          + "PRIMARY KEY(userId),\n"
          + "FOREIGN KEY(name)REFERENCES \"Hotel\"(name));");
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
    }
    connection.close();
  }

  @Override public UserInterface create(int userID,UserInterface newUser,HotelInterface hotel) throws SQLException
  {
    boolean check = false;
    Connection connection = getConnection();
    while (true)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO \"User\"(userId,email,password,firstName,lastName,nationality,phoneNumber,gender,name)VALUES(?,?,?,?,?,?,?,?,?);");
        statement.setInt(1,userID);
        statement.setString(2,newUser.getAccountInformation().getEmail());
        statement.setString(3,newUser.getAccountInformation().getPassword());
        statement.setString(4,newUser.getBasicInformation().getFirstName());
        statement.setString(5,newUser.getBasicInformation().getLastName());
        statement.setString(6,newUser.getBasicInformation().getNationality());
        statement.setString(7,newUser.getBasicInformation().getPhoneNumber());
        statement.setString(8,newUser.getBasicInformation().getGender());
        statement.setString(9,hotel.getName());
        statement.executeUpdate();
        break;
      }
      catch (SQLException e)
      {
        if (check)
        {
          System.out.println(e.getMessage());
          newUser = null;
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
    return newUser;
  }

  @Override public int readID(UserInterface user, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    int id = -1;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT userId FROM \"User\" WHERE (email = ? AND password = ? AND firstName = ? AND lastName = ? AND nationality = ? AND phoneNumber = ? AND gender = ? AND name  = ?);");
      statement.setString(1,user.getAccountInformation().getEmail());
      statement.setString(2,user.getAccountInformation().getPassword());
      statement.setString(3,user.getBasicInformation().getFirstName());
      statement.setString(4,user.getBasicInformation().getLastName());
      statement.setString(5,user.getBasicInformation().getNationality());
      statement.setString(6,user.getBasicInformation().getPhoneNumber());
      statement.setString(7,user.getBasicInformation().getGender());
      statement.setString(8,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        id = resultSet.getInt("userId");
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return id;
  }

  @Override public int readID(String email, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    int id = -1;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT userId FROM \"User\" WHERE (email = ? AND name  = ?);");
      statement.setString(1,email);
      statement.setString(2,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        id = resultSet.getInt("userId");
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return id;
  }

  @Override public UserInterface readByID(int userID) throws SQLException
  {
    Connection connection = getConnection();
    UserInterface user = null;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"User\" WHERE (userId = ?);");
      statement.setInt(1,userID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        user = new User(resultSet.getString("firstName"),resultSet.getString("lastName"),resultSet.getString("gender"),resultSet.getString("nationality"),resultSet.getString("phoneNumber"),resultSet.getString("email"),resultSet.getString("password"));
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return user;
  }

  @Override public UserList readByHotel(HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    UserList userList = new UserList();
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"User\" WHERE (name = ?);");
      statement.setString(1,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      while (true)
      {
        if (resultSet.next())
        {
          userList.addNewUser(new User(resultSet.getString("firstName"),resultSet.getString("lastName"),resultSet.getString("gender"),resultSet.getString("nationality"),resultSet.getString("phoneNumber"),resultSet.getString("email"),resultSet.getString("password")));
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
    return userList;
  }

  @Override public UserInterface updateByID(int userID, UserInterface newUser, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"User\" SET email=?,password=?,firstName=?,lastName=?,nationality=?,phoneNumber=?,gender=?,name=? WHERE userId=?;");
      statement.setString(1,newUser.getAccountInformation().getEmail());
      statement.setString(2,newUser.getAccountInformation().getPassword());
      statement.setString(3,newUser.getBasicInformation().getFirstName());
      statement.setString(4,newUser.getBasicInformation().getLastName());
      statement.setString(5,newUser.getBasicInformation().getNationality());
      statement.setString(6,newUser.getBasicInformation().getPhoneNumber());
      statement.setString(7,newUser.getBasicInformation().getGender());
      if (hotel!=null)
      {
        statement.setString(8,hotel.getName());
      }
      else
      {
        statement.setString(8,null);
      }
      statement.setInt(9,userID);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      newUser = null;
      System.out.println(e.getMessage());
    }
    connection.close();
    return newUser;
  }

  @Override public void updateHotelByHotel(HotelInterface oldHotel, HotelInterface newHotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"User\" SET name=? WHERE name=?;");
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

  @Override public void delete(int userID) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM \"User\" WHERE (userId=?);");
      statement.setInt(1,userID);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      //System.out.println(e.getMessage());
    }
    connection.close();
  }

  @Override public boolean checkBeUsed(int userID) throws SQLException
  {
    Connection connection = getConnection();
    boolean beUsed = false;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Order\" WHERE (userId=?);");
      statement.setInt(1,userID);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        beUsed =  true;
      }

      statement = connection.prepareStatement("SELECT name FROM \"User\" WHERE (userId=?);");
      statement.setInt(1,userID);
      resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        beUsed = resultSet.getString("name")!=null;
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
