package model.domain.room;

import model.domain.room.timeRange.Time;
import model.domain.room.timeRange.TimeRange;
import model.domain.room.timeRange.TimeRangeInterface;

import java.util.ArrayList;

public class TimeRangeList
{
  private ArrayList<TimeRange> timeRangeList;

  public TimeRangeList()
  {
    timeRangeList = new ArrayList<>();
  }

  public void addTimeRange(int day1, int month1, int year1,int day2, int month2, int year2,int hour1,int hour2)
  {
    Time time1 = new Time(day1,month1,year1,hour1,0);
    Time time2 = new Time(day2,month2,year2,hour2,0);
    addTimeRange(new TimeRange(time1,time2));
  }

  public void addTimeRange(TimeRangeInterface newTimeRange)
  {
    if (!hasTimeRange(newTimeRange)&&newTimeRange!=null)
    {
      timeRangeList.add(newTimeRange.copy());
    }
  }

  public void removeTimeRange(int day1, int month1, int year1,int day2, int month2, int year2,int hour1,int hour2)
  {
    Time time1 = new Time(day1,month1,year1,hour1,0);
    Time time2 = new Time(day2,month2,year2,hour2,0);
    TimeRangeInterface removeTimeRange = new TimeRange(time1,time2);
    removeTimeRange(removeTimeRange);
  }

  public void removeTimeRange(TimeRangeInterface removeTimeRange)
  {
    if (removeTimeRange!=null)
    {
      for (int x=0;x<timeRangeList.size();x++)
      {
        if (removeTimeRange.within(timeRangeList.get(x)))
        {
          timeRangeList.remove(x);
        }
      }
    }
  }

  public void removeAllTimeRange()
  {
    timeRangeList.clear();
  }

  public boolean hasTimeRange(int day1, int month1, int year1,int day2, int month2, int year2,int hour1,int hour2)
  {
    Time time1 = new Time(day1,month1,year1,hour1,0);
    Time time2 = new Time(day2,month2,year2,hour2,0);
    TimeRangeInterface timeRange = new TimeRange(time1,time2);
    for (int x=0;x<timeRangeList.size();x++)
    {
      if (timeRangeList.get(x).within(timeRange))
      {
        return true;
      }
    }
    return false;
  }

  public boolean hasTimeRange(TimeRangeInterface timeRange)
  {
    if (timeRange!=null)
    {
      for (int x=0;x<timeRangeList.size();x++)
      {
        if (timeRange.within(timeRangeList.get(x)))
        {
          return true;
        }
      }
    }
    return false;
  }

  public TimeRangeInterface getTimeRangeByIndex(int index)
  {
    if (index<timeRangeList.size()&&index>=0)
    {
      return timeRangeList.get(index);
    }
    return null;
  }

  public Time getStartTime()
  {
    Time startTime = null;
    for (int x=0;x<timeRangeList.size();x++)
    {
      if (startTime == null)
      {
        startTime = timeRangeList.get(x).getStartTime();
      }
      if (startTime.isAfter(timeRangeList.get(x).getStartTime()))
      {
        startTime = timeRangeList.get(x).getStartTime();
      }
    }
    return startTime;
  }

  public Time getEndTime()
  {
    Time endTime = null;
    for (int x=0;x<timeRangeList.size();x++)
    {
      if (endTime == null)
      {
        endTime = timeRangeList.get(x).getEndTime();
      }
      if (timeRangeList.get(x).getEndTime().isAfter(endTime))
      {
        endTime = timeRangeList.get(x).getEndTime();
      }
    }
    return endTime;
  }

  public int getSize()
  {
    return timeRangeList.size();
  }

  public String showDateByIndex(int index)
  {
    if (getTimeRangeByIndex(index)!=null)
    {
      return timeRangeList.get(index).getStartTime().getDate().toString() + " - " + timeRangeList.get(index).getEndTime().getDate().toString();
    }
    return null;
  }

  public TimeRangeList copy()
  {
    TimeRangeList other = new TimeRangeList();
    for (int x=0;x<timeRangeList.size();x++)
    {
      other.addTimeRange(timeRangeList.get(x).copy());
    }
    return other;
  }
}
