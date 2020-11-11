package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.RegisterViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RegisterViewController implements PropertyChangeListener
{
  private ViewHandler viewHandler;
  private RegisterViewModel registerViewModel;
  private Region root;
  @FXML private TextField emailAddText;
  @FXML private TextField passwordText;
  @FXML private TextField verText;
  @FXML private Label errorLabel;
  private boolean clickRegisterBtn;

  public void init(ViewHandler viewHandler,RegisterViewModel registerViewModel,Region root)
  {
    clickRegisterBtn = false;
    this.viewHandler=viewHandler;
    this.registerViewModel=registerViewModel;
    this.root=root;
    emailAddText.textProperty().bindBidirectional(this.registerViewModel.emailAddTextProperty());
    passwordText.textProperty().bindBidirectional(this.registerViewModel.passwordTextProperty());
    verText.textProperty().bindBidirectional(this.registerViewModel.verTextProperty());
    errorLabel.textProperty().bindBidirectional(this.registerViewModel.errorLabelProperty());
    this.registerViewModel.addListener("error",this);
  }

  public void reset()
  {
    registerViewModel.clear();
  }

  public Region getRoot(){
    return root;
  }

  @FXML private void clickBackBtn()
  {
    registerViewModel.clickBackButton();
    viewHandler.openView("LoginView");
  }

  @FXML private void clickRegisterBtn()
  {
    clickRegisterBtn = true;
    registerViewModel.clickRegisterBtn();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        if (evt.getNewValue()==null&&clickRegisterBtn)
        {
          viewHandler.openView("ReserveView");
          viewHandler.getViewModelFactory().getReserveViewModel().setInformation();
          clickRegisterBtn = false;
        }
        else
        {
          clickRegisterBtn = false;
        }
        break;
    }
  }
}
