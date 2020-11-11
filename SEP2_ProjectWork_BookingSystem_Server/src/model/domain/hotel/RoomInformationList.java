package model.domain.hotel;

import java.util.ArrayList;

public class RoomInformationList
{
  private ArrayList<RoomInformation> roomInformationList;

  public RoomInformationList()
  {
    roomInformationList = new ArrayList<>();
  }

  public void addNewRoomInformation(RoomInformation roomInformation)
  {
    if (roomInformation!=null)
    {
      if (getRoomInformationByType(roomInformation.getType())==null)
      {
        roomInformationList.add(roomInformation);
      }
    }
  }

  public RoomInformation getRoomInformationByIndex(int index)
  {
    if (index<=roomInformationList.size()-1&&index>=0)
    {
      return roomInformationList.get(index);
    }
    return null;
  }

  public RoomInformation getRoomInformationByType(String Type)
  {
    for (int x=0;x<roomInformationList.size();x++)
    {
      if (roomInformationList.get(x).getType().equals(Type))
      {
        return roomInformationList.get(x);
      }
    }
    return null;
  }

  public void removeRoomInformationByIndex(int index)
  {
    if (index<=roomInformationList.size()-1&&index>=0)
    {
      roomInformationList.remove(index);
    }
  }

  public void removeRoomInformationByType(String type)
  {
    roomInformationList.remove(getRoomInformationByType(type));
  }

  public void removeRoomInformation(RoomInformation roomInformation)
  {
    if (roomInformation!=null)
    {
      roomInformationList.remove(getRoomInformationByType(roomInformation.getType()));
    }
  }

  public int getSize()
  {
    return roomInformationList.size();
  }

  public RoomInformationList copy()
  {
    RoomInformationList other = new RoomInformationList();
    for (int x=0;x<roomInformationList.size();x++)
    {
      other.addNewRoomInformation(roomInformationList.get(x).copy());
    }
    return other;
  }
}
