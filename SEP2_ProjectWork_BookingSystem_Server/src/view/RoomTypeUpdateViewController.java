package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.RoomTypeUpdateViewModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RoomTypeUpdateViewController implements PropertyChangeListener
{
  private ViewHandler viewHandler;
  private Region root;
  private RoomTypeUpdateViewModel roomTypeUpdateViewModel;
  @FXML private TextField typeText;
  @FXML private TextField priceText;
  @FXML private TextField sizeText;
  @FXML private TextArea facilitiesText;
  @FXML private ListView<String> serviceListView;
  @FXML private ListView<String> roomServicesList;
  @FXML private Label errorLabel;
  private boolean clickSaveButton;

  public void init(ViewHandler viewHandler, RoomTypeUpdateViewModel roomTypeUpdateViewModel,Region root)
  {
    clickSaveButton = false;
    this.viewHandler=viewHandler;
    this.roomTypeUpdateViewModel=roomTypeUpdateViewModel;
    this.root=root;
    typeText.textProperty().bindBidirectional(this.roomTypeUpdateViewModel.typeTextProperty());
    facilitiesText.textProperty().bindBidirectional(this.roomTypeUpdateViewModel.facilitiesTextProperty());
    errorLabel.textProperty().bindBidirectional(this.roomTypeUpdateViewModel.errorLabelProperty());
    priceText.textProperty().bindBidirectional(this.roomTypeUpdateViewModel.priceTextProperty());
    sizeText.textProperty().bindBidirectional(this.roomTypeUpdateViewModel.sizeTextProperty());
    serviceListView.setItems(this.roomTypeUpdateViewModel.getServiceListView());
    roomServicesList.setItems(this.roomTypeUpdateViewModel.getRoomServicesList());
    this.roomTypeUpdateViewModel.addListener("error",this);
  }

  public Region getRoot()
  {
    return root;
  }

  public void reset()
  {
    roomTypeUpdateViewModel.clear();
  }

  @FXML private void clickRoomServicesList()
  {
    roomTypeUpdateViewModel.clickRoomServicesList(roomServicesList.getSelectionModel().getSelectedIndex());
  }

  @FXML private void clickServiceListView()
  {
    roomTypeUpdateViewModel.clickServiceListView(serviceListView.getSelectionModel().getSelectedIndex());
  }

  @FXML private void SaveBtn()
  {
    clickSaveButton = true;
    roomTypeUpdateViewModel.clickSaveButton();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        if (evt.getNewValue()==null&&clickSaveButton)
        {
          if (roomTypeUpdateViewModel.getOldRoomInformation()==null)
          {
            viewHandler.openView("SearchView");
            viewHandler.getViewModelFactory().getSearchViewModel().setInformation("Room Type");
          }
          else
          {
            viewHandler.openView("RoomTypeView");
            viewHandler.getViewModelFactory().getRoomTypeViewModel().setInformation(roomTypeUpdateViewModel.getNewRoomInformation());
          }
          clickSaveButton = false;
        }
        else
        {
          clickSaveButton = false;
        }
        break;
    }
  }
}
