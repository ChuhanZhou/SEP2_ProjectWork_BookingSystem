package view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.ProfileViewModel;

public class ProfileViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private ProfileViewModel viewModel;

  @FXML private TextField firstNameText;
  @FXML private TextField lastNameText;
  @FXML private TextField phoneNumText;
  @FXML private ChoiceBox<String> genderBox;
  @FXML private TextField nationalityText;
  @FXML private TextField emailText;
  @FXML private Label errorLabel;


  public void init(ViewHandler viewHandler, ProfileViewModel viewModel, Region root)
  {
    this.viewModel = viewModel;
    this.viewHandler=viewHandler;
    this.root=root;
    firstNameText.textProperty().bindBidirectional(this.viewModel.firstNameTextProperty());
    lastNameText.textProperty().bindBidirectional(this.viewModel.lastNameTextProperty());
    phoneNumText.textProperty().bindBidirectional(this.viewModel.phoneNumTextProperty());
    genderBox.setItems(this.viewModel.getGenderBox());
    genderBox.valueProperty().bindBidirectional(this.viewModel.genderBoxValueProperty());
    nationalityText.textProperty().bindBidirectional(this.viewModel.nationalityTextProperty());
    emailText.textProperty().bindBidirectional(this.viewModel.emailTextProperty());
    errorLabel.textProperty().bindBidirectional(this.viewModel.errorLabelProperty());
  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void onSaveBtn()
  {
    viewModel.clickSaveButton();
  }

  @FXML private void onPasswordBtn()
  {
    viewHandler.openView("PasswordView");
  }

  @FXML private void onSignOutBtn()
  {
    viewModel.clickSignOutButton();
    viewHandler.getViewModelFactory().init();
    viewHandler.openView("LoginView");
  }

  @FXML private void onBackBtn()
  {
    viewHandler.openView("ReserveView");
    viewHandler.getViewModelFactory().getReserveViewModel().setInformation();
  }
}
