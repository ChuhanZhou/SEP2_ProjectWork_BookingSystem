package model.domain.hotel;

import java.util.ArrayList;

public class Hotel implements HotelInterface
{
  private static Hotel hotel;
  private RoomInformationList roomInformationList;
  private OptionServiceList optionServiceList;
  private String name;
  private String address;
  private String contactNumber;
  private String facilities;
  private String characteristic;
  private String description;
  private String rules;
  private int checkInHour;
  private int checkOutHour;

  private Hotel()
  {
    this("New Hotel",null,null,null,null,null,null);
  }

  private Hotel(String name, String address, String contactNumber, String facilities, String characteristic,String description,String rules)
  {
    this.name=name;
    this.address=address;
    this.contactNumber=contactNumber;
    this.facilities=facilities;
    this.characteristic=characteristic;
    this.description = description;
    this.rules = rules;
    roomInformationList = new RoomInformationList();
    optionServiceList = new OptionServiceList();
    checkInHour = 1;
    checkOutHour = 0;
  }

  public static Hotel getHotel()
  {
    if (hotel==null)
    {
      hotel = new Hotel();
    }
    return hotel;
  }

  public static Hotel getHotel(String name, String address, String contactNumber, String facilities, String characteristic,String description,String rules)
  {
    hotel = new Hotel(name,address,contactNumber,facilities,characteristic,description,rules);
    return hotel;
  }

  public static void setHotel(Hotel hotel)
  {
    Hotel.hotel = hotel;
  }

  public void setOptionServiceList(OptionServiceList optionServiceList)
  {
    this.optionServiceList = optionServiceList;
  }

  public void setRoomInformationList(RoomInformationList roomInformationList)
  {
    this.roomInformationList = roomInformationList;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public void setRules(String rules)
  {
    this.rules = rules;
  }

  public void setCharacteristic(String characteristic)
  {
    this.characteristic = characteristic;
  }

  public void setFacilities(String facilities)
  {
    this.facilities = facilities;
  }

  public void setContactNumber(String contactNumber)
  {
    this.contactNumber = contactNumber;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setCheckInHour(int checkInHour)
  {
    this.checkInHour = checkInHour;
  }

  public void setCheckOutHour(int checkOutHour)
  {
    this.checkOutHour = checkOutHour;
  }

  public String getDescription()
  {
    return description;
  }

  public String getRules()
  {
    return rules;
  }

  public String getName()
  {
    return name;
  }

  public String getAddress()
  {
    return address;
  }

  public String getContactNumber()
  {
    return contactNumber;
  }

  public String getFacilities()
  {
    return facilities;
  }

  public String getCharacteristic()
  {
    return characteristic;
  }

  public int getCheckInHour()
  {
    return checkInHour;
  }

  public int getCheckOutHour()
  {
    return checkOutHour;
  }

  public OptionServiceList getOptionServiceList()
  {
    return optionServiceList;
  }

  public RoomInformationList getRoomInformationList()
  {
    return roomInformationList;
  }

  public Hotel copy()
  {
    Hotel other = new Hotel(name,address,contactNumber,facilities,characteristic,description,rules);
    other.setCheckInHour(checkInHour);
    other.setCheckOutHour(checkOutHour);
    other.setOptionServiceList(optionServiceList);
    other.setRoomInformationList(roomInformationList);
    return other;
  }
}
