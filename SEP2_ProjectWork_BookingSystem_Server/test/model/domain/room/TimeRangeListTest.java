package model.domain.room;

import model.domain.room.timeRange.Time;
import model.domain.room.timeRange.TimeRange;
import model.domain.room.timeRange.TimeRangeInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TimeRangeListTest
{
  private TimeRangeList timeRangeList;

  private int randomNumber()
  {
    return (int) (Math.random() * 50 + 0);
  }

  private Time randomTime()
  {
    return new Time(randomNumber(), randomNumber(), randomNumber(),
        randomNumber(), randomNumber());
  }

  private void checkTimeRangeEquals(TimeRangeInterface timeRange1,TimeRangeInterface timeRange2)
  {
    assertEquals(timeRange1.getStartTime().toString(),timeRange2.getStartTime().toString());
    assertEquals(timeRange1.getEndTime().toString(),timeRange2.getEndTime().toString());
  }

  @BeforeEach void setUp()
  {
    timeRangeList = new TimeRangeList();
  }

  @AfterEach void tearDown()
  {
  }

  @Test void addTimeRangeZero()
  {
    timeRangeList.addTimeRange(null);
    assertEquals(0, timeRangeList.getSize());
  }

  @Test void addTimeRangeOne()
  {
    TimeRange timeRange = new TimeRange(randomTime(), randomTime());
    timeRangeList.addTimeRange(timeRange);
    checkTimeRangeEquals(timeRange, timeRangeList.getTimeRangeByIndex(0));

    timeRangeList.removeAllTimeRange();
    timeRange.getStartTime().setClock(randomNumber(),0);
    timeRange.getEndTime().setClock(randomNumber(),0);
    timeRangeList.addTimeRange(timeRange.getStartTime().getDate().getDay(),
        timeRange.getStartTime().getDate().getMonth(),
        timeRange.getStartTime().getDate().getYear(),
        timeRange.getEndTime().getDate().getDay(),
        timeRange.getEndTime().getDate().getMonth(),
        timeRange.getEndTime().getDate().getYear(),
        timeRange.getStartTime().getClock().getHour(),
        timeRange.getEndTime().getClock().getHour());
    assertEquals(timeRange.toString(), timeRangeList.getTimeRangeByIndex(0).toString());
  }

  @Test void addTimeRangeMany()
  {
    ArrayList<TimeRange> timeRanges = new ArrayList<>();
    int time = randomNumber();
    for (int x = 0; x < time; x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      if (!timeRangeList.hasTimeRange(timeRange))
      {
        timeRanges.add(timeRange);
        timeRangeList.addTimeRange(timeRange);
      }
    }
    for (int x = 0; x < timeRanges.size(); x++)
    {
      checkTimeRangeEquals(timeRanges.get(x), timeRangeList.getTimeRangeByIndex(x));
    }

    timeRanges = new ArrayList<>();
    timeRangeList.removeAllTimeRange();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      timeRange.getStartTime().setClock(randomNumber(),0);
      timeRange.getEndTime().setClock(randomNumber(),0);
      if (!timeRangeList.hasTimeRange(timeRange))
      {
        timeRangeList.addTimeRange(timeRange.getStartTime().getDate().getDay(),
            timeRange.getStartTime().getDate().getMonth(),
            timeRange.getStartTime().getDate().getYear(),
            timeRange.getEndTime().getDate().getDay(),
            timeRange.getEndTime().getDate().getMonth(),
            timeRange.getEndTime().getDate().getYear(),
            timeRange.getStartTime().getClock().getHour(),
            timeRange.getEndTime().getClock().getHour());
        timeRanges.add(timeRange);
      }
    }
    for (int x=0;x<timeRanges.size();x++)
    {
      assertEquals(timeRanges.get(x).toString(), timeRangeList.getTimeRangeByIndex(x).toString());
    }
  }

  @Test void addTimeRangeBoundary()
  {
    timeRangeList.addTimeRange(99999,99999,99999,99999,99999,99999,99999,99999);
    assertEquals("31/12/9999-23:00",timeRangeList.getTimeRangeByIndex(0).toString());
    timeRangeList.addTimeRange(-1,-1,-1,-1,-1,-1,-1,-1);
    assertEquals("01/01/0001-00:00",timeRangeList.getTimeRangeByIndex(1).toString());
    TimeRange timeRange = new TimeRange(randomTime(),randomTime());
    timeRangeList.addTimeRange(timeRange);
    assertEquals(3,timeRangeList.getSize());
    timeRangeList.addTimeRange(timeRange);
    assertEquals(3,timeRangeList.getSize());
    TimeRange otherTimeRange = timeRange.copy();
    while (!otherTimeRange.getStartTime().isAfter(timeRange.getEndTime()))
    {
      otherTimeRange.getStartTime().getDate().stepForwardOneDay();
      otherTimeRange.getEndTime().getDate().stepForwardOneDay();
    }
    timeRangeList.addTimeRange(otherTimeRange);
    assertEquals(4,timeRangeList.getSize());
    timeRangeList.addTimeRange(otherTimeRange);
    assertEquals(4,timeRangeList.getSize());
  }

  @Test void addTimeRangeException()
  {
    //no exception
  }

  @Test void removeTimeRangeZero()
  {
    assertEquals(0,timeRangeList.getSize());
    timeRangeList.removeTimeRange(null);
    assertEquals(0,timeRangeList.getSize());
  }

  @Test void removeTimeRangeOne()
  {
    assertEquals(0,timeRangeList.getSize());
    TimeRange timeRange = new TimeRange(randomTime(), randomTime());
    timeRangeList.addTimeRange(timeRange);
    assertEquals(1,timeRangeList.getSize());
    timeRangeList.removeTimeRange(timeRange);
    assertEquals(0,timeRangeList.getSize());

    timeRangeList.removeAllTimeRange();
    assertEquals(0,timeRangeList.getSize());
    timeRange.getStartTime().setClock(randomNumber(),0);
    timeRange.getEndTime().setClock(randomNumber(),0);
    timeRangeList.addTimeRange(timeRange.getStartTime().getDate().getDay(),
        timeRange.getStartTime().getDate().getMonth(),
        timeRange.getStartTime().getDate().getYear(),
        timeRange.getEndTime().getDate().getDay(),
        timeRange.getEndTime().getDate().getMonth(),
        timeRange.getEndTime().getDate().getYear(),
        timeRange.getStartTime().getClock().getHour(),
        timeRange.getEndTime().getClock().getHour());
    assertEquals(1,timeRangeList.getSize());
    timeRangeList.removeTimeRange(timeRange);
    assertEquals(0,timeRangeList.getSize());
  }

  @Test void removeTimeRangeMany()
  {
    ArrayList<TimeRange> timeRanges = new ArrayList<>();
    int time = randomNumber();
    assertEquals(0,timeRangeList.getSize());
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      while (timeRangeList.hasTimeRange(timeRange))
      {
        timeRange = new TimeRange(randomTime(), randomTime());
      }
      timeRanges.add(timeRange);
      timeRangeList.addTimeRange(timeRange);
    }
    assertEquals(time,timeRangeList.getSize());
    for (int x = 0; x < timeRanges.size(); x++)
    {
      timeRangeList.removeTimeRange(timeRanges.get(x));
    }
    assertEquals(0,timeRangeList.getSize());

    timeRanges = new ArrayList<>();
    timeRangeList.removeAllTimeRange();
    assertEquals(0,timeRangeList.getSize());
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      timeRange.getStartTime().setClock(randomNumber(),0);
      timeRange.getEndTime().setClock(randomNumber(),0);
      while (timeRangeList.hasTimeRange(timeRange))
      {
        timeRange = new TimeRange(randomTime(), randomTime());
        timeRange.getStartTime().setClock(randomNumber(),0);
        timeRange.getEndTime().setClock(randomNumber(),0);
      }
      timeRangeList.addTimeRange(timeRange.getStartTime().getDate().getDay(),
          timeRange.getStartTime().getDate().getMonth(),
          timeRange.getStartTime().getDate().getYear(),
          timeRange.getEndTime().getDate().getDay(),
          timeRange.getEndTime().getDate().getMonth(),
          timeRange.getEndTime().getDate().getYear(),
          timeRange.getStartTime().getClock().getHour(),
          timeRange.getEndTime().getClock().getHour());
      timeRanges.add(timeRange);
    }
    assertEquals(time,timeRangeList.getSize());
    for (int x=0;x<timeRanges.size();x++)
    {
      timeRangeList.removeTimeRange(timeRanges.get(x));
    }
    assertEquals(0,timeRangeList.getSize());
  }

  @Test void removeTimeRangeBoundary()
  {
    assertEquals(0,timeRangeList.getSize());
    timeRangeList.addTimeRange(31,12,9999,31,12,9999,23,23);
    assertEquals(1,timeRangeList.getSize());
    timeRangeList.removeTimeRange(99999,99999,99999,99999,99999,99999,99999,99999);
    assertEquals(0,timeRangeList.getSize());
    timeRangeList.addTimeRange(1,1,1,1,1,1,0,0);
    assertEquals(1,timeRangeList.getSize());
    timeRangeList.removeTimeRange(-1,-1,-1,-1,-1,-1,-1,-1);
    assertEquals(0,timeRangeList.getSize());
    TimeRange timeRange = new TimeRange(randomTime(),randomTime());
    timeRangeList.addTimeRange(timeRange);
    assertEquals(1,timeRangeList.getSize());
    timeRangeList.removeTimeRange(null);
    assertEquals(1,timeRangeList.getSize());
    TimeRange timeRange1 = new TimeRange(randomTime(),randomTime());
    while (timeRangeList.hasTimeRange(timeRange1))
    {
      timeRange1 = new TimeRange(randomTime(),randomTime());
    }
    timeRangeList.removeTimeRange(timeRange1);
    assertEquals(1,timeRangeList.getSize());
  }

  @Test void removeTimeRangeException()
  {
    //no exception
  }

  @Test void removeAllTimeRangeZero()
  {
    assertEquals(0,timeRangeList.getSize());
    timeRangeList.addTimeRange(null);
    assertEquals(0,timeRangeList.getSize());
    timeRangeList.removeAllTimeRange();
    assertEquals(0,timeRangeList.getSize());
  }

  @Test void removeAllTimeRangeOne()
  {
    assertEquals(0,timeRangeList.getSize());
    timeRangeList.addTimeRange(new TimeRange(randomTime(),randomTime()));
    assertEquals(1,timeRangeList.getSize());
    timeRangeList.removeAllTimeRange();
    assertEquals(0,timeRangeList.getSize());
  }

  @Test void removeAllTimeRangeMany()
  {
    assertEquals(0,timeRangeList.getSize());
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      while (timeRangeList.hasTimeRange(timeRange))
      {
        timeRange = new TimeRange(randomTime(), randomTime());
      }
      timeRangeList.addTimeRange(timeRange);
    }
    assertEquals(time,timeRangeList.getSize());
    timeRangeList.removeAllTimeRange();
    assertEquals(0,timeRangeList.getSize());
  }

  @Test void removeAllTimeRangeBoundary()
  {
    //no boundary
  }

  @Test void removeAllTimeRangeException()
  {
    //no exception
  }

  @Test void hasTimeRangeZero()
  {
    assertFalse(timeRangeList.hasTimeRange(null));
    timeRangeList.addTimeRange(new TimeRange(randomTime(),randomTime()));
    assertFalse(timeRangeList.hasTimeRange(null));
  }

  @Test void hasTimeRangeOne()
  {
    timeRangeList.addTimeRange(99999,99999,99999,-1,-1,-1,99999,-1);
    assertTrue(timeRangeList.hasTimeRange(new TimeRange(randomTime(),randomTime())));
    timeRangeList.removeAllTimeRange();
    timeRangeList.addTimeRange(-1,-1,-1,-1,-1,-1,-1,-1);
    assertFalse(timeRangeList.hasTimeRange(new TimeRange(randomTime(),randomTime())));
  }

  @Test void hasTimeRangeMany()
  {
    ArrayList<TimeRange> timeRanges = new ArrayList<>();
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      timeRangeList.addTimeRange(timeRange);
      timeRanges.add(timeRange);
    }
    for (int x=0;x<timeRanges.size();x++)
    {
      assertTrue(timeRangeList.hasTimeRange(timeRanges.get(x)));
    }

    timeRanges.clear();
    time = randomNumber();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      timeRanges.add(timeRange);
      for (int i=0;i<timeRangeList.getSize();i++)
      {
        if (timeRange.within(timeRangeList.getTimeRangeByIndex(i)))
        {
          timeRanges.remove(timeRange);
          break;
        }
      }
    }
    for (int x=0;x<timeRanges.size();x++)
    {
      assertFalse(timeRangeList.hasTimeRange(timeRanges.get(x)));
    }
  }

  @Test void hasTimeRangeBoundary()
  {
    TimeRange timeRange1 = new TimeRange(randomTime(),randomTime());
    timeRangeList.addTimeRange(timeRange1);
    assertTrue(timeRangeList.hasTimeRange(new TimeRange(timeRange1.getStartTime(),timeRange1.getStartTime())));
    assertTrue(timeRangeList.hasTimeRange(new TimeRange(timeRange1.getEndTime(),timeRange1.getEndTime())));
    TimeRange timeRange2 = new TimeRange(timeRange1.getStartTime().copy(),timeRange1.getEndTime().copy());
    timeRange2.getStartTime().getDate().stepForwardOneDay();
    timeRange2.getEndTime().getDate().stepForwardOneDay();
    assertFalse(timeRangeList.hasTimeRange(timeRange2));
  }

  @Test void hasTimeRangeException()
  {
    //no exception
  }

  @Test void getTimeRangeByIndexZero()
  {
    assertNull(timeRangeList.getTimeRangeByIndex(-1));
  }

  @Test void getTimeRangeByIndexOne()
  {
    TimeRange timeRange = new TimeRange(randomTime(),randomTime());
    timeRangeList.addTimeRange(timeRange);
    assertEquals(timeRange.toString(),timeRangeList.getTimeRangeByIndex(0).toString());
  }

  @Test void getTimeRangeByIndexMany()
  {
    ArrayList<TimeRange> timeRanges = new ArrayList<>();
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      while (timeRangeList.hasTimeRange(timeRange))
      {
        timeRange = new TimeRange(randomTime(), randomTime());
      }
      timeRangeList.addTimeRange(timeRange);
      timeRanges.add(timeRange);
    }
    for (int x=0;x<timeRanges.size();x++)
    {
      assertEquals(timeRanges.get(x).toString(),timeRangeList.getTimeRangeByIndex(x).toString());
    }
  }

  @Test void getTimeRangeByIndexBoundary()
  {
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      timeRangeList.addTimeRange(timeRange);
    }
    assertNull(timeRangeList.getTimeRangeByIndex(timeRangeList.getSize()));
  }

  @Test void getTimeRangeByIndexException()
  {
    //no exception
  }

  @Test void showDateByIndexZero()
  {
    assertNull(timeRangeList.showDateByIndex(-1));
  }

  @Test void showDateByIndexOne()
  {
    TimeRange timeRange = new TimeRange(randomTime(),randomTime());
    timeRangeList.addTimeRange(timeRange);
    assertEquals(timeRange.getStartTime().getDate().toString() + " - " + timeRange.getEndTime().getDate().toString(),timeRangeList.showDateByIndex(0));
  }

  @Test void showDateByIndexMany()
  {
    ArrayList<TimeRange> timeRanges = new ArrayList<>();
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      while (timeRangeList.hasTimeRange(timeRange))
      {
        timeRange = new TimeRange(randomTime(), randomTime());
      }
      timeRangeList.addTimeRange(timeRange);
      timeRanges.add(timeRange);
    }
    for (int x=0;x<timeRanges.size();x++)
    {
      assertEquals(timeRanges.get(x).getStartTime().getDate().toString() + " - " + timeRanges.get(x).getEndTime().getDate().toString(),timeRangeList.showDateByIndex(x));
    }
  }

  @Test void showDateByIndexBoundary()
  {
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      timeRangeList.addTimeRange(timeRange);
    }
    assertNull(timeRangeList.showDateByIndex(timeRangeList.getSize()));
  }

  @Test void showDateByIndexException()
  {
    //no exception
  }

  @Test void copyZero()
  {
    TimeRangeList other = timeRangeList.copy();
    assertNotEquals(timeRangeList,other);

  }

  @Test void copyOne()
  {
    timeRangeList.addTimeRange(new TimeRange(randomTime(),randomTime()));
    TimeRangeList other = timeRangeList.copy();
    assertNotEquals(timeRangeList,other);
    assertNotEquals(timeRangeList.getTimeRangeByIndex(0),other.getTimeRangeByIndex(0));
    assertEquals(timeRangeList.getTimeRangeByIndex(0).toString(),other.getTimeRangeByIndex(0).toString());
  }

  @Test void copyMany()
  {
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      TimeRange timeRange = new TimeRange(randomTime(), randomTime());
      timeRangeList.addTimeRange(timeRange);
    }
    TimeRangeList other = timeRangeList.copy();
    assertNotEquals(timeRangeList,other);
    for (int x=0;x<timeRangeList.getSize();x++)
    {
      assertNotEquals(timeRangeList.getTimeRangeByIndex(x),other.getTimeRangeByIndex(x));
      assertEquals(timeRangeList.getTimeRangeByIndex(x).toString(),other.getTimeRangeByIndex(x).toString());
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