package dbs.DAOImpl;

import dbs.DAOModelManager;
import model.domain.OrderList;
import model.domain.hotel.HotelInterface;
import model.domain.hotel.OptionServiceList;
import model.domain.order.Order;
import model.domain.order.OrderCancel;
import model.domain.order.OrderInterface;
import model.domain.room.TimeRangeList;
import org.postgresql.Driver;

import java.sql.*;

public class OrderDAOImpl implements OrderDAOImplInterface
{
  private static OrderDAOImpl instance;
  private RoomInformationDAOImplInterface roomInformationDAOImpl;
  private OptionServiceDAOImplInterface optionServiceDAOImpl;
  private RoomDAOImplInterface roomDAOImpl;
  private UserDAOImplInterface userDAOImpl;
  private TimeRangeDAOImplInterface timeRangeDAOImpl;
  private String url;
  private String user;
  private String password;

  private OrderDAOImpl(String url,String user,String password,RoomInformationDAOImplInterface roomInformationDAOImpl,OptionServiceDAOImplInterface optionServiceDAOImpl,RoomDAOImplInterface roomDAOImpl,UserDAOImplInterface userDAOImpl,TimeRangeDAOImplInterface timeRangeDAOImpl) throws SQLException
  {
    this.url = url;
    this.user = user;
    this.password = password;
    this.roomInformationDAOImpl = roomInformationDAOImpl;
    this.optionServiceDAOImpl = optionServiceDAOImpl;
    this.roomDAOImpl = roomDAOImpl;
    this.userDAOImpl = userDAOImpl;
    this.timeRangeDAOImpl = timeRangeDAOImpl;
    DriverManager.registerDriver(new Driver());
    checkTableAndDomain();
  }

  public void updateConnection(String url,String user,String password)
  {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  public static OrderDAOImpl getInstance(String url,String user,String password,RoomInformationDAOImplInterface roomInformationDAOImpl,OptionServiceDAOImplInterface optionServiceDAOImpl,RoomDAOImplInterface roomDAOImpl,UserDAOImplInterface userDAOImpl,TimeRangeDAOImplInterface timeRangeDAOImpl)
  {
    if (instance==null)
    {
      try
      {
        instance = new OrderDAOImpl(url,user,password,roomInformationDAOImpl,optionServiceDAOImpl,roomDAOImpl,userDAOImpl,timeRangeDAOImpl);
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
    String[] tables = {"CREATE TABLE \"Order\"(\n"
        + "orderNumber \"orderNumber\" NOT null,\n"
        + "orderState varchar(200)Not NUll,\n"
        + "userID \"id\" NOT NULL,\n"
        + "name \"Name\",\n"
        + "roomInformationID \"id\" NOT NULL,\n"
        + "roomID id NOT NULL,\n"
        + "PRIMARY KEY(orderNumber),\n"
        + "FOREIGN KEY(userID)REFERENCES \"User\"(userId),\n"
        + "FOREIGN KEY(name)REFERENCES \"Hotel\"(name),\n"
        + "FOREIGN KEY(roomInformationID)REFERENCES \"RoomInformation\"(roomInformationId),\n"
        + "FOREIGN KEY(roomID)REFERENCES \"Room\"(roomId));",
        "CREATE TABLE \"TimeRange_Order\"(\n"
            + "timeRangeID \"id\" NOT null,\n"
            + "orderNumber \"orderNumber\" NOT null,\n"
            + "PRIMARY KEY(timeRangeID,orderNumber),\n"
            + "FOREIGN KEY(timeRangeID)REFERENCES \"TimeRange\"(timeRangeID),\n"
            + "FOREIGN KEY(orderNumber)References \"Order\"(orderNumber));",
        "CREATE TABLE \"Order_OptionService\"(\n"
            + "orderNumber \"orderNumber\" NOT null,\n"
            + "optionServiceId \"id\" NOT null,\n"
            + "PRIMARY KEY(orderNumber,optionServiceId),\n"
            + "FOREIGN KEY(orderNumber)REFERENCES \"Order\"(orderNumber),\n"
            + "FOREIGN KEY(optionServiceId)REFERENCES \"OptionService\"(optionServiceId));"};
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

  @Override public void clear() throws SQLException
  {
    Connection connection = getConnection();
    String[] domains = {
        "DROP TABLE \"Order_OptionService\";",
        "DROP TABLE \"TimeRange_Order\";",
        "DROP TABLE \"Order\";",
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
    roomDAOImpl.clear();
    roomInformationDAOImpl.clear();
    optionServiceDAOImpl.clear();
    userDAOImpl.clear();
    timeRangeDAOImpl.clear();
    HotelDAOImpl.getInstance(url,user,password).clear();
  }

  @Override public OrderInterface create(int orderNumber, OrderInterface newOrder, HotelInterface hotel) throws SQLException
  {
    boolean check = false;
    Connection connection = getConnection();
    while (true)
    {
      try
      {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Order\"(orderNumber,orderState,userID,name,roomInformationID,roomID)VALUES(?,?,?,?,?,?);");
        statement.setInt(1,orderNumber);
        statement.setString(2,newOrder.getOrderState().getState());
        statement.setInt(3,userDAOImpl.readID(newOrder.getBasicInformation().getEmail(),hotel));
        statement.setString(4,hotel.getName());
        statement.setInt(5,roomInformationDAOImpl.readID(newOrder.getRoomInformation(),hotel));
        statement.setInt(6,roomDAOImpl.readID(newOrder.getRoom(),hotel));
        statement.executeUpdate();

        for (int x=0;x<newOrder.getTimeRangeList().getSize();x++)
        {
          if (timeRangeDAOImpl.readID(newOrder.getTimeRangeList().getTimeRangeByIndex(x))==-1)
          {
            while (timeRangeDAOImpl.create(DAOModelManager.getRandomID(),newOrder.getTimeRangeList().getTimeRangeByIndex(x))==null);
          }
          statement = connection.prepareStatement("INSERT INTO \"TimeRange_Order\"(orderNumber,timeRangeID)VALUES(?,?);");
          statement.setInt(1,orderNumber);
          statement.setInt(2,timeRangeDAOImpl.readID(newOrder.getTimeRangeList().getTimeRangeByIndex(x)));
          statement.executeUpdate();
        }

        for (int x=0;x<newOrder.getUserOptionServiceList().getSize();x++)
        {
          statement = connection.prepareStatement("INSERT INTO \"Order_OptionService\"(orderNumber,optionServiceId)VALUES(?,?);");
          statement.setInt(1,orderNumber);
          statement.setInt(2,optionServiceDAOImpl.readID(newOrder.getUserOptionServiceList().getOptionServiceByIndex(x),hotel));
          statement.executeUpdate();
        }
        break;
      }
      catch (SQLException e)
      {
        if (check)
        {
          System.out.println(e.getMessage());
          newOrder = null;
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
    return newOrder;
  }

  @Override public OrderInterface readByOrderNumber(int orderNumber) throws SQLException
  {
    Connection connection = getConnection();
    OrderInterface order = null;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Order\" WHERE (orderNumber=?);");
      statement.setInt(1,orderNumber);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        OptionServiceList optionServiceList = new OptionServiceList();
        TimeRangeList timeRangeList = new TimeRangeList();

        PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM \"Order_OptionService\" WHERE (orderNumber=?);");
        statement1.setInt(1,orderNumber);
        ResultSet resultSet1 = statement.executeQuery();
        while (resultSet1.next())
        {
          optionServiceList.addOptionService(optionServiceDAOImpl.readByID(resultSet1.getInt("optionServiceId")));
        }

        statement1 = connection.prepareStatement("SELECT * FROM \"TimeRange_Order\" WHERE (orderNumber=?);");
        statement1.setInt(1,orderNumber);
        resultSet1 = statement.executeQuery();
        while (resultSet1.next())
        {
          timeRangeList.addTimeRange(timeRangeDAOImpl.readByID(resultSet1.getInt("timeRangeID")));
        }

        order = new Order(resultSet.getInt("orderNumber"),roomDAOImpl.readByID(resultSet.getInt("roomID")),roomInformationDAOImpl.readByID(resultSet.getInt("roomInformationID")),userDAOImpl.readByID(resultSet.getInt("userID")).getBasicInformation(),optionServiceList,timeRangeList);
        if (resultSet.getString("orderState").equals("Cancel"))
        {
          order.setOrderState(new OrderCancel());
        }
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return order;
  }

  @Override public OrderList readByHotel(HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    OrderList orderList = new OrderList();
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Order\" WHERE (name=?);");
      statement.setString(1,hotel.getName());
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        OptionServiceList optionServiceList = new OptionServiceList();
        TimeRangeList timeRangeList = new TimeRangeList();

        PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM \"Order_OptionService\" WHERE (orderNumber=?);");
        statement1.setInt(1,resultSet.getInt("orderNumber"));
        ResultSet resultSet1 = statement1.executeQuery();
        while (resultSet1.next())
        {
          optionServiceList.addOptionService(optionServiceDAOImpl.readByID(resultSet1.getInt("optionServiceId")));
        }

        statement1 = connection.prepareStatement("SELECT * FROM \"TimeRange_Order\" WHERE (orderNumber=?);");
        statement1.setInt(1,resultSet.getInt("orderNumber"));
        resultSet1 = statement1.executeQuery();
        while (resultSet1.next())
        {
          timeRangeList.addTimeRange(timeRangeDAOImpl.readByID(resultSet1.getInt("timeRangeID")));
        }

        OrderInterface order = new Order(resultSet.getInt("orderNumber"),roomDAOImpl.readByID(resultSet.getInt("roomID")),roomInformationDAOImpl.readByID(resultSet.getInt("roomInformationID")),userDAOImpl.readByID(resultSet.getInt("userID")).getBasicInformation(),optionServiceList,timeRangeList);
        if (resultSet.getString("orderState").equals("Cancel"))
        {
          order.setOrderState(new OrderCancel());
        }
        orderList.addNewOrder(order);
      }
    }
    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }
    connection.close();
    return orderList;
  }

  @Override public OrderInterface updateByOrderNumber(int orderNumber, OrderInterface newOrder, HotelInterface hotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"Order\" SET orderState=?,name=? WHERE orderNumber=?;");
      statement.setString(1,newOrder.getOrderState().getState());
      statement.setString(2,hotel.getName());
      statement.setInt(3,orderNumber);
      statement.executeUpdate();

      statement = connection.prepareStatement("DELETE FROM \"TimeRange_Order\" WHERE (orderNumber=?);");
      statement.setInt(1,orderNumber);
      statement.executeUpdate();

      for (int x=0;x<newOrder.getTimeRangeList().getSize();x++)
      {
        if (timeRangeDAOImpl.readID(newOrder.getTimeRangeList().getTimeRangeByIndex(x))==-1)
        {
          while (timeRangeDAOImpl.create(DAOModelManager.getRandomID(),newOrder.getTimeRangeList().getTimeRangeByIndex(x))==null);
        }
        statement = connection.prepareStatement("INSERT INTO \"TimeRange_Order\"(orderNumber,timeRangeID)VALUES(?,?);");
        statement.setInt(1,orderNumber);
        statement.setInt(2,timeRangeDAOImpl.readID(newOrder.getTimeRangeList().getTimeRangeByIndex(x)));
        statement.executeUpdate();
      }

      statement = connection.prepareStatement("DELETE FROM \"Order_OptionService\" WHERE (orderNumber=?);");
      statement.setInt(1,orderNumber);
      statement.executeUpdate();

      for (int x=0;x<newOrder.getUserOptionServiceList().getSize();x++)
      {
        statement = connection.prepareStatement("INSERT INTO \"Order_OptionService\"(orderNumber,optionServiceId)VALUES(?,?);");
        statement.setInt(1,orderNumber);
        statement.setInt(2,optionServiceDAOImpl.readID(newOrder.getUserOptionServiceList().getOptionServiceByIndex(x),hotel));
        statement.executeUpdate();
      }
    }
    catch (SQLException e)
    {
      newOrder = null;
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    connection.close();
    return newOrder;
  }

  @Override public void updateHotelByHotel(HotelInterface oldHotel, HotelInterface newHotel) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE \"Order\" SET name=? WHERE name=?;");
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

  @Override public void delete(int orderNumber) throws SQLException
  {
    Connection connection = getConnection();
    try
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM \"TimeRange_Order\" WHERE (orderNumber=?);");
      statement.setInt(1,orderNumber);
      statement.executeUpdate();

      statement = connection.prepareStatement("DELETE FROM \"Order_OptionService\" WHERE (orderNumber=?);");
      statement.setInt(1,orderNumber);
      statement.executeUpdate();

      statement = connection.prepareStatement("DELETE FROM \"Order\" WHERE (orderNumber=?);");
      statement.setInt(1,orderNumber);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      //System.out.println(e.getMessage());
    }
    connection.close();
  }

  @Override public boolean checkBeUsed(int orderNumber) throws SQLException
  {
    Connection connection = getConnection();
    boolean beUsed = false;
    try
    {
      PreparedStatement statement = connection.prepareStatement("SELECT name FROM \"User\" WHERE (userId=?);");
      statement.setInt(1,orderNumber);
      ResultSet resultSet = statement.executeQuery();
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
