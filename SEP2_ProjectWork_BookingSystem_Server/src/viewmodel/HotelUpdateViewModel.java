package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.BookingModel;
import model.domain.LocalError;
import utility.NamedPropertyChangeSubject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class HotelUpdateViewModel implements PropertyChangeListener,
    NamedPropertyChangeSubject
{
  private BookingModel model;
  private StringProperty hotelNameText;
  private StringProperty hotelAddressText;
  private StringProperty hotelDescriptionText;
  private StringProperty characteristicText;
  private StringProperty checkInTimeText;
  private StringProperty checkOutTimeText;
  private StringProperty facilitiesText;
  private StringProperty rulesText;
  private StringProperty contactNumText;
  private StringProperty errorLabel;
  private PropertyChangeSupport property;

  public HotelUpdateViewModel(BookingModel model)
  {
    this.model = model;
    hotelNameText = new SimpleStringProperty();
    hotelAddressText = new SimpleStringProperty();
    hotelDescriptionText = new SimpleStringProperty();
    characteristicText = new SimpleStringProperty();
    checkInTimeText = new SimpleStringProperty();
    checkOutTimeText = new SimpleStringProperty();
    facilitiesText = new SimpleStringProperty();
    rulesText = new SimpleStringProperty();
    contactNumText = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    property = new PropertyChangeSupport(this);
    this.model.addListener("error",this);
  }

  public void clickSaveButton()
  {
    try
    {
      if (Integer.parseInt(checkInTimeText.getValue())<=Integer.parseInt(checkOutTimeText.getValue()))
      {
        errorLabel.setValue("Please write Check-in Time after Check-out Time!");
        property.firePropertyChange("error",-1,errorLabel.getValue());
      }
      else if (Integer.parseInt(checkInTimeText.getValue())<0||Integer.parseInt(checkInTimeText.getValue())>=23)
      {
        errorLabel.setValue("Please write Check-in Time between 0 and 23!");
        property.firePropertyChange("error",-1,errorLabel.getValue());
      }
      else if (Integer.parseInt(checkOutTimeText.getValue())<0||Integer.parseInt(checkOutTimeText.getValue())>=23)
      {
        errorLabel.setValue("Please write Check-out Time between 0 and 23!");
        property.firePropertyChange("error",-1,errorLabel.getValue());
      }
      else
      {
        try
        {
          if (contactNumText.getValue()!=null)
          {
            String[] contactNumber = contactNumText.getValue().split("-");
            int contactNum = 0;
            for (int x=0;x<contactNumber.length;x++)
            {
              contactNum += Integer.parseInt(contactNumber[x]);
            }
          }
          model.updateHotel(hotelNameText.getValue(),hotelAddressText.getValue(),contactNumText.getValue(),facilitiesText.getValue(),characteristicText.getValue(),hotelDescriptionText.getValue(),rulesText.getValue(),Integer.parseInt(checkInTimeText.getValue()),Integer.parseInt(checkOutTimeText.getValue()));
        }
        catch (NumberFormatException e)
        {
          errorLabel.setValue("Please write only number and '-' in Contact Number!");
          property.firePropertyChange("error",-1,errorLabel.getValue());
        }
      }
    }
    catch (NumberFormatException e)
    {
      errorLabel.setValue("Please write a integer in Check-in Time and Check-out Time!");
      property.firePropertyChange("error",-1,errorLabel.getValue());
    }
  }

  public void setInformation()
  {
    hotelNameText.setValue(model.getHotel().getName());
    hotelAddressText.setValue(model.getHotel().getAddress());
    hotelDescriptionText.setValue(model.getHotel().getDescription());
    characteristicText.setValue(model.getHotel().getCharacteristic());
    checkInTimeText.setValue("" + model.getHotel().getCheckInHour());
    checkOutTimeText.setValue("" + model.getHotel().getCheckOutHour());
    facilitiesText.setValue(model.getHotel().getFacilities());
    rulesText.setValue(model.getHotel().getRules());
    contactNumText.setValue(model.getHotel().getContactNumber());
  }

  public void clear()
  {
    hotelNameText.setValue(null);
    hotelAddressText.setValue(null);
    hotelDescriptionText.setValue(null);
    characteristicText.setValue(null);
    checkInTimeText.setValue(null);
    checkOutTimeText.setValue(null);
    facilitiesText.setValue(null);
    rulesText.setValue(null);
    contactNumText.setValue(null);
    errorLabel.setValue(null);
  }

  public StringProperty facilitiesTextProperty()
  {
    return facilitiesText;
  }

  public StringProperty characteristicTextProperty()
  {
    return characteristicText;
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  public StringProperty hotelAddressTextProperty()
  {
    return hotelAddressText;
  }

  public StringProperty contactNumTextProperty()
  {
    return contactNumText;
  }

  public StringProperty hotelDescriptionTextProperty()
  {
    return hotelDescriptionText;
  }

  public StringProperty hotelNameTextProperty()
  {
    return hotelNameText;
  }

  public StringProperty rulesTextProperty()
  {
    return rulesText;
  }

  public StringProperty checkInTimeTextProperty()
  {
    return checkInTimeText;
  }

  public StringProperty checkOutTimeTextProperty()
  {
    return checkOutTimeText;
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
