package view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import viewmodel.HotelViewModel;

public class HotelViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private HotelViewModel viewModel;
  @FXML private Label hotelNameLabel;
  @FXML private Label addressLabel;
  @FXML private Label descriptionLabel;
  @FXML private Label characteristicsLabel;
  @FXML private DatePicker checkIn;
  @FXML private DatePicker checkOut;
  @FXML private ComboBox<String> roomTypeBox;
  @FXML private ComboBox<String> settingsBox;
  @FXML private ListView<String> ordersListView;
  @FXML private Label facilitiesLabel;
  @FXML private Label inTimeLabel;
  @FXML private Label outTimeLabel;
  @FXML private Label rulesLabel;
  @FXML private Label contactNumLabel;

  public void init(ViewHandler viewHandler, HotelViewModel viewModel, Region root)
  {
    this.viewModel = viewModel;
    this.viewHandler=viewHandler;
    this.root=root;
    hotelNameLabel.textProperty().bindBidirectional(this.viewModel.hotelNameProperty());
    addressLabel.textProperty().bindBidirectional(this.viewModel.addressProperty());
    descriptionLabel.textProperty().bindBidirectional(this.viewModel.descriptionProperty());
    characteristicsLabel.textProperty().bindBidirectional(this.viewModel.characteristicsProperty());
    roomTypeBox.setItems(this.viewModel.getRoomType());
    roomTypeBox.valueProperty().bindBidirectional(this.viewModel.roomTypeValueProperty());
    settingsBox.setItems(this.viewModel.getSettings());
    settingsBox.valueProperty().bindBidirectional(this.viewModel.settingsValueProperty());
    ordersListView.setItems(this.viewModel.getSearchListView());
    facilitiesLabel.textProperty().bindBidirectional(this.viewModel.facilitiesProperty());
    inTimeLabel.textProperty().bindBidirectional(this.viewModel.checkInTimeProperty());
    outTimeLabel.textProperty().bindBidirectional(this.viewModel.checkOutTimeProperty());
    rulesLabel.textProperty().bindBidirectional(this.viewModel.rulesProperty());
    contactNumLabel.textProperty().bindBidirectional(this.viewModel.contactNumberProperty());
    checkIn.valueProperty().bindBidirectional(this.viewModel.checkInProperty());
    checkOut.valueProperty().bindBidirectional(this.viewModel.checkOutProperty());
  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickSettings()
  {
    if (settingsBox.getValue()!=null)
    {
      switch (settingsBox.getValue())
      {
        case "Search":
          viewHandler.openView("SearchView");
          viewHandler.getViewModelFactory().getSearchViewModel().setInformation();
          break;
        case "Manage Hotel":
          viewHandler.openView("HotelUpdateView");
          viewHandler.getViewModelFactory().getHotelUpdateViewModel().setInformation();
          break;
        default:
          break;
      }
    }
  }

  @FXML private void clickCheckIn()
  {
    viewModel.clickCheckIn();
  }

  @FXML private void clickCheckOut()
  {
    viewModel.clickCheckOut();
  }

  @FXML private void clickCheckRoomType()
  {
    viewModel.search();
  }

  @FXML private void clickSearchListView()
  {
    if (viewModel.clickSearchListView())
    {
      if (viewModel.getOrderByIndex(ordersListView.getSelectionModel().getSelectedIndex())!=null)
      {
        viewHandler.openView("OrderView");
        viewHandler.getViewModelFactory().getOrderViewModel().setInformation(viewModel.getOrderByIndex(ordersListView.getSelectionModel().getSelectedIndex()),"HotelView");
      }
    }
  }
}
