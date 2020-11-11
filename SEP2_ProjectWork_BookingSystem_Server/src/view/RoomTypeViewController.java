package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import viewmodel.RoomTypeViewModel;
import viewmodel.RoomViewModel;

public class RoomTypeViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private RoomTypeViewModel roomTypeViewModel;
  @FXML private Label roomTypeLabel;
  @FXML private Label sizeLabel;
  @FXML private Label facilitiesLabel;
  @FXML private ListView<String> serviceList;

  public void init(ViewHandler viewHandler,RoomTypeViewModel roomTypeViewModel,Region root)
  {
    this.roomTypeViewModel=roomTypeViewModel;
    this.viewHandler=viewHandler;
    this.root=root;
    roomTypeLabel.textProperty().bindBidirectional(this.roomTypeViewModel.roomTypeLabelProperty());
    sizeLabel.textProperty().bindBidirectional(this.roomTypeViewModel.sizeLabelProperty());
    facilitiesLabel.textProperty().bindBidirectional(this.roomTypeViewModel.facilitiesLabelProperty());
    serviceList.setItems(this.roomTypeViewModel.getServiceList());
  }

  public void reset()
  {
    roomTypeViewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickUpdateButton()
  {
    viewHandler.openView("RoomTypeUpdateView");
    viewHandler.getViewModelFactory().getRoomTypeUpdateViewModel().setInformation(roomTypeViewModel.getRoomInformation());
  }

  @FXML private void backBtn()
  {
    viewHandler.openView("SearchView");
    viewHandler.getViewModelFactory().getSearchViewModel().setInformation("Room Type");
  }
}
