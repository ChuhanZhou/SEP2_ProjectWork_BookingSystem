package viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.room.Room;
import model.domain.room.RoomInterface;
import utility.NamedPropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RoomViewModel implements PropertyChangeListener,
    NamedPropertyChangeSubject
{
  private BookingModel model;
  private StringProperty roomNum;
  private StringProperty error;
  private ObservableList<String> type;
  private PropertyChangeSupport property;
  private RoomInterface oldRoom;
  private StringProperty typeBoxValue;

  public RoomViewModel(BookingModel model)
  {
    this.model = model;
    oldRoom = null;
    property = new PropertyChangeSupport(this);
    roomNum = new SimpleStringProperty();
    error = new SimpleStringProperty();
    typeBoxValue = new SimpleStringProperty();
    type = FXCollections.observableArrayList();
    model.addListener("error",this);
    model.addListener("hotel",this);
  }

  public void setInformation(RoomInterface room)
  {
    if (room!=null)
    {
      oldRoom = room.copy();
      roomNum.set("" + oldRoom.getRoomNumber());
      type.clear();
      for (int x=0;x<model.getRoomInformationList().getSize();x++)
      {
        type.add(model.getRoomInformationList().getRoomInformationByIndex(x).getType());
      }
      typeBoxValue.setValue(oldRoom.getType());
    }
    else
    {
      setInformation();
    }
  }

  public void setInformation()
  {
    oldRoom = null;
    roomNum.set(null);
    type.clear();
    for (int x=0;x<model.getRoomInformationList().getSize();x++)
    {
      type.add(model.getRoomInformationList().getRoomInformationByIndex(x).getType());
    }
    typeBoxValue.setValue(null);
  }

  public void clear()
  {
    type.clear();
    typeBoxValue.setValue(null);
    roomNum.set(null);
    error.set(null);
  }

  public StringProperty typeBoxValueProperty()
  {
    return typeBoxValue;
  }

  public StringProperty getRoomNumProperty()
  {
    return roomNum;
  }

  public StringProperty getErrorProperty()
  {
    return error;
  }

  public ObservableList<String> getTypeProperty()
  {
    return type;
  }

  public void clickSaveButton()
  {
    if (roomNum.getValue()==null)
    {
      error.set("Please write a room number!");
      property.firePropertyChange("error",-1,error.getValue());
    }
    else if (model.getRoomInformationList().getRoomInformationByType(typeBoxValue.getValue())==null)
    {
      error.set("Please choose a room type!");
      property.firePropertyChange("error",-1,error.getValue());
    }
    else
    {
      try
      {
        if (oldRoom ==null)
        {
          model.addNewRoom(Integer.parseInt(roomNum.getValue()),typeBoxValue.getValue());
        }
        else
        {
          model.updateRoom(oldRoom,new Room(Integer.parseInt(roomNum.getValue()),typeBoxValue.getValue()));
        }
      }
      catch (NumberFormatException e)
      {
        error.set("The room number should be an integer!");
        property.firePropertyChange("error",-1,error.getValue());
      }
    }
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        error.set(((LocalError) evt.getNewValue()).getError());
        property.firePropertyChange("error",-1,error.getValue());
        break;
    }
  }

  @Override public void addListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.addPropertyChangeListener(propertyName,listener);
  }

  @Override public void removeListener(String propertyName,
      PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(propertyName,listener);
  }
}
