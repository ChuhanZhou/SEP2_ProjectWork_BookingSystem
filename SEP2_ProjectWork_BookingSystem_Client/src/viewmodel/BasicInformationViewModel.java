package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BookingModel;
import model.domain.LocalError;
import model.domain.order.Order;
import model.domain.order.OrderInterface;
import model.domain.user.BasicInformation;
import model.domain.user.UserInterface;
import utility.NamedPropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BasicInformationViewModel implements PropertyChangeListener,
    NamedPropertyChangeSubject
{
  private BookingModel bookingModel;
  private StringProperty firstNameTextField;
  private StringProperty lastNameTextField;
  private StringProperty phoneNumberTextField;
  private StringProperty genderTextField;
  private ObservableList<String> genderChoice;
  private StringProperty nationalityTextField;
  private StringProperty errorLabel;
  private BasicInformation basicInformation;
  private PropertyChangeSupport property;
  private OrderInterface order;

  public BasicInformationViewModel(BookingModel bookingModel)
  {
    order = null;
    property = new PropertyChangeSupport(this);
    this.bookingModel = bookingModel;
    firstNameTextField = new SimpleStringProperty();
    lastNameTextField = new SimpleStringProperty();
    phoneNumberTextField = new SimpleStringProperty();
    genderTextField = new SimpleStringProperty();
    genderChoice = FXCollections.observableArrayList();
    nationalityTextField = new SimpleStringProperty();
    errorLabel = new SimpleStringProperty();
    clear();
    basicInformation = bookingModel.getUser().getBasicInformation().copy();
    setInformation(basicInformation);
    this.bookingModel.addListener("updateUser",this);
    this.bookingModel.addListener("error",this);
  }

  private void setInformation(BasicInformation basicInformation)
  {
    this.basicInformation = basicInformation;
    Platform.runLater(()->{
      firstNameTextField.set(this.basicInformation.getFirstName());
      lastNameTextField.set(this.basicInformation.getLastName());
      phoneNumberTextField.set(this.basicInformation.getPhoneNumber());
      genderTextField.set(this.basicInformation.getGender());
      genderChoice.clear();
      genderChoice.addAll("M","F");
      nationalityTextField.set(this.basicInformation.getNationality());
      if (order!=null)
      {
        order.setBasicInformation(this.basicInformation.copy());
      }
    });
  }

  public void setOrder(OrderInterface order)
  {
    this.order = order.copy();
    this.order.setBasicInformation(basicInformation);
  }

  public OrderInterface getOrder()
  {
    return order;
  }

  public void clear()
  {
    firstNameTextField.set("");
    lastNameTextField.set("");
    phoneNumberTextField.set("");
    genderTextField.set("");
    genderChoice.clear();
    genderChoice.addAll("M","F");
    nationalityTextField.set("");
    errorLabel.set("");
  }

  public StringProperty firstNameTextFieldProperty()
  {
    return firstNameTextField;
  }

  public StringProperty lastNameTextFieldProperty()
  {
    return lastNameTextField;
  }

  public StringProperty genderTextFieldProperty()
  {
    return genderTextField;
  }

  public ObservableList<String> getGenderChoice()
  {
    return genderChoice;
  }

  public StringProperty nationalityTextField()
  {
    return nationalityTextField;
  }

  public StringProperty phoneNumberTextFieldProperty()
  {
    return phoneNumberTextField;
  }

  public StringProperty errorLabelProperty()
  {
    return errorLabel;
  }

  public void clickNextbtn()
  {
    UserInterface user = bookingModel.getUser().copy();
    user.setBasicInformation(firstNameTextField.getValue(),lastNameTextField.getValue(),genderTextField.getValue(),nationalityTextField.getValue(),phoneNumberTextField.getValue());
    bookingModel.sendUserPackage(user);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(()->{
      switch (evt.getPropertyName())
      {
        case "error":
          errorLabel.set(((LocalError) evt.getNewValue()).getError());
          property.firePropertyChange("error",-1,errorLabel.getValue());
          break;
        case "updateUser":
          setInformation(((UserInterface) evt.getNewValue()).getBasicInformation());
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
