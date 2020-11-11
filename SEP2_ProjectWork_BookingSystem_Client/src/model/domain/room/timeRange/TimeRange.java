package model.domain.room.timeRange;

public class TimeRange implements TimeRangeInterface
{
  private Time startTime;
  private Time endTime;

  public TimeRange(Time time1,Time time2)
  {
    if (time1.isAfter(time2))
    {
      startTime = time2;
      endTime = time1;
    }
    else
    {
      startTime = time1;
      endTime = time2;
    }
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  public void setStartTime(Time startTime)
  {
    this.startTime = startTime;
  }

  public void setEndTime(Time endTime)
  {
    this.endTime = endTime;
  }

  public boolean within(TimeRangeInterface otherTimeRange)
  {
    if (startTime.isAfter(otherTimeRange.getStartTime()) || startTime.equals(otherTimeRange.getStartTime()))
    {
      if (otherTimeRange.getEndTime().isAfter(endTime) || otherTimeRange.getEndTime().equals(endTime))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      return false;
    }
  }

  public String toString()
  {
    if (startTime.getDate().equals(endTime.getDate()))
    {
      if (startTime.getClock().equals(endTime.getClock()))
      {
        return startTime.toString();
      }
      else
      {
        return startTime.getDate().toString() + " " + startTime.getClock().toString() + "-" + endTime.getClock().toString();
      }
    }
    else
    {
      return startTime.toString() + " —— " + endTime.toString();
    }
  }

  public TimeRange copy()
  {
    TimeRange other = new TimeRange(startTime.copy(),endTime.copy());
    return other;
  }
}
