package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.LoginViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginViewController implements PropertyChangeListener
{
  private ViewHandler viewHandler;
  private LoginViewModel loginViewModel;
  private Region root;
  @FXML private TextField ipAddressTextField;
  @FXML private TextField emailTextField;
  @FXML private TextField passwordTextField;
  @FXML private Label errorLabel;
  private boolean clickSignInButton;

  public void init(ViewHandler viewHandler,LoginViewModel loginViewModel,Region root)
  {
    clickSignInButton = false;
    this.viewHandler = viewHandler;
    this.loginViewModel = loginViewModel;
    this.root = root;
    ipAddressTextField.textProperty().bindBidirectional(this.loginViewModel.getIpAddressProperty());
    emailTextField.textProperty().bindBidirectional(this.loginViewModel.getEmailProperty());
    passwordTextField.textProperty().bindBidirectional(this.loginViewModel.getPasswordProperty());
    errorLabel.textProperty().bindBidirectional(this.loginViewModel.getErrorProperty());
    loginViewModel.addListener("error",this);
  }

  public void reset()
  {
    loginViewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickSignInButton()
  {
    clickSignInButton = true;
    loginViewModel.clickSignInButton();
  }

  @FXML private void clickRegisterButton()
  {
    if (loginViewModel.clickRegisterButton())
    {
      viewHandler.openView("RegisterView");
    }
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        if (evt.getNewValue()==null&&clickSignInButton)
        {
          viewHandler.openView("ReserveView");
          viewHandler.getViewModelFactory().getReserveViewModel().setInformation();
          clickSignInButton = false;
        }
        else
        {
          clickSignInButton = false;
        }
        break;
    }
  }
}
