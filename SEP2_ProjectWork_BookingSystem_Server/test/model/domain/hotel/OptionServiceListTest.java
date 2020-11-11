package model.domain.hotel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OptionServiceListTest
{
  private OptionServiceList optionServiceList;

  private int randomNumber()
  {
    return (int) (Math.random() * 100 + 0);
  }

  private int randomNumber(int max)
  {
    return (int) (Math.random() * max + 0);
  }

  private int randomNumber(int max,int min)
  {
    return (int) (Math.random() * (max - min) + min);
  }

  private void checkOptionServiceEquals(OptionService optionService1,OptionService optionService2)
  {
    assertEquals(optionService1.getServiceType(),optionService2.getServiceType());
    assertEquals(optionService1.getPrice(),optionService2.getPrice());
  }

  @BeforeEach void setUp()
  {
    optionServiceList = new OptionServiceList();
  }

  @AfterEach void tearDown()
  {
  }

  @Test void addOptionServiceZero()
  {
    assertEquals(0,optionServiceList.getSize());
    assertDoesNotThrow(()->optionServiceList.addOptionService(null));
    assertEquals(0,optionServiceList.getSize());
  }

  @Test void addOptionServiceOne()
  {
    OptionService optionService = new OptionService("Type " + randomNumber(),randomNumber());
    optionServiceList.addOptionService(optionService);
    checkOptionServiceEquals(optionService,optionServiceList.getOptionServiceByIndex(0));
  }

  @Test void addOptionServiceMany()
  {
    ArrayList<OptionService> optionServiceArrayList = new ArrayList<>();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      OptionService optionService = new OptionService("Type " + randomNumber(),randomNumber());
      while (optionServiceList.getOptionServiceByServiceType(optionService.getServiceType())!=null)
      {
        optionService = new OptionService("Type " + randomNumber(),randomNumber());
      }
      optionServiceList.addOptionService(optionService);
      optionServiceArrayList.add(optionService);
    }
    for (int x=0;x<time;x++)
    {
      checkOptionServiceEquals(optionServiceArrayList.get(x),optionServiceList.getOptionServiceByIndex(x));
    }
  }

  @Test void addOptionServiceBoundary()
  {
    assertEquals(0,optionServiceList.getSize());
    OptionService optionService = new OptionService("Type " + randomNumber(),randomNumber());
    optionServiceList.addOptionService(optionService);
    assertEquals(1,optionServiceList.getSize());
    OptionService otherOptionService = new OptionService(optionService.getServiceType(),optionService.getPrice()+1);
    optionServiceList.addOptionService(otherOptionService);
    assertEquals(1,optionServiceList.getSize());
    checkOptionServiceEquals(optionService,optionServiceList.getOptionServiceByIndex(0));
  }

  @Test void addOptionServiceException()
  {
    //no exception
  }

  @Test void getOptionServiceByIndexZero()
  {
    assertDoesNotThrow(()->optionServiceList.getOptionServiceByIndex(-1));
    assertNull(optionServiceList.getOptionServiceByIndex(-1));
    addOptionServiceMany();
    assertDoesNotThrow(()->optionServiceList.getOptionServiceByIndex(-1));
    assertNull(optionServiceList.getOptionServiceByIndex(-1));
  }

  @Test void getOptionServiceByIndexOne()
  {
    addOptionServiceOne();
  }

  @Test void getOptionServiceByIndexMany()
  {
    addOptionServiceMany();
  }

  @Test void getOptionServiceByIndexBoundary()
  {
    assertNull(optionServiceList.getOptionServiceByIndex(optionServiceList.getSize()));
    addOptionServiceMany();
    assertNull(optionServiceList.getOptionServiceByIndex(optionServiceList.getSize()));
  }

  @Test void getOptionServiceByIndexException()
  {
    //no exception
  }

  @Test void getOptionServiceByServiceTypeZero()
  {
    assertDoesNotThrow(()->optionServiceList.getOptionServiceByServiceType(null));
    assertNull(optionServiceList.getOptionServiceByServiceType(null));
    addOptionServiceMany();
    assertDoesNotThrow(()->optionServiceList.getOptionServiceByServiceType(null));
    assertNull(optionServiceList.getOptionServiceByServiceType(null));
  }

  @Test void getOptionServiceByServiceTypeOne()
  {
    addOptionServiceMany();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.getOptionServiceByIndex(index);
    checkOptionServiceEquals(optionService,optionServiceList.getOptionServiceByServiceType(optionService.getServiceType()));
  }

  @Test void getOptionServiceByServiceTypeMany()
  {
    addOptionServiceMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(optionServiceList.getSize());
      OptionService optionService = optionServiceList.getOptionServiceByIndex(index);
      checkOptionServiceEquals(optionService,optionServiceList.getOptionServiceByServiceType(optionService.getServiceType()));
    }
  }

  @Test void getOptionServiceByServiceTypeBoundary()
  {
    addOptionServiceMany();
    int index = randomNumber(optionServiceList.getSize());
    assertNull(optionServiceList.getOptionServiceByServiceType("New " + optionServiceList.getOptionServiceByIndex(index).getServiceType()));
  }

  @Test void getOptionServiceByServiceTypeException()
  {
    //no exception
  }

  @Test void updatePriceZero()
  {
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    assertDoesNotThrow(()->optionServiceList.updatePrice(null));
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void updatePriceOne()
  {
    addOptionServiceMany();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    optionService.setPrice(randomNumber());
    optionServiceList.updatePrice(optionService);
    checkOptionServiceEquals(optionService,optionServiceList.getOptionServiceByIndex(index));
  }

  @Test void updatePriceMany()
  {
    addOptionServiceMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(optionServiceList.getSize());
      OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
      optionService.setPrice(randomNumber());
      optionServiceList.updatePrice(optionService);
      checkOptionServiceEquals(optionService,optionServiceList.getOptionServiceByIndex(index));
    }
  }

  @Test void updatePriceBoundary()
  {
    addOptionServiceMany();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    OptionService otherOptionService = optionService.copy();
    otherOptionService.setServiceType("New " + optionService.getServiceType());
    otherOptionService.setPrice(optionService.getPrice() + 1);
    optionServiceList.updatePrice(otherOptionService);
    checkOptionServiceEquals(optionService,optionServiceList.getOptionServiceByIndex(index));
  }

  @Test void updatePriceException()
  {
    //no exception
  }

  @Test void updateOptionServiceZero()
  {
    assertDoesNotThrow(()->optionServiceList.updateOptionService(null,null));
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    assertDoesNotThrow(()->optionServiceList.updateOptionService(null,null));
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void updateOptionServiceOne()
  {
    addOptionServiceMany();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    OptionService newOptionService = optionService.copy();
    newOptionService.setServiceType("New " + optionService.getServiceType());
    optionServiceList.updateOptionService(optionService,newOptionService);
    checkOptionServiceEquals(newOptionService,optionServiceList.getOptionServiceByIndex(index));
  }

  @Test void updateOptionServiceMany()
  {
    addOptionServiceMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(optionServiceList.getSize());
      OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
      OptionService newOptionService = optionService.copy();
      newOptionService.setServiceType("New " + optionService.getServiceType());
      optionServiceList.updateOptionService(optionService,newOptionService);
      checkOptionServiceEquals(newOptionService,optionServiceList.getOptionServiceByIndex(index));
    }
  }

  @Test void updateOptionServiceBoundary()
  {
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    OptionService otherOptionService = optionServiceList.copy().getOptionServiceByIndex(index + 1);
    OptionService newOptionService = optionService.copy();
    newOptionService.setServiceType("New " + optionService.getServiceType());
    optionServiceList.updateOptionService(newOptionService,optionService);
    optionServiceList.updateOptionService(optionService,otherOptionService);
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void updateOptionServiceException()
  {
    //no exception
  }

  @Test void removeOptionServiceByServiceTypeZero()
  {
    assertDoesNotThrow(()->optionServiceList.removeOptionServiceByServiceType(null));
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    assertDoesNotThrow(()->optionServiceList.removeOptionServiceByServiceType(null));
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void removeOptionServiceByServiceTypeOne()
  {
    addOptionServiceMany();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    optionServiceList.removeOptionServiceByServiceType(optionService.getServiceType());
    assertNull(optionServiceList.getOptionServiceByServiceType(optionService.getServiceType()));
  }

  @Test void removeOptionServiceByServiceTypeMany()
  {
    addOptionServiceMany();
    int time = randomNumber(optionServiceList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(optionServiceList.getSize());
      OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
      optionServiceList.removeOptionServiceByServiceType(optionService.getServiceType());
      assertNull(optionServiceList.getOptionServiceByServiceType(optionService.getServiceType()));
    }
  }

  @Test void removeOptionServiceByServiceTypeBoundary()
  {
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    optionServiceList.removeOptionServiceByServiceType("New " + optionService.getServiceType());
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void removeOptionServiceByServiceTypeException()
  {
    //no exception
  }

  @Test void removeOptionServiceZero()
  {
    assertDoesNotThrow(()->optionServiceList.removeOptionService(null));
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    assertDoesNotThrow(()->optionServiceList.removeOptionService(null));
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void removeOptionServiceOne()
  {
    addOptionServiceMany();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    optionServiceList.removeOptionService(optionService);
    assertNull(optionServiceList.getOptionServiceByServiceType(optionService.getServiceType()));
  }

  @Test void removeOptionServiceMany()
  {
    addOptionServiceMany();
    int time = randomNumber(optionServiceList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(optionServiceList.getSize());
      OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
      optionServiceList.removeOptionService(optionService);
      assertNull(optionServiceList.getOptionServiceByServiceType(optionService.getServiceType()));
    }
  }

  @Test void removeOptionServiceBoundary()
  {
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    optionService.setServiceType("New " + optionService.getServiceType());
    optionServiceList.removeOptionService(optionService);
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void removeOptionServiceException()
  {
    //no exception
  }

  @Test void removeOptionServiceByIndexZero()
  {
    assertDoesNotThrow(()->optionServiceList.removeOptionServiceByIndex(-1));
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    assertDoesNotThrow(()->optionServiceList.removeOptionServiceByIndex(-1));
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void removeOptionServiceByIndexOne()
  {
    addOptionServiceMany();
    int index = randomNumber(optionServiceList.getSize());
    OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
    optionServiceList.removeOptionServiceByIndex(index);
    assertNull(optionServiceList.getOptionServiceByServiceType(optionService.getServiceType()));
  }

  @Test void removeOptionServiceByIndexMany()
  {
    addOptionServiceMany();
    int time = randomNumber(optionServiceList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(optionServiceList.getSize());
      OptionService optionService = optionServiceList.copy().getOptionServiceByIndex(index);
      optionServiceList.removeOptionServiceByIndex(index);
      assertNull(optionServiceList.getOptionServiceByServiceType(optionService.getServiceType()));
    }
  }

  @Test void removeOptionServiceByIndexBoundary()
  {
    addOptionServiceMany();
    OptionServiceList optionServiceListCopy = optionServiceList.copy();
    int index = randomNumber(optionServiceList.getSize());
    optionServiceList.removeOptionServiceByIndex(optionServiceList.getSize());
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(optionServiceList.getOptionServiceByIndex(x),optionServiceListCopy.getOptionServiceByIndex(x));
    }
  }

  @Test void removeOptionServiceByIndexException()
  {
    //no exception
  }

  @Test void copyZero()
  {
    OptionServiceList other = optionServiceList.copy();
    assertNotEquals(optionServiceList,other);
  }

  @Test void copyOne()
  {
    addOptionServiceOne();
    OptionServiceList other = optionServiceList.copy();
    assertNotEquals(other,optionServiceList);
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(other.getOptionServiceByIndex(x),optionServiceList.getOptionServiceByIndex(x));
    }
  }

  @Test void copyMany()
  {
    addOptionServiceMany();
    OptionServiceList other = optionServiceList.copy();
    assertNotEquals(other,optionServiceList);
    for (int x=0;x<optionServiceList.getSize();x++)
    {
      checkOptionServiceEquals(other.getOptionServiceByIndex(x),optionServiceList.getOptionServiceByIndex(x));
    }
  }

  @Test void copyBoundary()
  {
    //no boundary
  }

  @Test void copyException()
  {
    //no exception
  }
}