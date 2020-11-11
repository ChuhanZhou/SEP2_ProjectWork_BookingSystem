package information;

import model.domain.hotel.RoomInformationList;
import model.domain.room.TimeRangeList;

public class SearchPackage extends InformationPackage
{
  private RoomInformationList roomInformationList;
  private TimeRangeList timeRangeList;

  public SearchPackage(RoomInformationList roomInformationList,TimeRangeList timeRangeList)
  {
    super(InformationType.SEARCH);
    this.roomInformationList = roomInformationList;
    this.timeRangeList = timeRangeList;
  }

  public SearchPackage(RoomInformationList roomInformationList)
  {
    this(roomInformationList,null);
  }

  public SearchPackage(TimeRangeList timeRangeList)
  {
    this(null,timeRangeList);
  }

  public TimeRangeList getTimeRangeList()
  {
    return timeRangeList;
  }

  public RoomInformationList getRoomInformationList()
  {
    return roomInformationList;
  }
}
