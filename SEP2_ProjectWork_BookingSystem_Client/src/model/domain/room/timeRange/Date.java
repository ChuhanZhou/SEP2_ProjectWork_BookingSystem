package model.domain.room.timeRange;

public class Date
{
  private int month;
  private int day;
  private int year;

  public Date(int day, int month, int year)
  {
    this.month = Math.max(Math.min(month, 12), 1);
    this.day = Math.max(Math.min(day, numberOfDaysInMonth(this.month)), 1);
    this.year = Math.max(Math.min(year,9999), 1);
  }

  public void set(int day, int month, int year)
  {
    this.month = Math.max(Math.min(month, 12), 1);
    this.day = Math.max(Math.min(day, numberOfDaysInMonth(this.month)), 1);
    this.year = Math.max(Math.min(year,9999), 1);
  }

  public int getDay()
  {
    return day;
  }

  public int getMonth()
  {
    return month;
  }

  public int getYear()
  {
    return year;
  }

  public String getMonthName()
  {
    switch (month)
    {
      default:
        return "Wrong!";

      case 1:
        return "January";

      case 2:
        return "February";

      case 3:
        return "March";

      case 4:
        return "April";

      case 5:
        return "May";

      case 6:
        return "June";

      case 7:
        return "July";

      case 8:
        return "August";

      case 9:
        return "September";

      case 10:
        return "October";

      case 11:
        return "November";

      case 12:
        return "December";
    }
  }

  public boolean isLeapYear()
  {
    return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
  }

  public int numberOfDaysInMonth(int monthNumber)
  {
    switch (monthNumber)
    {
      default:
        return -1;

      case 1:
        return 31;

      case 2:
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
        {
          return 29;
        }
        else
        {
          return 28;
        }

      case 3:
        return 31;

      case 4:
        return 30;

      case 5:
        return 31;

      case 6:
        return 30;

      case 7:
        return 31;

      case 8:
        return 31;

      case 9:
        return 30;

      case 10:
        return 31;

      case 11:
        return 30;

      case 12:
        return 31;
    }
  }

  public void stepForwardOneDay()
  {
    Date input = new Date(day, month, year);
    if (day == input.numberOfDaysInMonth(month))
    {
      day = 1;
      if (month == 12)
      {
        month = 1;
        year++;
      }
      else if (month < 12)
      {
        month++;
      }
      else
      {
        System.out.println("Wrong month!");
      }
    }
    else if (day < input.numberOfDaysInMonth(month))
    {
      day++;
    }
    else
    {
      if (month > 12 || month < 0)
      {
        System.out.println("Wrong month!");
      }
      else
      {
        System.out.println("Wrong day!");
      }
    }
  }

  public boolean equals(Date otherDate)
  {
    return year==otherDate.getYear() && month==otherDate.getMonth() && day==otherDate.getDay();
  }

  public boolean isAfter(Date otherDate)
  {
    if(getYear()>otherDate.getYear())
    {
      return true;
    }
    else if (getYear()==otherDate.getYear())
    {
      if (getMonth()>otherDate.getMonth())
      {
        return true;
      }
      else if (getMonth()==otherDate.getMonth())
      {
        if (getDay()>otherDate.getDay())
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
    else
    {
      return false;
    }
  }

  public Date copy()
  {
    Date Other = new Date(day,month,year);
    return Other;
  }

  @Override public String toString()
  {
    return String.format("%02d", day) + "/" + String.format("%02d", month) + "/"
        + String.format("%04d", year);
  }
}