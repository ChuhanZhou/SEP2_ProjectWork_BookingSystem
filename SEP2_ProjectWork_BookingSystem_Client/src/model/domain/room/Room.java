package model.domain.room;

import model.domain.room.timeRange.TimeRangeInterface;

public class Room implements RoomInterface
{
  private int roomNumber;
  private String type;
  private TimeRangeList bookedTimeRangeList;

  public Room(int roomNumber,String type)
  {
    this.roomNumber = roomNumber;
    this.type = type;
    bookedTimeRangeList = new TimeRangeList();
  }

  public void setRoomNumber(int roomNumber)
  {
    this.roomNumber = roomNumber;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public int getRoomNumber()
  {
    return roomNumber;
  }

  public String getType()
  {
    return type;
  }

  public void addBookedTimeRange(int day1, int month1, int year1,int day2, int month2, int year2,int hour1,int hour2)
  {
    bookedTimeRangeList.addTimeRange(day1, month1, year1, day2, month2, year2, hour1, hour2);
  }

  public void addBookedTimeRange(TimeRangeInterface newTimeRange)
  {
    bookedTimeRangeList.addTimeRange(newTimeRange);
  }

  public void setBookedTimeRangeList(TimeRangeList timeRangeList)
  {
    bookedTimeRangeList = timeRangeList;
  }

  public void removeBookedTimeRange(int day1, int month1, int year1,int day2, int month2, int year2,int hour1,int hour2)
  {
    bookedTimeRangeList.removeTimeRange(day1, month1, year1, day2, month2, year2, hour1, hour2);
  }

  public void removeBookedTimeRange(TimeRangeInterface removeTimeRange)
  {
    bookedTimeRangeList.removeTimeRange(removeTimeRange);
  }

  public void removeAllBookedTimeRange()
  {
    bookedTimeRangeList.removeAllTimeRange();
  }

  public TimeRangeList getBookedTimeRangeList()
  {
    return bookedTimeRangeList;
  }

  @Override public boolean equals(Object obj)
  {
    if (obj instanceof Room)
    {
      if (((Room) obj).getRoomNumber()==roomNumber)
      {
        return true;
      }
    }
    return false;
  }

  @Override public String toString()
  {
    return "Room number: " + roomNumber + "\nRoom type: " + type;
  }

  public Room copy()
  {
    Room other = new Room(roomNumber,type);
    other.setBookedTimeRangeList(bookedTimeRangeList.copy());
    return other;
  }
}
