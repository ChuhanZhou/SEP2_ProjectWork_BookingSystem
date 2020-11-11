package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.user.UserInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PasswordViewModel implements PropertyChangeListener
{
  private BookingModel model;
  private StringProperty oldText;
  private StringProperty newText;
  private StringProperty verText;
  private StringProperty errorLabel;
  private boolean clickSaveButton;

  public PasswordViewModel(BookingModel model)
  {
    clickSaveButton = false;
    this.model = model;
    oldText = new SimpleStringProperty();
    newText = new SimpleStringProperty();
    verText = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    this.model.addListener("error",this);
  }

  public void clear()
  {
    oldText.setValue(null);
    newText.setValue(null);
    verText.setValue(null);
    errorLabel.setValue(null);
  }

  public void clickSaveButton()
  {
    clickSaveButton = true;
    if (model.getUser().getAccountInformation().getPassword().equals(oldText.getValue()))
    {
      if (newText.getValue()!=null)
      {
        String[] newPassword = ("[ " + newText.get() + " ]").split(" ");
        if (newPassword.length!=3)
        {
          errorLabel.setValue("Please write the new password without blank!");
          clickSaveButton = false;
        }
        else
        {
          if (newText.getValue().equals(verText.getValue()))
          {
            UserInterface user = model.getUser().copy();
            user.setPassword(newText.getValue());
            model.sendUserPackage(user);
          }
          else
          {
            errorLabel.setValue("Please write the new password in Verification again!");
            clickSaveButton = false;
          }
        }
      }
      else
      {
        errorLabel.setValue("Please write a new password!");
        clickSaveButton = false;
      }
    }
    else
    {
      errorLabel.setValue("The old password is wrong!");
      clickSaveButton = false;
    }
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  public StringProperty verTextProperty()
  {
    return verText;
  }

  public StringProperty newTextProperty()
  {
    return newText;
  }

  public StringProperty oldTextProperty()
  {
    return oldText;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {

        case "error":
          errorLabel.setValue(((LocalError) evt.getNewValue()).getError());
          if (clickSaveButton&&errorLabel.getValue()==null)
          {
            errorLabel.setValue("Succeed!");
            clickSaveButton = false;
          }
          else
          {
            clickSaveButton = false;
          }
          break;
      }
    });
  }
}
