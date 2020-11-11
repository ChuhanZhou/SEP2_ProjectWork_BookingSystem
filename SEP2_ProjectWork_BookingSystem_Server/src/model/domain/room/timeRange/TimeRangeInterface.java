package model.domain.room.timeRange;

public interface TimeRangeInterface
{
  Time getStartTime();
  Time getEndTime();
  void setStartTime(Time startTime);
  void setEndTime(Time endTime);
  boolean within(TimeRangeInterface otherTimeRange);
  TimeRange copy();
}
