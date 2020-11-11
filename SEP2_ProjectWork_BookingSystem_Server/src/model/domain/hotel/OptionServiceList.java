package model.domain.hotel;

import java.util.ArrayList;

public class OptionServiceList
{
  private ArrayList<OptionService> optionServiceList;

  public OptionServiceList()
  {
    optionServiceList = new ArrayList<>();
  }

  public void addOptionService(OptionService optionService)
  {
    if (optionService!=null)
    {
      if (getOptionServiceByServiceType(optionService.getServiceType())==null)
      {
        optionServiceList.add(optionService);
      }
    }
  }

  public OptionService getOptionServiceByIndex(int index)
  {
    if (index<=optionServiceList.size()-1&&index>=0)
    {
      return optionServiceList.get(index);
    }
    return null;
  }

  public OptionService getOptionServiceByServiceType(String serviceType)
  {
    for (int i=0;i<optionServiceList.size();i++)
    {
      if (optionServiceList.get(i).getServiceType().equals(serviceType))
      {
        return optionServiceList.get(i);
      }
    }
    return null;
  }

  public void updatePrice(OptionService optionService)
  {
    if (optionService!=null)
    {
      if (getOptionServiceByServiceType(optionService.getServiceType())!=null)
      {
        getOptionServiceByServiceType(optionService.getServiceType()).setPrice(optionService.getPrice());
      }
    }
  }

  public void updateOptionService(OptionService oldOptionService,OptionService newOptionService)
  {
    if (oldOptionService!=null&&newOptionService!=null)
    {
      if (oldOptionService.getServiceType().equals(newOptionService.getServiceType())||getOptionServiceByServiceType(newOptionService.getServiceType())==null)
      {
        getOptionServiceByServiceType(oldOptionService.getServiceType()).setPrice(newOptionService.getPrice());
        getOptionServiceByServiceType(oldOptionService.getServiceType()).setServiceType(newOptionService.getServiceType());
      }
    }
  }

  public double getTotalPrice()
  {
    double totalPrice = 0;
    for (int x=0;x<optionServiceList.size();x++)
    {
      totalPrice += optionServiceList.get(x).getPrice();
    }
    return totalPrice;
  }

  public int getSize()
  {
    return optionServiceList.size();
  }

  public void removeOptionServiceByServiceType(String serviceType)
  {
    optionServiceList.remove(getOptionServiceByServiceType(serviceType));
  }

  public void removeOptionService(OptionService optionService)
  {
    if (optionService!=null)
    {
      optionServiceList.remove(getOptionServiceByServiceType(optionService.getServiceType()));
    }
  }

  public void removeOptionServiceByIndex(int index)
  {
    if (index<optionServiceList.size()&&index>=0)
    {
      optionServiceList.remove(index);
    }
  }

  public OptionServiceList copy()
  {
    OptionServiceList other = new OptionServiceList();
    for (int x=0;x<optionServiceList.size();x++)
    {
      other.addOptionService(optionServiceList.get(x).copy());
    }
    return other;
  }
}