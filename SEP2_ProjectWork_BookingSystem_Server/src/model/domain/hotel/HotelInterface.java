package model.domain.hotel;

public interface HotelInterface
{
  void setOptionServiceList(OptionServiceList optionServiceList);
  void setRoomInformationList(RoomInformationList roomInformationList);
  void setDescription(String description);
  void setRules(String rules);
  void setCharacteristic(String characteristic);
  void setFacilities(String facilities);
  void setContactNumber(String contactNumber);
  void setAddress(String address);
  void setName(String name);
  void setCheckInHour(int checkInHour);
  void setCheckOutHour(int checkOutHour);
  String getDescription();
  String getRules();
  String getName();
  String getAddress();
  String getContactNumber();
  String getFacilities();
  String getCharacteristic();
  int getCheckInHour();
  int getCheckOutHour();
  OptionServiceList getOptionServiceList();
  RoomInformationList getRoomInformationList();
  Hotel copy();
}
