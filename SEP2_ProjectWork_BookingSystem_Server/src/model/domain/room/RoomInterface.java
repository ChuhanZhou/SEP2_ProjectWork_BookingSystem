package model.domain.room;

import model.domain.room.timeRange.TimeRangeInterface;

public interface RoomInterface
{
  void setRoomNumber(int roomNumber);
  void setType(String type);
  int getRoomNumber();
  String getType();
  void addBookedTimeRange(int day1, int month1, int year1,int day2, int month2, int year2,int hour1,int hour2);
  void addBookedTimeRange(TimeRangeInterface newTimeRange);
  void setBookedTimeRangeList(TimeRangeList timeRangeList);
  void removeBookedTimeRange(int day1, int month1, int year1,int day2, int month2, int year2,int hour1,int hour2);
  void removeBookedTimeRange(TimeRangeInterface removeTimeRange);
  void removeAllBookedTimeRange();
  TimeRangeList getBookedTimeRangeList();
  Room copy();
  boolean equals(Object obj);
}
