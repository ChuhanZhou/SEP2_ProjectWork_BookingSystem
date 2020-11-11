package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.hotel.OptionServiceList;
import model.domain.hotel.RoomInformation;
import utility.NamedPropertyChangeSubject;
import utility.doubleClick.Mouse;
import utility.doubleClick.VirtualMouse;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RoomTypeUpdateViewModel implements PropertyChangeListener,
    NamedPropertyChangeSubject
{
  private BookingModel model;
  private RoomInformation oldRoomInformation;
  private StringProperty typeText;
  private StringProperty facilitiesText;
  private StringProperty errorLabel;
  private StringProperty priceText;
  private StringProperty sizeText;
  private ObservableList<String> serviceListView;
  private ObservableList<String> roomServicesList;
  private PropertyChangeSupport property;
  private OptionServiceList optionServiceList;
  private OptionServiceList searchServiceList;
  private VirtualMouse listMouse;
  private VirtualMouse searchListMouse;

  public RoomTypeUpdateViewModel(BookingModel model)
  {
    this.model = model;
    oldRoomInformation = null;
    optionServiceList = new OptionServiceList();
    searchServiceList = new OptionServiceList();
    listMouse = new Mouse();
    searchListMouse = new Mouse();
    typeText = new SimpleStringProperty();
    facilitiesText = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    priceText = new SimpleStringProperty();
    sizeText = new SimpleStringProperty();
    serviceListView = FXCollections.observableArrayList();
    roomServicesList = FXCollections.observableArrayList();
    property = new PropertyChangeSupport(this);
    this.model.addListener("error",this);
    this.model.addListener("hotel",this);
  }

  public void clickRoomServicesList(int index)
  {
    searchListMouse.click();
    if (searchListMouse.getClickTime()==2)
    {
      optionServiceList.addOptionService(searchServiceList.getOptionServiceByIndex(index));
      searchServiceList.removeOptionServiceByIndex(index);
      updateServiceList();
    }
  }

  public void clickServiceListView(int index)
  {
    listMouse.click();
    if (listMouse.getClickTime()==2)
    {
      searchServiceList.addOptionService(optionServiceList.getOptionServiceByIndex(index));
      optionServiceList.removeOptionServiceByIndex(index);
      updateServiceList();
    }
  }

  public void clickSaveButton()
  {
    if (typeText.getValue()==null)
    {
      errorLabel.setValue("Please write a room type!");
      property.firePropertyChange("error",-1,errorLabel.getValue());
    }
    else if (priceText.getValue()==null)
    {
      errorLabel.setValue("Please write a price!");
      property.firePropertyChange("error",-1,errorLabel.getValue());
    }
    else if (sizeText.getValue()==null)
    {
      errorLabel.setValue("Please write a size!");
      property.firePropertyChange("error",-1,errorLabel.getValue());
    }
    else
    {
      String[] roomType = (typeText.getValue()).split(" ");
      if (roomType.length==0)
      {
        errorLabel.setValue("Please write a room type!");
        property.firePropertyChange("error",-1,errorLabel.getValue());
      }
      else
      {
        try
        {
          if (Integer.parseInt(sizeText.getValue())<=0||Double.parseDouble(priceText.getValue())<=0)
          {
            errorLabel.setValue("The number of price and size mush larger than 0!");
            property.firePropertyChange("error",-1,errorLabel.getValue());
          }
          else
          {
            if (oldRoomInformation==null)
            {
              model.addRoomInformation(new RoomInformation(typeText.getValue(),Double.parseDouble(priceText.getValue()),searchServiceList.copy(),facilitiesText.getValue(),Integer.parseInt(sizeText.getValue())));
            }
            else
            {
              model.updateRoomInformation(oldRoomInformation,new RoomInformation(typeText.getValue(),Double.parseDouble(priceText.getValue()),searchServiceList.copy(),facilitiesText.getValue(),Integer.parseInt(sizeText.getValue())));
            }
          }
        }
        catch (NumberFormatException e)
        {
          errorLabel.setValue("Please write a number in price and an integer in size!");
          property.firePropertyChange("error",-1,errorLabel.getValue());
        }
      }
    }
  }

  public void setInformation(RoomInformation roomInformation)
  {
    Platform.runLater(()->{
      if(roomInformation!=null)
      {
        oldRoomInformation = roomInformation.copy();
        typeText.setValue(oldRoomInformation.getType());
        facilitiesText.setValue(oldRoomInformation.getFacilities());
        priceText.setValue("" + oldRoomInformation.getPrice());
        sizeText.setValue("" + oldRoomInformation.getSize());
        searchServiceList = oldRoomInformation.getSupplyForFree().copy();
        updateServiceList();
      }
      else
      {
        setInformation();
      }
    });
  }

  public void setInformation()
  {
    Platform.runLater(()->{
      oldRoomInformation = null;
      typeText.setValue(null);
      facilitiesText.setValue(null);
      priceText.setValue(null);
      sizeText.setValue(null);
      searchServiceList = new OptionServiceList();
      updateServiceList();
    });
  }

  private void updateServiceList()
  {
    Platform.runLater(()->{
      OptionServiceList newSearchServiceList = new OptionServiceList();
      for (int x=0;x<searchServiceList.getSize();x++)
      {
        if (model.getOptionServiceByServiceType(searchServiceList.getOptionServiceByIndex(x).getServiceType())!=null)
        {
          newSearchServiceList.addOptionService(model.getOptionServiceByServiceType(searchServiceList.getOptionServiceByIndex(x).getServiceType()));
        }
      }
      searchServiceList = newSearchServiceList;
      roomServicesList.clear();
      for (int x=0;x<searchServiceList.getSize();x++)
      {
        roomServicesList.add(searchServiceList.getOptionServiceByIndex(x).toString());
      }
      optionServiceList = model.getOptionServiceList().copy();
      for (int x=0;x<model.getOptionServiceList().getSize();x++)
      {
        for (int i=0;i<searchServiceList.getSize();i++)
        {
          if (model.getOptionServiceByIndex(x).equals(searchServiceList.getOptionServiceByIndex(i)))
          {
            optionServiceList.removeOptionService(model.getOptionServiceByIndex(x));
          }
        }
      }
      serviceListView.clear();
      for (int x=0;x<optionServiceList.getSize();x++)
      {
        serviceListView.add(optionServiceList.getOptionServiceByIndex(x).toString());
      }
    });
  }

  public void clear()
  {
    oldRoomInformation = null;
    typeText.setValue(null);
    facilitiesText.setValue(null);
    errorLabel.setValue(null);
    priceText.setValue(null);
    sizeText.setValue(null);
    serviceListView.clear();
    roomServicesList.clear();
  }

  public RoomInformation getNewRoomInformation()
  {
    return new RoomInformation(typeText.getValue(),Double.parseDouble(priceText.getValue()),searchServiceList.copy(),facilitiesText.getValue(),Integer.parseInt(sizeText.getValue()));
  }

  public RoomInformation getOldRoomInformation()
  {
    return oldRoomInformation;
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  public StringProperty facilitiesTextProperty()
  {
    return facilitiesText;
  }

  public StringProperty typeTextProperty()
  {
    return typeText;
  }

  public StringProperty sizeTextProperty()
  {
    return sizeText;
  }

  public StringProperty priceTextProperty()
  {
    return priceText;
  }

  public ObservableList<String> getRoomServicesList()
  {
    return roomServicesList;
  }

  public ObservableList<String> getServiceListView()
  {
    return serviceListView;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "error":
          errorLabel.setValue(((LocalError) evt.getNewValue()).getError());
          property.firePropertyChange("error",-1,errorLabel.getValue());
          break;
        case "hotel":
          updateServiceList();
          break;
      }
    });
  }

  @Override public void addListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.addPropertyChangeListener(propertyName, listener);
  }

  @Override public void removeListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(propertyName, listener);
  }
}
