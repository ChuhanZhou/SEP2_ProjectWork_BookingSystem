package model.domain.hotel;

public class RoomInformation
{
  private String type;
  private double price;
  private OptionServiceList supplyForFree;
  private String facilities;
  private int size;

  public RoomInformation(String type,double price,OptionServiceList supplyForFree,String facilities,int size)
  {
    this.type = type;
    this.price = price;
    this.supplyForFree = supplyForFree;
    this.facilities = facilities;
    this.size = size;
  }

  public void setFacilities(String facilities)
  {
    this.facilities = facilities;
  }

  public void setSize(int size)
  {
    this.size = size;
  }

  public String getFacilities()
  {
    return facilities;
  }

  public int getSize()
  {
    return size;
  }

  public String getType()
  {
    return type;
  }

  public double getPrice()
  {
    return price;
  }

  public OptionServiceList getSupplyForFree()
  {
    return supplyForFree;
  }

  public void setPrice(double price)
  {
    this.price = price;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public void setSupplyForFree(OptionServiceList supplyForFree)
  {
    this.supplyForFree = supplyForFree;
  }

  public void addFreeService(OptionService newFreeService)
  {
    supplyForFree.addOptionService(newFreeService);
  }

  public void removeFreeService(OptionService removedFreeService)
  {
    supplyForFree.removeOptionService(removedFreeService);
  }

  public void addFreeServiceList(OptionServiceList newFreeServiceList)
  {
    for (int i=0;i<newFreeServiceList.getSize();i++)
    {
      supplyForFree.addOptionService(newFreeServiceList.getOptionServiceByIndex(i));
    }
  }

  public void removeFreeServiceList(OptionServiceList removedFreeServiceList)
  {
    for (int i=0;i<removedFreeServiceList.getSize();i++)
    {
      supplyForFree.removeOptionService(removedFreeServiceList.getOptionServiceByIndex(i));
    }
  }

  public void removeFreeServiceByIndex(int index)
  {
    supplyForFree.removeOptionServiceByIndex(index);
  }

  @Override public String toString()
  {
    return "Type: " + type + " - Size: " + size + " m² - Price: " + price + " DKK";
  }

  @Override public boolean equals(Object obj)
  {
    if (obj instanceof RoomInformation)
    {
      if (((RoomInformation) obj).getType().equals(type))
      {
        return true;
      }
    }
    return false;
  }

  public RoomInformation copy()
  {
    RoomInformation other = new RoomInformation(type,price,supplyForFree.copy(),facilities,size);
    return other;
  }
}
