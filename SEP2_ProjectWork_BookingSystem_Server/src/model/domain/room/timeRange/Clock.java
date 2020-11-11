package model.domain.room.timeRange;

public class Clock
{
  private int hour;
  private int minute;
  private int second;
  private boolean is24HourFormat;

  public Clock(int hour,int minute,int second)
  {
    this.hour = Math.max(Math.min(hour,23),0);
    this.minute = Math.max(Math.min(minute,59),0);
    this.second = Math.max(Math.min(second,59),0);
    is24HourFormat = true;
  }

  public Clock(int totalSeconds)
  {
    hour = Math.max(Math.min(totalSeconds/(60*60),23),0);
    minute = totalSeconds%(60*60)/60;
    second = totalSeconds%(60*60)%60;
    is24HourFormat = true;
  }

  public void set(int hour,int minute,int second)
  {
    this.hour = Math.max(Math.min(hour,23),0);
    this.minute = Math.max(Math.min(minute,59),0);
    this.second = Math.max(Math.min(second,59),0);
    is24HourFormat = true;
  }

  public int getHour()
  {
    return hour;
  }

  public int getMinute()
  {
    return minute;
  }

  public int getSecond()
  {
    return second;
  }

  public void set24HourFormat()
  {
    is24HourFormat = true;
  }

  public void set12HourFormat()
  {
    is24HourFormat = false;
  }

  public boolean is24HourFormat()
  {
    return is24HourFormat;
  }

  public int convertToSeconds()
  {
    return hour*60*60 + minute*60 + second;
  }

  public boolean isAfter(Clock otherClock)
  {
    return convertToSeconds()>otherClock.convertToSeconds();
  }

  public Clock copy()
  {
    Clock Other = new Clock(hour,minute,second);
    return Other;
  }

  public boolean equals(Clock otherClock)
  {
    return hour==otherClock.getHour() && minute==otherClock.getMinute() && second==otherClock.getSecond();
  }

  public String toString()
  {
    if (second == 0)
    {
      if (is24HourFormat)
      {
        return String.format("%02d",hour) + ":" + String.format("%02d",minute);
      }
      else
      {
        if (hour>11)
        {
          return hour-12 + ":" + String.format("%02d",minute) + " PM";
        }
        else
        {
          return hour + ":" + String.format("%02d",minute)+ " AM";
        }
      }
    }
    else
    {
      if (is24HourFormat)
      {
        return String.format("%02d", hour) + ":" + String.format("%02d", minute)
            + ":" + String.format("%02d", second);
      }
      else
      {
        if (hour > 11)
        {
          return hour - 12 + ":" + String.format("%02d", minute) + ":" + String
              .format("%02d", second) + " PM";
        }
        else
        {
          return hour + ":" + String.format("%02d", minute) + ":" + String
              .format("%02d", second) + " AM";
        }
      }
    }
  }
}
