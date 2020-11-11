package model.domain.hotel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoomInformationListTest
{
  private RoomInformationList roomInformationList;
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

  private void addOptionServices()
  {
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      OptionService optionService = new OptionService("Type " + randomNumber(),randomNumber());
      while (optionServiceList.getOptionServiceByServiceType(optionService.getServiceType())!=null)
      {
        optionService = new OptionService("Type " + randomNumber(),randomNumber());
      }
      optionServiceList.addOptionService(optionService);
    }
  }

  private OptionServiceList getRandomOptionServiceList()
  {
    OptionServiceList optionServiceList = new OptionServiceList();
    int time = randomNumber(this.optionServiceList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(this.optionServiceList.getSize());
      optionServiceList.addOptionService(this.optionServiceList.getOptionServiceByIndex(index));
    }
    return optionServiceList;
  }

  private void checkRoomInformationEquals(RoomInformation roomInformation1,RoomInformation roomInformation2)
  {
    assertEquals(roomInformation1.getType(),roomInformation2.getType());
    assertEquals(roomInformation1.getPrice(),roomInformation2.getPrice());
    for (int x=0;x<roomInformation1.getSupplyForFree().getSize();x++)
    {
      assertEquals(roomInformation1.getSupplyForFree().getOptionServiceByIndex(x).getServiceType(),roomInformation2.getSupplyForFree().getOptionServiceByIndex(x).getServiceType());
      assertEquals(roomInformation1.getSupplyForFree().getOptionServiceByIndex(x).getPrice(),roomInformation2.getSupplyForFree().getOptionServiceByIndex(x).getPrice());
    }
  }

  @BeforeEach void setUp()
  {
    roomInformationList = new RoomInformationList();
    optionServiceList = new OptionServiceList();
    addOptionServices();
  }

  @AfterEach void tearDown()
  {
  }

  @Test void addNewRoomInformationZero()
  {
    assertEquals(0,roomInformationList.getSize());
    assertDoesNotThrow(()->roomInformationList.addNewRoomInformation(null));
    assertEquals(0,roomInformationList.getSize());
  }

  @Test void addNewRoomInformationOne()
  {
    RoomInformation roomInformation = new RoomInformation("Type " + randomNumber(),randomNumber(),getRandomOptionServiceList(),"Facilities " + randomNumber(),randomNumber());
    roomInformationList.addNewRoomInformation(roomInformation);
    checkRoomInformationEquals(roomInformation,roomInformationList.getRoomInformationByIndex(0));
  }

  @Test void addNewRoomInformationMany()
  {
    ArrayList<RoomInformation> roomInformationArrayList = new ArrayList<>();
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      RoomInformation roomInformation = new RoomInformation("Type " + randomNumber(),randomNumber(),getRandomOptionServiceList(),"Facilits " + randomNumber(),randomNumber());
      while (roomInformationList.getRoomInformationByType(roomInformation.getType())!=null)
      {
        roomInformation = new RoomInformation("Type " + randomNumber(),randomNumber(),getRandomOptionServiceList(),"Facilits " + randomNumber(),randomNumber());
      }
      roomInformationList.addNewRoomInformation(roomInformation);
      roomInformationArrayList.add(roomInformation);
    }
    for (int x=0;x<time;x++)
    {
      checkRoomInformationEquals(roomInformationArrayList.get(x),roomInformationList.getRoomInformationByIndex(x));
    }
  }

  @Test void addNewRoomInformationBoundary()
  {
    assertEquals(0,roomInformationList.getSize());
    RoomInformation roomInformation = new RoomInformation("Type " + randomNumber(),randomNumber(),getRandomOptionServiceList(),"Facilits " + randomNumber(),randomNumber());
    roomInformationList.addNewRoomInformation(roomInformation);
    assertEquals(1,roomInformationList.getSize());
    RoomInformation otherRoomInformation = new RoomInformation(roomInformation.getType(),randomNumber(),getRandomOptionServiceList(),"Facilits " + randomNumber(),randomNumber());
    roomInformationList.addNewRoomInformation(otherRoomInformation);
    assertEquals(1,roomInformationList.getSize());
    checkRoomInformationEquals(roomInformation,roomInformationList.getRoomInformationByIndex(0));
  }

  @Test void addNewRoomInformationException()
  {
    //no exception
  }

  @Test void getRoomInformationByIndex()
  {
  }

  @Test void getRoomInformationByType()
  {
  }

  @Test void removeRoomInformationByIndex()
  {
  }

  @Test void removeRoomInformationByType()
  {
  }

  @Test void removeRoomInformation()
  {
  }

  @Test void copy()
  {
  }
}