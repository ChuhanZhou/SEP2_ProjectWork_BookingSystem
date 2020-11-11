package model.domain.hotel;

public class OptionService
{
  private String serviceType;
  private double price;

  public OptionService(String serviceType,double price)
  {
    this.serviceType = serviceType;
    this.price = price;
  }

  public void setServiceType(String serviceType)
  {
    this.serviceType = serviceType;
  }

  public void setPrice(double price)
  {
    this.price = price;
  }

  public String getServiceType()
  {
    return serviceType;
  }

  public double getPrice()
  {
    return price;
  }

  @Override public boolean equals(Object obj)
  {
    if (obj instanceof OptionService)
    {
      if (((OptionService) obj).serviceType.equals(serviceType))
      {
        return true;
      }
    }
    return false;
  }

  @Override public String toString()
  {
    return "[" + serviceType + "]: " + price + " DKK";
  }

  public OptionService copy()
  {
    OptionService other = new OptionService(serviceType,price);
    return other;
  }
}

