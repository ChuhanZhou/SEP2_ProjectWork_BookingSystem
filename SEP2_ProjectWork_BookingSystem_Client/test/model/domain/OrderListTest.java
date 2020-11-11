package model.domain;

import model.domain.hotel.OptionService;
import model.domain.hotel.OptionServiceList;
import model.domain.hotel.RoomInformation;
import model.domain.hotel.RoomInformationList;
import model.domain.order.Order;
import model.domain.order.OrderConfirm;
import model.domain.order.OrderInterface;
import model.domain.room.Room;
import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;
import model.domain.room.timeRange.Time;
import model.domain.room.timeRange.TimeRange;
import model.domain.room.timeRange.TimeRangeInterface;
import model.domain.user.User;
import model.domain.user.UserInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderListTest
{
  private OrderList orderList;
  private UserInterface user;
  private ArrayList<RoomInterface> roomList;
  private OptionServiceList optionServiceList;
  private RoomInformationList roomInformationList;

  private int randomNumber()
{
  return (int) (Math.random() * 100 + 0);
}

  private int randomNumber(int max)
  {
    return (int) (Math.random() * max + 0);
  }

  private int randomNumber(int max,int min)
  {
    return (int) (Math.random() * (max - min) + min);
  }

  private void checkOrderEquals(OrderInterface order1,OrderInterface order2)
  {
    assertEquals(order1.getOrderNumber(),order2.getOrderNumber());
    for (int x=0;x<order1.getTimeRangeList().getSize();x++)
    {
      assertEquals(order1.getTimeRangeList().getTimeRangeByIndex(x).getStartTime().toString(),order2.getTimeRangeList().getTimeRangeByIndex(x).getStartTime().toString());
      assertEquals(order1.getTimeRangeList().getTimeRangeByIndex(x).getEndTime().toString(),order2.getTimeRangeList().getTimeRangeByIndex(x).getEndTime().toString());
    }
    assertEquals(order1.getRoom().getRoomNumber(),order2.getRoom().getRoomNumber());
    assertEquals(order1.getRoom().getType(),order2.getRoom().getType());
    assertEquals(order1.getBasicInformation().getFirstName(),order2.getBasicInformation().getFirstName());
    assertEquals(order1.getBasicInformation().getLastName(),order2.getBasicInformation().getLastName());
    assertEquals(order1.getBasicInformation().getGender(),order2.getBasicInformation().getGender());
    assertEquals(order1.getBasicInformation().getEmail(),order2.getBasicInformation().getEmail());
    assertEquals(order1.getBasicInformation().getPhoneNumber(),order2.getBasicInformation().getPhoneNumber());
    assertEquals(order1.getBasicInformation().getNationality(),order2.getBasicInformation().getNationality());
    assertEquals(order1.getRoomInformation().getType(),order2.getRoomInformation().getType());
    assertEquals(order1.getRoomInformation().getPrice(),order2.getRoomInformation().getPrice());
    for (int x=0;x<order1.getRoomInformation().getSupplyForFree().getSize();x++)
    {
      assertEquals(order1.getRoomInformation().getSupplyForFree().getOptionServiceByIndex(x).getServiceType(),order2.getRoomInformation().getSupplyForFree().getOptionServiceByIndex(x).getServiceType());
      assertEquals(order1.getRoomInformation().getSupplyForFree().getOptionServiceByIndex(x).getPrice(),order2.getRoomInformation().getSupplyForFree().getOptionServiceByIndex(x).getPrice());
    }
    for (int x=0;x<order1.getUserOptionServiceList().getSize();x++)
    {
      assertEquals(order1.getUserOptionServiceList().getOptionServiceByIndex(x).getServiceType(),order2.getUserOptionServiceList().getOptionServiceByIndex(x).getServiceType());
      assertEquals(order1.getUserOptionServiceList().getOptionServiceByIndex(x).getPrice(),order2.getUserOptionServiceList().getOptionServiceByIndex(x).getPrice());
    }
    assertEquals(order1.getOrderState().getState(),order2.getOrderState().getState());
    assertEquals(order1.getTotalPrice(),order2.getTotalPrice());
  }

  private void addUser()
  {
    user = new User("Email " + randomNumber(),"Password " + randomNumber());
  }

  private RoomInformation getRandomRoomInformation()
  {
    int index = randomNumber(this.roomInformationList.getSize());
    return roomInformationList.getRoomInformationByIndex(index);
  }

  private void addRooms()
  {
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      RoomInterface room = new Room(randomNumber(),getRandomRoomInformation().getType());
      Boolean newRoom = false;
      while (!newRoom)
      {
        int i;
        for (i=0;i<roomList.size();i++)
        {
          if (this.roomList.get(i).equals(room))
          {
            room = new Room(randomNumber(),getRandomRoomInformation().getType());
            break;
          }
        }
        if (i==roomList.size())
        {
          newRoom = true;
        }
      }
      roomList.add(room);
    }
  }

  private void addOptionServices()
  {
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      OptionService optionService = new OptionService("Type " + randomNumber(),randomNumber());
      while (optionServiceList.getOptionServiceByServiceType(optionService.getServiceType())!=null)
      {
        optionService = new OptionService("Type " + randomNumber(),randomNumber());
      }
      optionServiceList.addOptionService(optionService);
    }
  }

  private OptionServiceList getRandomOptionServiceList()
  {
    OptionServiceList optionServiceList = new OptionServiceList();
    int time = randomNumber(this.optionServiceList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(this.optionServiceList.getSize());
      optionServiceList.addOptionService(this.optionServiceList.getOptionServiceByIndex(index));
    }
    return optionServiceList;
  }

  private void addRoomInformation()
  {
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      RoomInformation roomInformation = new RoomInformation("Type " + randomNumber(),randomNumber(),getRandomOptionServiceList(),"Facilities " + randomNumber(),randomNumber());
      while (roomInformationList.getRoomInformationByType(roomInformation.getType())!=null)
      {
        roomInformation = new RoomInformation("Type " + randomNumber(),randomNumber(),getRandomOptionServiceList(),"Facilities " + randomNumber(),randomNumber());
      }
      roomInformationList.addNewRoomInformation(roomInformation);
    }
  }

  private Time randomTime()
  {
    return new Time(randomNumber(), randomNumber(), randomNumber(), randomNumber(), randomNumber());
  }

  private TimeRangeList getRandomTimeRangeList()
  {
    TimeRangeList timeRangeList = new TimeRangeList();
    int time = randomNumber();
    for (int x = 0; x < time; x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      if (!timeRangeList.hasTimeRange(timeRange))
      {
        timeRangeList.addTimeRange(timeRange);
      }
    }
    return timeRangeList;
  }

  @BeforeEach void setUp()
  {
    orderList = new OrderList();
    addUser();
    optionServiceList = new OptionServiceList();
    addOptionServices();
    roomInformationList = new RoomInformationList();
    addRoomInformation();
    roomList = new ArrayList<>();
    addRooms();
  }

  @AfterEach void tearDown()
  {
  }

  @Test void addNewOrderZero()
  {
    assertEquals(0,orderList.getSize());
    assertDoesNotThrow(()->orderList.addNewOrder(null));
    assertEquals(0,orderList.getSize());
  }

  @Test void addNewOrderOne()
  {
    assertEquals(0,orderList.getSize());
    RoomInterface room = roomList.get(randomNumber(roomList.size()));
    OrderInterface order = new Order(randomNumber(),room,roomInformationList.getRoomInformationByType(room.getType()),user.getBasicInformation(),getRandomOptionServiceList(),getRandomTimeRangeList());
    assertDoesNotThrow(()->orderList.addNewOrder(order));
    assertEquals(1,orderList.getSize());
    checkOrderEquals(order,orderList.getOrderByIndex(0));
  }

  @Test void addNewOrderMany()
  {
    ArrayList<OrderInterface> other = new ArrayList<>();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      RoomInterface room = roomList.get(randomNumber(roomList.size()));
      OrderInterface order = new Order(randomNumber(),room,roomInformationList.getRoomInformationByType(room.getType()),user.getBasicInformation(),getRandomOptionServiceList(),getRandomTimeRangeList());
      while (orderList.getOrderByOrderNumber(order.getOrderNumber())!=null)
      {
        order = new Order(randomNumber(),room,roomInformationList.getRoomInformationByType(room.getType()),user.getBasicInformation(),getRandomOptionServiceList(),getRandomTimeRangeList());
      }
      orderList.addNewOrder(order);
      other.add(order);
    }

    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(orderList.getOrderByIndex(x),other.get(x));
    }
  }

  @Test void addNewOrderBoundary()
  {
    RoomInterface room = roomList.get(randomNumber(roomList.size()));
    OrderInterface order = new Order(randomNumber(),room,roomInformationList.getRoomInformationByType(room.getType()),user.getBasicInformation(),getRandomOptionServiceList(),getRandomTimeRangeList());
    orderList.addNewOrder(order);
    RoomInterface otherRoom = roomList.get(randomNumber(roomList.size()));
    OrderInterface otherOrder = new Order(order.getOrderNumber(),otherRoom,roomInformationList.getRoomInformationByType(otherRoom.getType()),user.getBasicInformation(),getRandomOptionServiceList(),getRandomTimeRangeList());
    orderList.addNewOrder(otherOrder);
    assertEquals(1,orderList.getSize());
    checkOrderEquals(order,orderList.getOrderByIndex(0));
  }

  @Test void addNewOrderException()
  {
    //no exception
  }

  @Test void updateOrderZero()
  {
    addNewOrderMany();
    OrderList orderListCopy = orderList.copy();
    orderList.updateOrder(null);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(orderList.getOrderByIndex(x),orderListCopy.getOrderByIndex(x));
    }
  }

  @Test void updateOrderOne()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface copy = orderList.getOrderByIndex(index).copy();
    RoomInterface room = roomList.get(randomNumber(roomList.size()));
    OrderInterface newOrder = new Order(copy.getOrderNumber(),room,roomInformationList.getRoomInformationByType(room.getType()),user.getBasicInformation(),getRandomOptionServiceList(),getRandomTimeRangeList());
    orderList.updateOrder(newOrder);
    checkOrderEquals(newOrder,orderList.getOrderByIndex(index));
  }

  @Test void updateOrderMany()
  {
    addNewOrderMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(orderList.getSize());
      OrderInterface copy = orderList.getOrderByIndex(index).copy();
      RoomInterface room = roomList.get(randomNumber(roomList.size()));
      OrderInterface newOrder = new Order(copy.getOrderNumber(),room,roomInformationList.getRoomInformationByType(room.getType()),user.getBasicInformation(),getRandomOptionServiceList(),getRandomTimeRangeList());
      orderList.updateOrder(newOrder);
      checkOrderEquals(newOrder,orderList.getOrderByIndex(index));
    }
  }

  @Test void updateOrderBoundary()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    RoomInterface room = roomList.get(randomNumber(roomList.size()));
    OrderInterface newOrder = new Order(-1,room,roomInformationList.getRoomInformationByType(room.getType()),user.getBasicInformation(),getRandomOptionServiceList(),getRandomTimeRangeList());
    orderList.updateOrder(newOrder);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }

  @Test void updateOrderException()
  {
    //no exception
  }

  //@Test void getOrderByIndex()
  //{
  //}
//
  //@Test void getOrderByOrderNumber()
  //{
  //}
//
  //@Test void getOrdersByUser()
  //{
  //}
//
  //@Test void getOrdersByRoom()
  //{
  //}
//
  //@Test void getOrdersByRoomInformation()
  //{
  //}
//
  //@Test void getOrdersByTimeRange()
  //{
  //}
//
  //@Test void getOrdersByTimeRangeList()
  //{
  //}

  @Test void cancelOrderByIndexZero()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrderByIndex(-1);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }

  @Test void cancelOrderByIndexOne()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    orderList.cancelOrderByIndex(index);
    assertEquals("Cancel",orderList.getOrderByIndex(index).getOrderState().getState());
  }

  @Test void cancelOrderByIndexMany()
  {
    addNewOrderMany();
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(orderList.getSize());
      orderList.cancelOrderByIndex(index);
      assertEquals("Cancel",orderList.getOrderByIndex(index).getOrderState().getState());
    }
  }

  @Test void cancelOrderByIndexBoundary()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrderByIndex(orderList.getSize());
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
    int index = randomNumber(orderList.getSize());
    orderList.cancelOrderByIndex(index);
    assertEquals("Cancel",orderList.getOrderByIndex(index).getOrderState().getState());
    orderList.cancelOrderByIndex(index);
    assertEquals("Cancel",orderList.getOrderByIndex(index).getOrderState().getState());
  }

  @Test void cancelOrderByIndexException()
  {
    //no exception
  }

  @Test void cancelOrderByOrderNumberZero()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrderByOrderNumber(-1);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }

  @Test void cancelOrderByOrderNumberOne()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    orderList.cancelOrderByOrderNumber(order.getOrderNumber());
    assertEquals("Cancel",orderList.getOrderByIndex(index).getOrderState().getState());
  }

  @Test void cancelOrderByOrderNumberMany()
  {
    addNewOrderMany();
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(orderList.getSize());
      OrderInterface order = orderList.getOrderByIndex(index);
      orderList.cancelOrderByOrderNumber(order.getOrderNumber());
      assertEquals("Cancel",orderList.getOrderByIndex(index).getOrderState().getState());
    }
  }

  @Test void cancelOrderByOrderNumberBoundary()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrderByOrderNumber(-1);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    orderList.cancelOrderByOrderNumber(order.getOrderNumber());
    assertEquals("Cancel",orderList.getOrderByIndex(index).getOrderState().getState());
    orderList.cancelOrderByOrderNumber(order.getOrderNumber());
    assertEquals("Cancel",orderList.getOrderByIndex(index).getOrderState().getState());
  }

  @Test void cancelOrderByOrderNumberException()
  {
    //no exception
  }

  @Test void cancelOrdersByUserZero()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrdersByUser(null);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }

  @Test void cancelOrdersByUserOne()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    orderList.cancelOrdersByUser(user);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (orderList.getOrderByIndex(x).getBasicInformation().getEmail().equals(user.getBasicInformation().getEmail()))
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByUserMany()
  {
    addNewOrderMany();
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(orderList.getSize());
      OrderInterface order = orderList.getOrderByIndex(index);
      orderList.cancelOrdersByUser(user);
      for (int i=0;i<orderList.getOrdersByUser(user).getSize();i++)
      {
        if (orderList.getOrdersByUser(user).getOrderByIndex(i).getBasicInformation().getEmail().equals(user.getBasicInformation().getEmail()))
        {
          assertEquals("Cancel",orderList.getOrdersByUser(user).getOrderByIndex(i).getOrderState().getState());
        }
      }
    }
  }

  @Test void cancelOrdersByUserBoundary()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    orderList.cancelOrdersByUser(user);
    orderList.cancelOrdersByUser(user);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (orderList.getOrderByIndex(x).getBasicInformation().getEmail().equals(user.getBasicInformation().getEmail()))
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByUserException()
  {
    //no boundary
  }

  @Test void cancelOrdersByRoomZero()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrdersByRoom(null);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }

  @Test void cancelOrdersByRoomOne()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    RoomInterface room = order.getRoom();
    orderList.cancelOrdersByRoom(room);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (orderList.getOrderByIndex(x).getRoom().equals(room))
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByRoomMany()
  {
    addNewOrderMany();
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(orderList.getSize());
      OrderInterface order = orderList.getOrderByIndex(index);
      RoomInterface room = order.getRoom();
      orderList.cancelOrdersByRoom(room);
      for (int i=0;i<orderList.getOrdersByRoom(room).getSize();i++)
      {
        if (orderList.getOrdersByRoom(room).getOrderByIndex(i).getRoom().equals(room))
        {
          assertEquals("Cancel",orderList.getOrdersByRoom(room).getOrderByIndex(i).getOrderState().getState());
        }
      }
    }
  }

  @Test void cancelOrdersByRoomBoundary()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    RoomInterface room = order.getRoom();
    orderList.cancelOrdersByRoom(room);
    orderList.cancelOrdersByRoom(room);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (orderList.getOrderByIndex(x).getRoom().equals(room))
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByRoomException()
  {
    //no exception
  }

  @Test void cancelOrdersByRoomInformationZero()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrdersByRoomInformation(null);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }

  @Test void cancelOrdersByRoomInformationOne()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    RoomInformation roomInformation = order.getRoomInformation();
    orderList.cancelOrdersByRoomInformation(roomInformation);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (orderList.getOrderByIndex(x).getRoomInformation().equals(roomInformation))
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByRoomInformationMany()
  {
    addNewOrderMany();
    int time = randomNumber();
    for (int i=0;i<time;i++)
    {
      int index = randomNumber(orderList.getSize());
      OrderInterface order = orderList.getOrderByIndex(index);
      RoomInformation roomInformation = order.getRoomInformation();
      orderList.cancelOrdersByRoomInformation(roomInformation);
      for (int x=0;x<orderList.getSize();x++)
      {
        if (orderList.getOrderByIndex(x).getRoomInformation().equals(roomInformation))
        {
          assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
        }
      }
    }
  }

  @Test void cancelOrdersByRoomInformationBoundary()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    RoomInformation roomInformation = order.getRoomInformation();
    orderList.cancelOrdersByRoomInformation(roomInformation);
    orderList.cancelOrdersByRoomInformation(roomInformation);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (orderList.getOrderByIndex(x).getRoomInformation().equals(roomInformation))
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByRoomInformationException()
  {
    //no exception
  }

  @Test void cancelOrdersByTimeRangeZero()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrdersByTimeRange(null);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }

  @Test void cancelOrdersByTimeRangeOne()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    TimeRangeInterface timeRange = order.getTimeRangeList().getTimeRangeByIndex(randomNumber(order.getTimeRangeList().getSize()));
    orderList.cancelOrdersByTimeRange(timeRange);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (orderList.getOrderByIndex(x).getTimeRangeList().hasTimeRange(timeRange))
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByTimeRangeMany()
  {
    addNewOrderMany();
    int time = randomNumber();
    for (int i=0;i<time;i++)
    {
      int index = randomNumber(orderList.getSize());
      OrderInterface order = orderList.getOrderByIndex(index);
      TimeRangeInterface timeRange = order.getTimeRangeList().getTimeRangeByIndex(randomNumber(order.getTimeRangeList().getSize()));
      orderList.cancelOrdersByTimeRange(timeRange);
      for (int x=0;x<orderList.getSize();x++)
      {
        if (orderList.getOrderByIndex(x).getTimeRangeList().hasTimeRange(timeRange))
        {
          assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
        }
      }
    }
  }

  @Test void cancelOrdersByTimeRangeBoundary()
  {
    addNewOrderMany();
    int index = randomNumber(orderList.getSize());
    OrderInterface order = orderList.getOrderByIndex(index);
    TimeRangeInterface timeRange = order.getTimeRangeList().getTimeRangeByIndex(randomNumber(order.getTimeRangeList().getSize()));
    orderList.cancelOrdersByTimeRange(timeRange);
    orderList.cancelOrdersByTimeRange(timeRange);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (orderList.getOrderByIndex(x).getTimeRangeList().hasTimeRange(timeRange))
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByTimeRangeException()
  {
    //no exception
  }

  @Test void cancelOrdersByOrderListZero()
  {
    addNewOrderMany();
    OrderList copy = orderList.copy();
    orderList.cancelOrdersByOrderList(null);
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }

  @Test void cancelOrdersByOrderListOne()
  {
    addNewOrderMany();
    int numberOfCancel = randomNumber(orderList.getSize(),1);
    OrderList cancelOrderList = new OrderList();
    for (int x=0;x<numberOfCancel;x++)
    {
      cancelOrderList.addNewOrder(orderList.getOrderByIndex(randomNumber(orderList.getSize())).copy());
    }
    orderList.cancelOrdersByOrderList(cancelOrderList);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (cancelOrderList.getOrderByOrderNumber(orderList.getOrderByIndex(x).getOrderNumber())!=null)
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByOrderListMany()
  {
    addNewOrderMany();
    int time = randomNumber();
    for (int i=0;i<time;i++)
    {
      int numberOfCancel = randomNumber(orderList.getSize(),1);
      OrderList cancelOrderList = new OrderList();
      for (int x=0;x<numberOfCancel;x++)
      {
        cancelOrderList.addNewOrder(orderList.getOrderByIndex(randomNumber(orderList.getSize())).copy());
      }
      orderList.cancelOrdersByOrderList(cancelOrderList);
      for (int x=0;x<orderList.getSize();x++)
      {
        if (cancelOrderList.getOrderByOrderNumber(orderList.getOrderByIndex(x).getOrderNumber())!=null)
        {
          assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
        }
      }
    }
  }

  @Test void cancelOrdersByOrderListBoundary()
  {
    addNewOrderMany();
    int numberOfCancel = randomNumber(orderList.getSize(),1);
    OrderList cancelOrderList = new OrderList();
    for (int x=0;x<numberOfCancel;x++)
    {
      cancelOrderList.addNewOrder(orderList.getOrderByIndex(randomNumber(orderList.getSize())).copy());
    }
    orderList.cancelOrdersByOrderList(cancelOrderList);
    orderList.cancelOrdersByOrderList(cancelOrderList);
    for (int x=0;x<orderList.getSize();x++)
    {
      if (cancelOrderList.getOrderByOrderNumber(orderList.getOrderByIndex(x).getOrderNumber())!=null)
      {
        assertEquals("Cancel",orderList.getOrderByIndex(x).getOrderState().getState());
      }
      else
      {
        assertEquals("Confirm",orderList.getOrderByIndex(x).getOrderState().getState());
      }
    }
  }

  @Test void cancelOrdersByOrderListException()
  {
    //no exception
  }

  @Test void copy()
  {
    OrderList copy = orderList.copy();
    assertNotEquals(copy.hashCode(),orderList.hashCode());
    for (int x=0;x<orderList.getSize();x++)
    {
      checkOrderEquals(copy.getOrderByIndex(x),orderList.getOrderByIndex(x));
    }
  }
}