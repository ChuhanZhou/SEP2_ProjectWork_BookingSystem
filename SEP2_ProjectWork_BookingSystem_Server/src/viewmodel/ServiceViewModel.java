package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.hotel.OptionService;
import utility.NamedPropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ServiceViewModel implements PropertyChangeListener,
    NamedPropertyChangeSubject
{
  private BookingModel model;
  private OptionService oldOptionService;
  private StringProperty serviceTypeText;
  private StringProperty priceText;
  private StringProperty errorLabel;
  private PropertyChangeSupport property;

  public ServiceViewModel(BookingModel model)
  {
    this.model = model;
    oldOptionService = null;
    serviceTypeText = new SimpleStringProperty();
    priceText = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    property = new PropertyChangeSupport(this);
    model.addListener("error",this);
  }

  public void setInformation(OptionService optionService)
  {
    if (optionService!=null)
    {
      oldOptionService = optionService.copy();
      serviceTypeText.setValue(oldOptionService.getServiceType());
      priceText.setValue("" + oldOptionService.getPrice());
    }
    else
    {
      setInformation();
    }
  }

  public void setInformation()
  {
    oldOptionService = null;
    serviceTypeText.setValue(null);
    priceText.setValue(null);
    errorLabel.setValue(null);
  }

  public void clickSaveButton()
  {
    if (serviceTypeText.getValue()==null)
    {
      errorLabel.setValue("Please write a service type!");
      property.firePropertyChange("error",-1,errorLabel.getValue());
    }
    else if (priceText.getValue()==null)
    {
      errorLabel.setValue("Please write a price!");
      property.firePropertyChange("error",-1,errorLabel.getValue());
    }
    else
    {
      String[] serviceType = (serviceTypeText.getValue()).split(" ");
      if (serviceType.length==0)
      {
        errorLabel.setValue("Please write a service type!");
        property.firePropertyChange("error",-1,errorLabel.getValue());
      }
      else
      {
        try
        {
          if (Double.parseDouble(priceText.getValue())<=0)
          {
            errorLabel.setValue("The number of price must larger than 0!");
            property.firePropertyChange("error",-1,errorLabel.getValue());
          }
          else
          {
            if (oldOptionService==null)
            {
              model.addOptionService(new OptionService(serviceTypeText.getValue(),Double.parseDouble(priceText.getValue())));
            }
            else
            {
              model.updateOptionService(oldOptionService,new OptionService(serviceTypeText.getValue(),Double.parseDouble(priceText.getValue())));
            }
          }
        }
        catch (NumberFormatException e)
        {
          errorLabel.setValue("Please write a number in Price!");
          property.firePropertyChange("error",-1,errorLabel.getValue());
        }

      }
    }

  }

  public void clear()
  {
    serviceTypeText.setValue(null);
    priceText.setValue(null);
    errorLabel.setValue(null);
  }

  public StringProperty priceTextProperty()
  {
    return priceText;
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  public StringProperty serviceTypeTextProperty()
  {
    return serviceTypeText;
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
