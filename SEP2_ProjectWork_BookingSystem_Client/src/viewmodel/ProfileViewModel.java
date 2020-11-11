package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.user.UserInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProfileViewModel implements PropertyChangeListener
{
  private BookingModel model;
  private StringProperty firstNameText;
  private StringProperty lastNameText;
  private StringProperty phoneNumText;
  private StringProperty nationalityText;
  private StringProperty emailText;
  private StringProperty errorLabel;
  private ObservableList<String> genderBox;
  private StringProperty genderBoxValue;
  private boolean clickSave;

  public ProfileViewModel(BookingModel model)
  {
    clickSave = false;
    this.model = model;
    firstNameText = new SimpleStringProperty();
    lastNameText = new SimpleStringProperty();
    phoneNumText = new SimpleStringProperty();
    nationalityText = new SimpleStringProperty();
    emailText = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    genderBoxValue = new SimpleStringProperty();
    genderBox = FXCollections.observableArrayList();
    this.model.addListener("updateUser",this);
    this.model.addListener("error",this);
  }

  public void clickSaveButton()
  {
    clickSave = true;
    UserInterface user = model.getUser().copy();
    user.setBasicInformation(firstNameText.getValue(),lastNameText.getValue(),genderBoxValue.getValue(),nationalityText.getValue(),phoneNumText.getValue());
    model.sendUserPackage(user);
  }

  public void clickSignOutButton()
  {
    model.signOut();
  }

  public void clear()
  {
    firstNameText.setValue(null);
    lastNameText.setValue(null);
    phoneNumText.setValue(null);
    nationalityText.setValue(null);
    emailText.setValue(null);
    errorLabel.setValue(null);
    genderBoxValue.setValue(null);
    genderBox.clear();
  }

  public void setInformation()
  {
    Platform.runLater(()->{
      firstNameText.setValue(model.getUser().getBasicInformation().getFirstName());
      lastNameText.setValue(model.getUser().getBasicInformation().getLastName());
      phoneNumText.setValue(model.getUser().getBasicInformation().getPhoneNumber());
      nationalityText.setValue(model.getUser().getBasicInformation().getNationality());
      emailText.setValue(model.getUser().getBasicInformation().getEmail());
      genderBox.clear();
      genderBoxValue.setValue(model.getUser().getBasicInformation().getGender());
      genderBox.addAll("M","F");
    });
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  public StringProperty emailTextProperty()
  {
    return emailText;
  }

  public StringProperty firstNameTextProperty()
  {
    return firstNameText;
  }

  public StringProperty lastNameTextProperty()
  {
    return lastNameText;
  }

  public StringProperty nationalityTextProperty()
  {
    return nationalityText;
  }

  public StringProperty phoneNumTextProperty()
  {
    return phoneNumText;
  }

  public StringProperty genderBoxValueProperty()
  {
    return genderBoxValue;
  }

  public ObservableList<String> getGenderBox()
  {
    return genderBox;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "error":
          if (clickSave&&((LocalError)evt.getNewValue()).getError()==null)
          {
            errorLabel.setValue("Succeed!");
          }
          else
          {
            errorLabel.setValue(((LocalError)evt.getNewValue()).getError());
          }
          break;
        case "updateUser":
          setInformation();
          break;
      }
    });
  }
}
