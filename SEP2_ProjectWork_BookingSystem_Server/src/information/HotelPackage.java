package information;

import model.domain.hotel.Hotel;
import model.domain.hotel.HotelInterface;

public class HotelPackage extends InformationPackage
{
  private Hotel hotel;

  public HotelPackage(HotelInterface hotel)
  {
    super(InformationType.HOTEL);
    this.hotel = hotel.copy();
  }

  public void setHotel(HotelInterface hotel)
  {
    this.hotel = hotel.copy();
  }

  public HotelInterface getHotel()
  {
    return hotel;
  }
}
