package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.user.User;
import utility.NamedPropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoginViewModel implements PropertyChangeListener, NamedPropertyChangeSubject
{
  private BookingModel model;
  private StringProperty ipAddress;
  private StringProperty email;
  private StringProperty password;
  private StringProperty error;
  private PropertyChangeSupport property;

  public LoginViewModel(BookingModel model)
  {
    this.model = model;
    ipAddress = new SimpleStringProperty();
    email = new SimpleStringProperty();
    password = new SimpleStringProperty();
    error = new SimpleStringProperty();
    property = new PropertyChangeSupport(this);
    this.model.addListener("error",this);
    ipAddress.set("localhost");
  }

  public void clear()
  {
    ipAddress.set("localhost");
    email.set("");
    password.set("");
    error.set("");
  }

  public StringProperty getErrorProperty()
  {
    return error;
  }

  public StringProperty getIpAddressProperty()
  {
    return ipAddress;
  }

  public StringProperty getEmailProperty()
  {
    return email;
  }

  public StringProperty getPasswordProperty()
  {
    return password;
  }

  public void clickSignInButton()
  {
    if (model.connectToServer(0,ipAddress.getValue()))
    {
      model.login("login",new User(email.getValue(),password.getValue()));
    }
  }

  public boolean clickRegisterButton()
  {
    return model.connectToServer(0,ipAddress.getValue());
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "error":
          error.set(((LocalError) evt.getNewValue()).getError());
          property.firePropertyChange("error",-1,error.getValue());
          break;
      }
    });
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
