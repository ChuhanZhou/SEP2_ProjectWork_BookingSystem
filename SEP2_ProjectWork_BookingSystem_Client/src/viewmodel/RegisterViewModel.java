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

public class RegisterViewModel implements PropertyChangeListener,
    NamedPropertyChangeSubject
{
  private BookingModel model;
  private StringProperty emailAddText;
  private StringProperty passwordText;
  private StringProperty verText;
  private StringProperty errorLabel;
  private PropertyChangeSupport property;

  public RegisterViewModel(BookingModel model)
  {
    this.model = model;
    emailAddText = new SimpleStringProperty();
    passwordText = new SimpleStringProperty();
    verText = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    this.model.addListener("error",this);
    property = new PropertyChangeSupport(this);
  }

  public void clear()
  {
    emailAddText.set(null);
    passwordText.set(null);
    verText.set(null);
    errorLabel.set(null);
  }

  public StringProperty emailAddTextProperty()
  {
    return emailAddText;
  }

  public StringProperty passwordTextProperty()
  {
    return passwordText;
  }

  public StringProperty verTextProperty()
  {
    return verText;
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  private void setErrorLabel(String error)
  {
    errorLabel.set(error);
    property.firePropertyChange("error",-1,errorLabel.getValue());
  }

  public void clickRegisterBtn()
  {
    if (emailAddText.getValue()==null)
    {
      setErrorLabel("Please write a Email!");
    }
    else if (passwordText.getValue()==null)
    {
      setErrorLabel("Please write a password!");
    }
    else
    {
      String[] email = ("[ " + emailAddText.get() + " ]").split(" ");
      String[] password = ("[ " + passwordText.get() + " ]").split(" ");
      if (email.length!=3)
      {
        setErrorLabel("Please write a Email without blank!");
      }
      else if (password.length!=3)
      {
        setErrorLabel("Please write a Password without blank!");
      }
      else if (!passwordText.getValue().equals(verText.getValue()))
      {
        setErrorLabel("Please write the same password in Verification again!");
      }
      else
      {
        model.login("register",new User(emailAddText.getValue(),passwordText.getValue()));
      }
    }
  }

  public void clickBackButton()
  {
    model.signOut();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "error":
          errorLabel.set(((LocalError)evt.getNewValue()).getError());
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
