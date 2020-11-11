package model.domain;

import model.domain.room.RoomInterface;
import model.domain.room.TimeRangeList;

import java.util.ArrayList;

public class RoomList
{
  private ArrayList<RoomInterface> roomList;

  public RoomList()
  {
    roomList = new ArrayList<>();
  }

  public void addNewRoom(RoomInterface newRoom)
  {
    if (newRoom!=null && getRoomByRoomNumber(newRoom.getRoomNumber())==null)
    {
      roomList.add(newRoom.copy());
    }
  }

  public void updateRoom(RoomInterface oldRoom,RoomInterface newRoom)
  {
    if (newRoom!=null||oldRoom!=null)
    {
      newRoom = newRoom.copy();
      if (getRoomByRoomNumber(oldRoom.getRoomNumber())!=null)
      {
        if (oldRoom.getRoomNumber()!=newRoom.getRoomNumber())
        {
          if (getRoomByRoomNumber(newRoom.getRoomNumber())==null)
          {
            getRoomByRoomNumber(oldRoom.getRoomNumber()).setType(newRoom.getType());
            getRoomByRoomNumber(oldRoom.getRoomNumber()).setBookedTimeRangeList(newRoom.getBookedTimeRangeList());
            getRoomByRoomNumber(oldRoom.getRoomNumber()).setRoomNumber(newRoom.getRoomNumber());
          }
        }
        else
        {
          getRoomByRoomNumber(oldRoom.getRoomNumber()).setType(newRoom.getType());
          getRoomByRoomNumber(oldRoom.getRoomNumber()).setBookedTimeRangeList(newRoom.getBookedTimeRangeList());
        }
      }
    }
  }

  public void updateRoomType(RoomInterface newRoom)
  {
    if (newRoom!=null)
    {
      newRoom = newRoom.copy();
      if (getRoomByRoomNumber(newRoom.getRoomNumber()) != null)
      {
        getRoomByRoomNumber(newRoom.getRoomNumber()).setType(newRoom.getType());
      }
    }
  }

  public void updateRoomBookedTimeRange(RoomInterface newRoom)
  {
    if (newRoom!=null)
    {
      newRoom = newRoom.copy();
      if (getRoomByRoomNumber(newRoom.getRoomNumber()) != null)
      {
        getRoomByRoomNumber(newRoom.getRoomNumber()).setBookedTimeRangeList(newRoom.getBookedTimeRangeList());
      }
    }
  }

  public RoomInterface getRoom(RoomInterface room)
  {
    for (int x=0;x<roomList.size();x++)
    {
      if (roomList.get(x).equals(room))
      {
        return roomList.get(x);
      }
    }
    return null;
  }

  public RoomInterface getRoomByRoomNumber(int roomNumber)
  {
    for (int x=0;x<roomList.size();x++)
    {
      if (roomList.get(x).getRoomNumber()==roomNumber)
      {
        return roomList.get(x);
      }
    }
    return null;
  }

  public RoomInterface getRoomByIndex(int index)
  {
    if (index<roomList.size()&&index>=0)
    {
      return roomList.get(index);
    }
    return null;
  }

  public RoomList getRoomsByType(String type)
  {
    RoomList newRoomList = new RoomList();
    for (int x=0;x<roomList.size();x++)
    {
      if (roomList.get(x).getType().equals(type))
      {
        newRoomList.addNewRoom(roomList.get(x));
      }
    }
    return newRoomList;
  }

  public RoomList getRoomsByFreeTimeRangeList(TimeRangeList timeRangeList)
  {
    RoomList newRoomList = this.copy();
    if (timeRangeList!=null)
    {
      for (int x=0;x<roomList.size();x++)
      {
        for (int i=0;i<timeRangeList.getSize();i++)
        {
          if (roomList.get(x).getBookedTimeRangeList().hasTimeRange(timeRangeList.getTimeRangeByIndex(i)))
          {
            newRoomList.removeRoom(roomList.get(x));
          }
        }
      }
    }
    return newRoomList;
  }

  public void removeRoom(RoomInterface room)
  {
    for (int x=0;x<roomList.size();x++)
    {
      if (roomList.get(x).equals(room))
      {
        roomList.remove(x);
        break;
      }
    }
  }

  public void removeRoomByRoomNumber(int roomNumber)
  {
    for (int x=0;x<roomList.size();x++)
    {
      if (roomList.get(x).getRoomNumber()==roomNumber)
      {
        roomList.remove(x);
        break;
      }
    }
  }

  public void removeRoomByIndex(int index)
  {
    if (index<roomList.size()&&index>=0)
    {
      roomList.remove(index);
    }
  }

  public void removeRoomsByType(String type)
  {
    ArrayList<RoomInterface> newRoomList = new ArrayList<>();
    for (int x=0;x<roomList.size();x++)
    {
      if (!roomList.get(x).getType().equals(type))
      {
        newRoomList.add(roomList.get(x));
      }
    }
    roomList = newRoomList;
  }

  public RoomList copy()
  {
    RoomList other = new RoomList();
    for (int x=0;x<roomList.size();x++)
    {
      other.addNewRoom(roomList.get(x).copy());
    }
    return other;
  }

  public int getSize()
  {
    return roomList.size();
  }
}
