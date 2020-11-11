package view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.RoomViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RoomViewController implements PropertyChangeListener
{
  private ViewHandler viewHandler;
  private RoomViewModel roomViewModel;
  private Region root;
  @FXML private TextField roomNumTextField;
  @FXML private ComboBox<String> typeBox;
  @FXML private Label errorLabel;
  private boolean clickSaveButton;

  public void init(ViewHandler viewHandler,RoomViewModel roomViewModel, Region root)
  {
    clickSaveButton = false;
    this.viewHandler = viewHandler;
    this.roomViewModel = roomViewModel;
    this.root = root;
    roomNumTextField.textProperty().bindBidirectional(this.roomViewModel.getRoomNumProperty());
    errorLabel.textProperty().bindBidirectional(roomViewModel.getErrorProperty());
    typeBox.setItems(roomViewModel.getTypeProperty());
    typeBox.valueProperty().bindBidirectional(this.roomViewModel.typeBoxValueProperty());
    roomViewModel.addListener("error",this);
  }

  public void reset()
  {
    roomViewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickSaveButton()
  {
    clickSaveButton = true;
    roomViewModel.clickSaveButton();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        if (evt.getNewValue() == null && clickSaveButton)
        {
          clickSaveButton = false;
          viewHandler.openView("SearchView");
          viewHandler.getViewModelFactory().getSearchViewModel().setInformation("Room");
        }
        else
        {
          clickSaveButton = false;
        }
        break;
    }
  }
}
