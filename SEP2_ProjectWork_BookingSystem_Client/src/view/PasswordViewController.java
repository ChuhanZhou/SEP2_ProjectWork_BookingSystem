package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.PasswordViewModel;

public class PasswordViewController
{
  private ViewHandler viewHandler;
  private PasswordViewModel passwordViewModel;
  private Region root;
  @FXML private TextField oldText;
  @FXML private TextField newText;
  @FXML private TextField verText;
  @FXML private Label errorLabel;

  public void init(ViewHandler viewHandler,PasswordViewModel passwordViewModel,Region root)
  {
    this.viewHandler=viewHandler;
    this.passwordViewModel=passwordViewModel;
    this.root=root;
    oldText.textProperty().bindBidirectional(this.passwordViewModel.oldTextProperty());
    newText.textProperty().bindBidirectional(this.passwordViewModel.newTextProperty());
    verText.textProperty().bindBidirectional(this.passwordViewModel.verTextProperty());
    errorLabel.textProperty().bindBidirectional(this.passwordViewModel.errorLabelProperty());
  }

  public void reset()
  {
    passwordViewModel.clear();
  }

  public Region getRoot(){
    return root;
  }

  @FXML private void clickBackBtn()
  {
    viewHandler.openView("ProfileView");
    viewHandler.getViewModelFactory().getProfileViewModel().setInformation();
  }

  @FXML private void clickSaveBtn()
  {
    passwordViewModel.clickSaveButton();
  }
}
