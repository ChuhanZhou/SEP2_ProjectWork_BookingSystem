package model.domain;

import model.domain.room.Room;
import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;
import model.domain.room.timeRange.Time;
import model.domain.room.timeRange.TimeRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoomListTest
{
  private RoomList roomList;

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

  private Time randomTime()
  {
    return new Time(randomNumber(), randomNumber(), randomNumber(),
        randomNumber(), randomNumber());
  }

  private void checkRoomEquals(RoomInterface room1,RoomInterface room2)
  {
    assertEquals(room1.getRoomNumber(),room2.getRoomNumber());
    assertEquals(room1.getType(),room2.getType());
    for (int x=0;x<room1.getBookedTimeRangeList().getSize();x++)
    {
      assertTrue(room2.getBookedTimeRangeList().hasTimeRange(room1.getBookedTimeRangeList().getTimeRangeByIndex(x)));
    }
    for (int x=0;x<room2.getBookedTimeRangeList().getSize();x++)
    {
      assertTrue(room1.getBookedTimeRangeList().hasTimeRange(room2.getBookedTimeRangeList().getTimeRangeByIndex(x)));
    }
  }

  @BeforeEach void setUp()
  {
    roomList = new RoomList();
  }

  @Test void addNewRoomZero()
  {
    assertDoesNotThrow(()->roomList.addNewRoom(null));
    assertEquals(0,roomList.getSize());
  }

  @Test void addNewRoomOne()
  {
    RoomInterface room = new Room(randomNumber(),"Type " + randomNumber());
    assertEquals(0,roomList.getSize());
    assertDoesNotThrow(()-> roomList.addNewRoom(room));
    assertEquals(1,roomList.getSize());
    checkRoomEquals(room,roomList.getRoomByIndex(0));
  }

  @Test void addNewRoomMany()
  {
    ArrayList<RoomInterface> roomList = new ArrayList<>();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      RoomInterface room = new Room(randomNumber(),"Type " + randomNumber());
      while (this.roomList.getRoomByRoomNumber(room.getRoomNumber())!=null)
      {
        room = new Room(randomNumber(),"Type " + randomNumber());
      }
      this.roomList.addNewRoom(room);
      roomList.add(room);
    }
    for (int x=0;x<roomList.size();x++)
    {
      checkRoomEquals(roomList.get(x),this.roomList.getRoomByIndex(x));
    }
  }

  @Test void addNewRoomBoundary()
  {
    RoomInterface room = new Room(randomNumber(),"Type " + randomNumber());
    assertEquals(0,roomList.getSize());
    roomList.addNewRoom(room);
    assertEquals(1,roomList.getSize());
    roomList.addNewRoom(room);
    assertEquals(1,roomList.getSize());
    RoomInterface otherRoom = new Room(room.getRoomNumber(),"New " + room.getType());
    roomList.addNewRoom(otherRoom);
    assertEquals(1,roomList.getSize());
    checkRoomEquals(room,roomList.getRoomByIndex(0));
  }

  @Test void addNewRoomException()
  {
    //no exception
  }

  @Test void updateRoomZero()
  {
    addNewRoomMany();
    assertDoesNotThrow(()-> roomList.updateRoom(null,null));
  }

  @Test void updateRoomOne()
  {
    addNewRoomMany();
    int index = randomNumber(roomList.getSize());
    RoomInterface room = roomList.copy().getRoomByIndex(index);
    room.setType("New " + room.getType());
    roomList.updateRoom(roomList.copy().getRoomByIndex(index),room);
    assertEquals(room.getClass(),roomList.getRoomByIndex(index).getClass());
    assertNotEquals(room.hashCode(),roomList.getRoomByIndex(index).hashCode());
    checkRoomEquals(room,roomList.getRoomByIndex(index));
  }

  @Test void updateRoomMany()
  {
    addNewRoomMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(roomList.getSize());
      RoomInterface room = roomList.copy().getRoomByIndex(index);
      room.setType("New " + room.getType());
      roomList.updateRoom(roomList.copy().getRoomByIndex(index),room);
      assertEquals(room.getClass(),roomList.getRoomByIndex(index).getClass());
      assertNotEquals(room.hashCode(),roomList.getRoomByIndex(index).hashCode());
      checkRoomEquals(room,roomList.getRoomByIndex(index));
    }
  }

  @Test void updateRoomBoundary()
  {
    addNewRoomMany();
    int index = randomNumber(roomList.getSize());
    RoomInterface room = roomList.copy().getRoomByIndex(index);
    RoomInterface otherRoom = new Room(room.getRoomNumber()+1,"New " + room.getType());
    roomList.updateRoom(otherRoom,room);
    assertEquals(room.getClass(),roomList.getRoomByIndex(index).getClass());
    assertNotEquals(room.hashCode(),roomList.getRoomByIndex(index).hashCode());
    checkRoomEquals(room,roomList.getRoomByIndex(index));
  }

  @Test void updateRoomException()
  {
    //no exception
  }

  @Test void updateRoomTypeZero()
  {
    assertDoesNotThrow(()-> roomList.updateRoomType(null));
    addNewRoomMany();
    assertDoesNotThrow(()-> roomList.updateRoomType(null));
  }

  @Test void updateRoomTypeOne()
  {
    addNewRoomMany();
    int index = randomNumber(roomList.getSize());
    RoomInterface room = roomList.copy().getRoomByIndex(index);
    room.setType("New " + room.getType());
    roomList.updateRoomType(room);
    assertNotEquals(room.hashCode(),roomList.getRoomByIndex(index).hashCode());
    checkRoomEquals(room,roomList.getRoomByIndex(index));
  }

  @Test void updateRoomTypeMany()
  {
    addNewRoomMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(roomList.getSize());
      RoomInterface room = roomList.copy().getRoomByIndex(index);
      room.setType("New " + room.getType());
      roomList.updateRoom(roomList.copy().getRoomByIndex(index),room);
      assertEquals(room.getClass(),roomList.getRoomByIndex(index).getClass());
      assertNotEquals(room.hashCode(),roomList.getRoomByIndex(index).hashCode());
      checkRoomEquals(room,roomList.getRoomByIndex(index));
    }
  }

  @Test void updateRoomTypeBoundary()
  {
    addNewRoomOne();
    RoomInterface room = roomList.copy().getRoomByIndex(0);
    RoomInterface otherRoom = roomList.copy().getRoomByIndex(0);
    otherRoom.setRoomNumber(room.getRoomNumber()+1);
    otherRoom.setType("New " + room.getType());
    roomList.updateRoomType(otherRoom);
    checkRoomEquals(room,roomList.getRoomByIndex(0));
  }

  @Test void updateRoomTypeException()
  {
    //no exception
  }

  @Test void updateRoomBookedTimeRangeZero()
  {
    assertDoesNotThrow(()-> roomList.updateRoomBookedTimeRange(null));
    addNewRoomMany();
    assertDoesNotThrow(()-> roomList.updateRoomBookedTimeRange(null));
  }

  @Test void updateRoomBookedTimeRangeOne()
  {
    addNewRoomMany();
    int index = randomNumber(roomList.getSize());
    RoomInterface room = roomList.copy().getRoomByIndex(index);
    room.addBookedTimeRange(new TimeRange(randomTime(),randomTime()));
    roomList.updateRoomBookedTimeRange(room);
    assertNotEquals(room.hashCode(),roomList.getRoomByIndex(index).hashCode());
    checkRoomEquals(room,roomList.getRoomByIndex(index));
  }

  @Test void updateRoomBookedTimeRangeMany()
  {
    addNewRoomMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(roomList.getSize());
      RoomInterface room = roomList.copy().getRoomByIndex(index);
      room.addBookedTimeRange(new TimeRange(randomTime(),randomTime()));
      roomList.updateRoomBookedTimeRange(room);
      assertNotEquals(room.hashCode(),roomList.getRoomByIndex(index).hashCode());
      checkRoomEquals(room,roomList.getRoomByIndex(index));
    }
  }

  @Test void updateRoomBookedTimeRangeBoundary()
  {
    addNewRoomOne();
    RoomInterface room = roomList.copy().getRoomByIndex(0);
    RoomInterface otherRoom = roomList.copy().getRoomByIndex(0);
    otherRoom.setRoomNumber(room.getRoomNumber()+1);
    otherRoom.addBookedTimeRange(new TimeRange(randomTime(),randomTime()));
    roomList.updateRoomBookedTimeRange(otherRoom);
    checkRoomEquals(room,roomList.getRoomByIndex(0));
  }

  @Test void updateRoomBookedTimeRangeException()
  {
    //no exception
  }

  @Test void getRoomZero()
  {
    assertNull(roomList.getRoom(null));
  }

  @Test void getRoomOne()
  {
    addNewRoomOne();
  }

  @Test void getRoomMany()
  {
    addNewRoomMany();
  }

  @Test void getRoomBoundary()
  {
    addNewRoomMany();
    assertNull(roomList.getRoom(new Room(-1,null)));
  }

  @Test void getRoomException()
  {
    //no exception
  }

  @Test void getRoomByRoomNumberZero()
  {
    addNewRoomMany();
    assertNull(roomList.getRoomByRoomNumber(-1));
  }

  @Test void getRoomByRoomNumberOne()
  {
    addNewRoomMany();
    int index = randomNumber(roomList.getSize());
    RoomInterface room = roomList.getRoomByIndex(index);
    checkRoomEquals(room,roomList.getRoomByRoomNumber(room.getRoomNumber()));
  }

  @Test void getRoomByRoomNumberMany()
  {
    addNewRoomMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(roomList.getSize());
      RoomInterface room = roomList.getRoomByIndex(index);
      checkRoomEquals(room, roomList.getRoomByRoomNumber(room.getRoomNumber()));
    }
  }

  @Test void getRoomByRoomNumberBoundary()
  {
    addNewRoomMany();
    assertNull(roomList.getRoomByRoomNumber(-randomNumber()));
  }

  @Test void getRoomByRoomNumberException()
  {
    //no exception
  }

  @Test void getRoomByIndexZero()
  {
    addNewRoomMany();
    assertNull(roomList.getRoomByIndex(-1));
  }

  @Test void getRoomByIndexOne()
  {
    addNewRoomOne();
  }

  @Test void getRoomByIndexMany()
  {
    addNewRoomMany();
  }

  @Test void getRoomByIndexBoundary()
  {
    addNewRoomMany();
    assertNull(roomList.getRoomByIndex(-1));
    assertNull(roomList.getRoomByIndex(roomList.getSize()));
  }

  @Test void getRoomByIndexException()
  {
    //no exception
  }

  @Test void getRoomsByTypeZero()
  {
    addNewRoomMany();
    assertEquals(0,roomList.getRoomsByType(null).getSize());
  }

  @Test void getRoomsByTypeOne()
  {
    ArrayList<RoomInterface> roomList = new ArrayList<>();
    for (int x=0;x<randomNumber();x++)
    {
      RoomInterface room = new Room(randomNumber(),"Target Type 1");
      while (this.roomList.getRoomByRoomNumber(room.getRoomNumber())!=null)
      {
        room = new Room(randomNumber(),"Target Type 1");
      }
      this.roomList.addNewRoom(room);
      roomList.add(room);
    }
    for (int x=0;x<randomNumber();x++)
    {
      RoomInterface room = new Room(randomNumber(),"Type " + randomNumber());
      while (this.roomList.getRoomByRoomNumber(room.getRoomNumber())!=null)
      {
        room = new Room(randomNumber(),"Type " + randomNumber());
      }
      this.roomList.addNewRoom(room);
    }
    RoomList searchRoomList = this.roomList.getRoomsByType("Target Type 1");
    for (int x=0;x<searchRoomList.getSize();x++)
    {
      checkRoomEquals(searchRoomList.getRoomByIndex(x),roomList.get(x));
    }
  }

  @Test void getRoomsByTypeMany()
  {
    ArrayList<ArrayList<RoomInterface>> roomListList = new ArrayList<>();
    int listTime = randomNumber();
    for (int x=0;x<listTime;x++)
    {
      roomListList.add(new ArrayList<>());
      int time = randomNumber(100,1);
      for (int i=0;i<time;i++)
      {
        RoomInterface room = new Room(randomNumber(10000),"Target Type " + x);
        while (this.roomList.getRoomByRoomNumber(room.getRoomNumber())!=null)
        {
          room = new Room(randomNumber(10000),"Target Type " + x);
        }
        System.out.println(room);
        this.roomList.addNewRoom(room);
        roomListList.get(x).add(room);
      }
    }
    for (int x=0;x<roomListList.size();x++)
    {
      RoomList searchRoomList = this.roomList.getRoomsByType("Target Type " + x);
      for (int i=0;i<searchRoomList.getSize();i++)
      {
        checkRoomEquals(searchRoomList.getRoomByIndex(i),roomListList.get(x).get(i));
      }
    }
  }

  @Test void getRoomsByTypeBoundary()
  {
    //no boundary
  }

  @Test void getRoomsByTypeException()
  {
    //no exception
  }

  @Test void getRoomsByFreeTimeRangeListZero()
  {
    addNewRoomMany();
    assertEquals(roomList.getSize(),roomList.getRoomsByFreeTimeRangeList(null).getSize());
  }

  @Test void getRoomsByFreeTimeRangeListOne()
  {
    addNewRoomMany();
    TimeRangeList timeRangeList = new TimeRangeList();
    int time = randomNumber(roomList.getSize(),1);
    TimeRange timeRange = new TimeRange(randomTime(),randomTime());
    timeRangeList.addTimeRange(timeRange);
    for (int x=0;x<time;x++)
    {
      RoomInterface room = roomList.copy().getRoomByIndex(x);
      room.addBookedTimeRange(timeRangeList.getTimeRangeByIndex(0));
      roomList.updateRoomBookedTimeRange(room);
    }
    assertEquals(roomList.getSize() - time,roomList.getRoomsByFreeTimeRangeList(timeRangeList).getSize());
    for (int x=0;x<(roomList.getSize()-time);x++)
    {
      checkRoomEquals(roomList.getRoomByIndex(x+time),roomList.getRoomsByFreeTimeRangeList(timeRangeList).getRoomByIndex(x));
    }
  }

  @Test void getRoomsByFreeTimeRangeListBoundary()
  {
    //no boundary
  }

  @Test void getRoomsByFreeTimeRangeListException()
  {
    //no exception
  }

  @Test void removeRoomZero()
  {
    assertDoesNotThrow(()-> roomList.removeRoom(null));
    addNewRoomMany();
    RoomList roomListCopy = roomList.copy();
    assertDoesNotThrow(()-> roomList.removeRoom(null));
    for (int x=0;x<roomList.getSize();x++)
    {
      checkRoomEquals(roomListCopy.getRoomByIndex(x),roomList.getRoomByIndex(x));
    }
  }

  @Test void removeRoomOne()
  {
    addNewRoomMany();
    int index = randomNumber(roomList.getSize());
    RoomInterface room = roomList.getRoomByIndex(index);
    checkRoomEquals(room,roomList.getRoom(room));
    roomList.removeRoom(room);
    assertNull(roomList.getRoom(room));
  }

  @Test void removeRoomMany()
  {
    addNewRoomMany();
    int time = randomNumber(roomList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(roomList.getSize());
      RoomInterface room = roomList.getRoomByIndex(index);
      checkRoomEquals(room,roomList.getRoom(room));
      roomList.removeRoom(room);
      assertNull(roomList.getRoom(room));
    }
  }

  @Test void removeRoomBoundary()
  {
    addNewRoomMany();
    RoomInterface room = new Room(-1,"");
    RoomList roomListCopy = roomList.copy();
    roomList.removeRoom(room);
    for (int x=0;x<roomList.getSize();x++)
    {
      checkRoomEquals(roomListCopy.getRoomByIndex(x),roomList.getRoomByIndex(x));
    }
  }

  @Test void removeRoomException()
  {
    //no exception
  }

  @Test void removeRoomByRoomNumberZero()
  {
    assertDoesNotThrow(()-> roomList.removeRoomByRoomNumber(-1));
    addNewRoomMany();
    RoomList roomListCopy = roomList.copy();
    assertDoesNotThrow(()-> roomList.removeRoomByRoomNumber(-1));
    for (int x=0;x<roomList.getSize();x++)
    {
      checkRoomEquals(roomListCopy.getRoomByIndex(x),roomList.getRoomByIndex(x));
    }
  }

  @Test void removeRoomByRoomNumberOne()
  {
    addNewRoomMany();
    int index = randomNumber(roomList.getSize());
    RoomInterface room = roomList.getRoomByIndex(index);
    checkRoomEquals(room,roomList.getRoom(room));
    roomList.removeRoomByRoomNumber(room.getRoomNumber());
    assertNull(roomList.getRoom(room));
  }

  @Test void removeRoomByRoomNumberMany()
  {
    addNewRoomMany();
    int time = randomNumber(roomList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(roomList.getSize());
      RoomInterface room = roomList.getRoomByIndex(index);
      checkRoomEquals(room,roomList.getRoom(room));
      roomList.removeRoomByRoomNumber(room.getRoomNumber());
      assertNull(roomList.getRoom(room));
    }
  }

  @Test void removeRoomByRoomNumberBoundary()
  {
    //no boundary
  }

  @Test void removeRoomByRoomNumberException()
  {
    //no exception
  }

  @Test void removeRoomByIndexZero()
  {
    assertDoesNotThrow(()-> roomList.removeRoomByIndex(-1));
    addNewRoomMany();
    RoomList roomListCopy = roomList.copy();
    assertDoesNotThrow(()-> roomList.removeRoomByIndex(-1));
    for (int x=0;x<roomList.getSize();x++)
    {
      checkRoomEquals(roomListCopy.getRoomByIndex(x),roomList.getRoomByIndex(x));
    }
  }

  @Test void removeRoomByIndexOne()
  {
    addNewRoomMany();
    int index = randomNumber(roomList.getSize());
    RoomInterface room = roomList.getRoomByIndex(index);
    checkRoomEquals(room,roomList.getRoom(room));
    roomList.removeRoomByIndex(index);
    assertNull(roomList.getRoom(room));
  }

  @Test void removeRoomByIndexMany()
  {
    addNewRoomMany();
    int time = randomNumber(roomList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(roomList.getSize());
      RoomInterface room = roomList.getRoomByIndex(index);
      checkRoomEquals(room,roomList.getRoom(room));
      roomList.removeRoomByIndex(index);
      assertNull(roomList.getRoom(room));
    }
  }

  @Test void removeRoomByIndexBoundary()
  {
    addNewRoomMany();
    RoomList roomListCopy = roomList.copy();
    assertDoesNotThrow(()-> roomList.removeRoomByIndex(roomList.getSize()));
    for (int x=0;x<roomList.getSize();x++)
    {
      checkRoomEquals(roomListCopy.getRoomByIndex(x),roomList.getRoomByIndex(x));
    }
  }

  @Test void removeRoomByIndexException()
  {
    //no exception
  }

  @Test void removeRoomsByTypeZero()
  {
    assertDoesNotThrow(()-> roomList.removeRoomsByType(null));
    addNewRoomMany();
    RoomList roomListCopy = roomList.copy();
    assertDoesNotThrow(()-> roomList.removeRoomsByType(null));
    for (int x=0;x<roomList.getSize();x++)
    {
      checkRoomEquals(roomListCopy.getRoomByIndex(x),roomList.getRoomByIndex(x));
    }
  }

  @Test void removeRoomsByTypeOne()
  {
    ArrayList<RoomInterface> roomList = new ArrayList<>();
    for (int x=0;x<randomNumber();x++)
    {
      RoomInterface room = new Room(randomNumber(),"Target Type 1");
      while (this.roomList.getRoomByRoomNumber(room.getRoomNumber())!=null)
      {
        room = new Room(randomNumber(),"Target Type 1");
      }
      this.roomList.addNewRoom(room);
      roomList.add(room);
    }
    for (int x=0;x<randomNumber();x++)
    {
      RoomInterface room = new Room(randomNumber(),"Type " + randomNumber());
      while (this.roomList.getRoomByRoomNumber(room.getRoomNumber())!=null)
      {
        room = new Room(randomNumber(),"Type " + randomNumber());
      }
      this.roomList.addNewRoom(room);
    }
    this.roomList.removeRoomsByType("Target Type 1");
    for (int x=0;x<roomList.size();x++)
    {
      assertNull(this.roomList.getRoom(roomList.get(x)));
    }
  }

  @Test void removeRoomsByTypeMany()
  {
    ArrayList<ArrayList<RoomInterface>> roomListList = new ArrayList<>();
    int listTime = randomNumber(100,1);
    for (int x=0;x<listTime;x++)
    {
      roomListList.add(new ArrayList<>());
      int time = randomNumber(100,1);
      for (int i=0;i<time;i++)
      {
        RoomInterface room = new Room(randomNumber(10000),"Target Type " + x);
        while (this.roomList.getRoomByRoomNumber(room.getRoomNumber())!=null)
        {
          room = new Room(randomNumber(10000),"Target Type " + x);
        }
        this.roomList.addNewRoom(room);
        roomListList.get(x).add(room);
      }
    }
    for (int x=0;x<roomListList.size();x++)
    {
      roomList.removeRoomsByType("Target Type " + x);
      for (int i=0;i<roomListList.get(x).size();i++)
      {
        assertNull(roomList.getRoom(roomListList.get(x).get(i)));
      }
    }
  }

  @Test void removeRoomsByTypeBoundary()
  {
    //no boundary
  }

  @Test void removeRoomsByTypeException()
  {
    //no exception
  }

  @Test void copyZero()
  {
    RoomList other = roomList.copy();
    assertNotEquals(roomList,other);
  }

  @Test void copyOne()
  {
    roomList.addNewRoom(new Room(randomNumber(),"Type " + randomNumber()));
    RoomList other = roomList.copy();
    assertNotEquals(roomList,other);
    assertNotEquals(roomList.getRoomByIndex(0).hashCode(),other.getRoomByIndex(0).hashCode());
    checkRoomEquals(roomList.getRoomByIndex(0),other.getRoomByIndex(0));
  }

  @Test void copyMany()
  {
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      roomList.addNewRoom(new Room(randomNumber(),"Type " + randomNumber()));
    }
    RoomList other = roomList.copy();
    assertNotEquals(roomList,other);
    for (int x=0;x<roomList.getSize();x++)
    {
      assertNotEquals(roomList.getRoomByIndex(x).hashCode(),other.getRoomByIndex(x).hashCode());
      checkRoomEquals(roomList.getRoomByIndex(x),other.getRoomByIndex(x));
    }
  }

  @Test void copyBoundary()
  {
    //no boundary;
  }

  @Test void copyException()
  {
    //no exception
  }
}