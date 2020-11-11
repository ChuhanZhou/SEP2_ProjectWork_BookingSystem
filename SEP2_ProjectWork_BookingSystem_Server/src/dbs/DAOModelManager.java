package dbs;

import dbs.DAOImpl.*;
import model.domain.OrderList;
import model.domain.RoomList;
import model.domain.UserList;
import model.domain.hotel.*;
import model.domain.order.OrderInterface;
import model.domain.room.RoomInterface;
import model.domain.user.UserInterface;

import java.sql.SQLException;

public class DAOModelManager implements DAOModel
{
  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USER = "postgres";
  private static final String PASSWORD = "294409";
  private String url;
  private String user;
  private String password;
  private HotelDAOImplInterface hotelDAOImpl;
  private UserDAOImplInterface userDAOImpl;
  private TimeRangeDAOImplInterface timeRangeDAOImpl;
  private OptionServiceDAOImplInterface optionServiceDAOImpl;
  private RoomInformationDAOImplInterface roomInformationDAOImpl;
  private RoomDAOImplInterface roomDAOImpl;
  private OrderDAOImplInterface orderDAOImpl;

  public static int getRandomID()
  {
    return (int) (Math.random() * (1999999999 - 1) + 1);
  }

  public DAOModelManager()
  {
    this(URL,USER,PASSWORD);
  }

  public DAOModelManager(String url,String user,String password)
  {
    this.url = url;
    this.user = user;
    this.password = password;
    hotelDAOImpl = HotelDAOImpl.getInstance(this.url,this.user,this.password);
    timeRangeDAOImpl = TimeRangeDAOImpl.getInstance(this.url,this.user,this.password);
    optionServiceDAOImpl = OptionServiceDAOImpl.getInstance(this.url,this.user,this.password);
    userDAOImpl = UserDAOImpl.getInstance(this.url,this.user,this.password);
    roomInformationDAOImpl = RoomInformationDAOImpl.getInstance(this.url,this.user,this.password,optionServiceDAOImpl);
    roomDAOImpl = RoomDAOImpl.getInstance(this.url,this.user,this.password,timeRangeDAOImpl);
    orderDAOImpl = OrderDAOImpl.getInstance(this.url,this.user,this.password,roomInformationDAOImpl,optionServiceDAOImpl,roomDAOImpl,userDAOImpl,timeRangeDAOImpl);
  }

  @Override public void clear()
  {
    try
    {
      hotelDAOImpl.clear();
      userDAOImpl.clear();
      timeRangeDAOImpl.clear();
      optionServiceDAOImpl.clear();
      roomInformationDAOImpl.clear();
      roomDAOImpl.clear();
      orderDAOImpl.clear();
    }
    catch (SQLException e)
    {
      //e.printStackTrace();
    }
  }

  @Override public boolean haveData()
  {
    try
    {
      return hotelDAOImpl.read()!=null;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

  @Override public HotelInterface addHotel(HotelInterface hotel)
  {
    try
    {
      return hotelDAOImpl.create(hotel);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  @Override public HotelInterface getHotel()
  {
    try
    {
      return hotelDAOImpl.read();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  @Override public HotelInterface updateHotel(HotelInterface oldHotel, HotelInterface newHotel)
  {
    try
    {
      if (oldHotel.getName().equals(newHotel.getName()))
      {
        hotelDAOImpl.update(oldHotel,newHotel);
      }
      else
      {
        hotelDAOImpl.create(newHotel);
        optionServiceDAOImpl.updateHotelByHotel(oldHotel,newHotel);
        roomInformationDAOImpl.updateHotelByHotel(oldHotel,newHotel);
        roomDAOImpl.updateHotelByHotel(oldHotel,newHotel);
        userDAOImpl.updateHotelByHotel(oldHotel,newHotel);
        orderDAOImpl.updateHotelByHotel(oldHotel,newHotel);
        hotelDAOImpl.delete(oldHotel);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      newHotel = null;
    }
    return newHotel;
  }

  @Override public OptionService addOptionService(OptionService newOptionService)
  {
    try
    {
      while (optionServiceDAOImpl.create(getRandomID(),newOptionService, Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      newOptionService = null;
      e.printStackTrace();
    }
    return newOptionService;
  }

  @Override public OptionServiceList getAllOptionService()
  {
    try
    {
      return optionServiceDAOImpl.readByHotel(Hotel.getHotel());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return new OptionServiceList();
    }
  }

  @Override public OptionService updateOptionService(OptionService oldOptionService, OptionService newOptionService)
  {
    try
    {
      int oldId = optionServiceDAOImpl.readID(oldOptionService,Hotel.getHotel());
      optionServiceDAOImpl.updateByID(oldId,oldOptionService,null);
      if (!optionServiceDAOImpl.checkBeUsed(oldId))
      {
        optionServiceDAOImpl.delete(oldId);
      }
      while (optionServiceDAOImpl.create(getRandomID(),newOptionService,Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      newOptionService = null;
    }
    return newOptionService;
  }

  @Override public void removeOptionService(OptionService optionService)
  {
    try
    {
      int id = optionServiceDAOImpl.readID(optionService,Hotel.getHotel());
      optionServiceDAOImpl.updateByID(id,optionService,null);
      if (!optionServiceDAOImpl.checkBeUsed(id))
      {
        optionServiceDAOImpl.delete(id);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  @Override public RoomInformation addRoomInformation(RoomInformation newRoomInformation)
  {
    try
    {
      while (roomInformationDAOImpl.create(getRandomID(),newRoomInformation, Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      newRoomInformation = null;
      e.printStackTrace();
    }
    return newRoomInformation;
  }

  @Override public RoomInformationList getAllRoomInformation()
  {
    try
    {
      return roomInformationDAOImpl.readByHotel(Hotel.getHotel());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return new RoomInformationList();
    }
  }

  @Override public RoomInformation updateRoomInformation(RoomInformation oldRoomInformation, RoomInformation newRoomInformation)
  {
    try
    {
      int oldId = roomInformationDAOImpl.readID(oldRoomInformation,Hotel.getHotel());
      roomInformationDAOImpl.updateByID(oldId,oldRoomInformation,null);
      if (!roomInformationDAOImpl.checkBeUsed(oldId))
      {
        roomInformationDAOImpl.delete(oldId);
      }
      while (roomInformationDAOImpl.create(getRandomID(),newRoomInformation,Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      newRoomInformation = null;
    }
    return newRoomInformation;
  }

  @Override public void removeRoomInformation(RoomInformation roomInformation)
  {
    try
    {
      int id = roomInformationDAOImpl.readID(roomInformation,Hotel.getHotel());
      roomInformationDAOImpl.updateByID(id,roomInformation,null);
      if (!roomInformationDAOImpl.checkBeUsed(id))
      {
        roomInformationDAOImpl.delete(id);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  @Override public RoomInterface addRoom(RoomInterface newRoom)
  {
    try
    {
      while (roomDAOImpl.create(getRandomID(),newRoom, Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      newRoom = null;
      e.printStackTrace();
    }
    return newRoom;
  }

  @Override public RoomList getAllRoom()
  {
    try
    {
      return roomDAOImpl.readByHotel(Hotel.getHotel());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return new RoomList();
    }
  }

  @Override public RoomInterface updateRoom(RoomInterface oldRoom, RoomInterface newRoom)
  {
    try
    {
      int oldId = roomDAOImpl.readID(oldRoom,Hotel.getHotel());
      roomDAOImpl.updateByID(oldId,oldRoom,null);
      if (!roomDAOImpl.checkBeUsed(oldId))
      {
        roomDAOImpl.delete(oldId);
      }
      while (roomDAOImpl.create(getRandomID(),newRoom,Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      newRoom = null;
    }
    return newRoom;
  }

  @Override public void removeRoom(RoomInterface room)
  {
    try
    {
      int id = roomDAOImpl.readID(room,Hotel.getHotel());
      roomDAOImpl.updateByID(id,room,null);
      if (!roomDAOImpl.checkBeUsed(id))
      {
        roomDAOImpl.delete(id);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  @Override public UserInterface addUser(UserInterface newUser)
  {
    try
    {
      while (userDAOImpl.create(getRandomID(),newUser, Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      newUser = null;
      e.printStackTrace();
    }
    return newUser;
  }

  @Override public UserList getAllUser()
  {
    try
    {
      return userDAOImpl.readByHotel(Hotel.getHotel());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return new UserList();
    }
  }

  @Override public UserInterface updateUser(UserInterface oldUser, UserInterface newUser)
  {
    try
    {
      int oldId = userDAOImpl.readID(oldUser,Hotel.getHotel());
      userDAOImpl.updateByID(oldId,oldUser,null);
      if (!userDAOImpl.checkBeUsed(oldId))
      {
        userDAOImpl.delete(oldId);
      }
      while (userDAOImpl.create(getRandomID(),newUser,Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      newUser = null;
    }
    return newUser;
  }

  @Override public void removeUser(UserInterface user)
  {
    try
    {
      int id = userDAOImpl.readID(user,Hotel.getHotel());
      userDAOImpl.updateByID(id,user,null);
      if (!userDAOImpl.checkBeUsed(id))
      {
        userDAOImpl.delete(id);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }

  @Override public OrderInterface addOrder(OrderInterface newOrder)
  {
    try
    {
      while (orderDAOImpl.create(newOrder.getOrderNumber(),newOrder, Hotel.getHotel())==null);
    }
    catch (SQLException e)
    {
      newOrder = null;
      e.printStackTrace();
    }
    return newOrder;
  }

  @Override public OrderList getAllOrder()
  {
    try
    {
      return orderDAOImpl.readByHotel(Hotel.getHotel());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return new OrderList();
    }
  }

  @Override public OrderInterface updateOrder(OrderInterface newOrder)
  {
    try
    {
      orderDAOImpl.updateByOrderNumber(newOrder.getOrderNumber(),newOrder,Hotel.getHotel());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      newOrder = null;
    }
    return newOrder;
  }

  @Override public void removeOrder(OrderInterface order)
  {
    try
    {
      orderDAOImpl.updateByOrderNumber(order.getOrderNumber(),order,null);
      if (!orderDAOImpl.checkBeUsed(order.getOrderNumber()))
      {
        orderDAOImpl.delete(order.getOrderNumber());
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
}
