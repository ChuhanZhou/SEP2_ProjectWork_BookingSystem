package view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.BasicInformationViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BasicInformationViewController implements PropertyChangeListener
{
  private ViewHandler viewHandler;
  private BasicInformationViewModel basicInformationViewModel;
  private Region root;
  @FXML private TextField firstNameTextField;
  @FXML private TextField lastNameTextField;
  @FXML private TextField phoneNumberTextField;
  @FXML private ChoiceBox<String> genderChoice;
  @FXML private TextField nationalityTextField;
  @FXML private Label errorLabel;
  private boolean clickNextBtn;

  public void init(ViewHandler viewHandler,BasicInformationViewModel basicInformationViewModel,Region root)
  {
    clickNextBtn = false;
    this.viewHandler=viewHandler;
    this.basicInformationViewModel=basicInformationViewModel;
    this.root=root;
    firstNameTextField.textProperty().bindBidirectional(this.basicInformationViewModel.firstNameTextFieldProperty());
    lastNameTextField.textProperty().bindBidirectional(this.basicInformationViewModel.lastNameTextFieldProperty());
    phoneNumberTextField.textProperty().bindBidirectional(this.basicInformationViewModel.phoneNumberTextFieldProperty());
    genderChoice.valueProperty().bindBidirectional(this.basicInformationViewModel.genderTextFieldProperty());
    genderChoice.setItems(FXCollections.observableList(this.basicInformationViewModel.getGenderChoice()));
    nationalityTextField.textProperty().bindBidirectional(this.basicInformationViewModel.nationalityTextField());
    errorLabel.textProperty().bindBidirectional(this.basicInformationViewModel.errorLabelProperty());
    this.basicInformationViewModel.addListener("error",this);
  }

  public void reset()
  {
    basicInformationViewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickNextBtn()
  {
    clickNextBtn = true;
    basicInformationViewModel.clickNextbtn();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        if (evt.getNewValue()==null&&clickNextBtn)
        {
          viewHandler.openView("RoomView");
          viewHandler.getViewModelFactory().getRoomViewModel().setOrder(basicInformationViewModel.getOrder());
          clickNextBtn = false;
        }
        else
        {
          clickNextBtn = false;
        }
        break;
    }
  }
}
