package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.hotel.RoomInformation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RoomTypeViewModel implements PropertyChangeListener
{
  private BookingModel model;
  private StringProperty roomTypeLabel;
  private StringProperty sizeLabel;
  private StringProperty facilitiesLabel;
  private ObservableList<String> serviceList;
  private RoomInformation roomInformation;

  public RoomTypeViewModel(BookingModel model)
  {
    this.model = model;
    roomTypeLabel = new SimpleStringProperty();
    sizeLabel = new SimpleStringProperty();
    facilitiesLabel = new SimpleStringProperty();
    serviceList = FXCollections.observableArrayList();
    this.model.addListener("hotel",this);
  }

  public void setInformation(RoomInformation roomInformation)
  {
    if (roomInformation!=null)
    {
      this.roomInformation = roomInformation;
      roomTypeLabel.setValue(this.roomInformation.getType());
      sizeLabel.setValue(this.roomInformation.getSize() + " m²");
      facilitiesLabel.setValue(this.roomInformation.getFacilities());
      serviceList.clear();
      for (int x=0;x<this.roomInformation.getSupplyForFree().getSize();x++)
      {
        serviceList.add(this.roomInformation.getSupplyForFree().getOptionServiceByIndex(x).toString());
      }
    }
  }

  public void clear()
  {
    roomTypeLabel.setValue(null);
    sizeLabel.setValue(null);
    facilitiesLabel.setValue(null);
    serviceList.clear();
  }

  public RoomInformation getRoomInformation()
  {
    return roomInformation;
  }

  public StringProperty sizeLabelProperty()
  {
    return sizeLabel;
  }

  public StringProperty facilitiesLabelProperty()
  {
    return facilitiesLabel;
  }

  public StringProperty roomTypeLabelProperty()
  {
    return roomTypeLabel;
  }

  public ObservableList<String> getServiceList()
  {
    return serviceList;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "hotel":
          setInformation(model.getRoomInformationList().getRoomInformationByType(roomTypeLabel.getValue()));
          break;
      }
    });
  }
}
