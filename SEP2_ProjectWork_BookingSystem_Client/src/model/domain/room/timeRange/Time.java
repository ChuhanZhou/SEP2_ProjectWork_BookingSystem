package model.domain.room.timeRange;

public class Time
{
  private Clock clock;
  private Date date;

  public Time(int day,int month,int year,int hour,int minute)
  {
    clock = new Clock(hour,minute,0);
    date = new Date(day,month,year);
  }

  public void setClock(int hour,int minute)
  {
    clock.set(hour,minute,0);
  }

  public void setDate(int day,int month,int year)
  {
    date.set(day,month,year);
  }

  public Clock getClock()
  {
    return clock;
  }

  public Date getDate()
  {
    return date;
  }

  public boolean equals(Time otherTime)
  {
    return date.equals(otherTime.getDate()) && clock.equals(otherTime.getClock());
  }

  public boolean isAfter(Time otherTime)
  {
    if (date.isAfter(otherTime.getDate()))
    {
      return true;
    }
    else
    {
      if (otherTime.getDate().isAfter(date.copy()))
      {
        return false;
      }
      else
      {
        return clock.isAfter(otherTime.getClock());
      }
    }
  }

  public Time copy()
  {
    Time other = new Time(date.getDay(),date.getMonth(),date.getYear(),clock.getHour(),clock.getMinute());
    return other;
  }

  public String toString()
  {
    return date.toString() + "-" + clock.toString();
  }
}
