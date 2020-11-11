package view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import viewmodel.ReserveViewModel;

public class ReserveViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private ReserveViewModel viewModel;
  @FXML private DatePicker checkIn;
  @FXML private DatePicker checkOut;
  @FXML private ComboBox<String> roomType;
  @FXML private ComboBox<String> MY;
  @FXML private Label hotelName;
  @FXML private Label address;
  @FXML private Label description;
  @FXML private Label characteristics;
  @FXML private ListView<String> searchListView;
  @FXML private Label facilities;
  @FXML private Label checkInTime;
  @FXML private Label checkOutTime;
  @FXML private Label rules;
  @FXML private Label contactNumber;

  public void init(ViewHandler viewHandler,ReserveViewModel viewModel,Region root)
  {
    this.viewModel = viewModel;
    this.viewHandler=viewHandler;
    this.root=root;
    hotelName.textProperty().bindBidirectional(this.viewModel.hotelNameProperty());
    address.textProperty().bindBidirectional(this.viewModel.addressProperty());
    description.textProperty().bindBidirectional(this.viewModel.descriptionProperty());
    characteristics.textProperty().bindBidirectional(this.viewModel.characteristicsProperty());
    facilities.textProperty().bindBidirectional(this.viewModel.facilitiesProperty());
    checkInTime.textProperty().bindBidirectional(this.viewModel.checkInTimeProperty());
    checkOutTime.textProperty().bindBidirectional(this.viewModel.checkOutTimeProperty());
    rules.textProperty().bindBidirectional(this.viewModel.rulesProperty());
    contactNumber.textProperty().bindBidirectional(this.viewModel.contactNumberProperty());
    searchListView.setItems(this.viewModel.getSearchListView());
    roomType.setItems(this.viewModel.getRoomType());
    roomType.valueProperty().bindBidirectional(this.viewModel.roomTypeValueProperty());
    MY.setItems(this.viewModel.getMY());
    MY.valueProperty().bindBidirectional(this.viewModel.MYValueProperty());
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

  @FXML private void clickMY()
  {
    if (MY.getValue()!=null)
    {
      switch (MY.getValue())
      {
        case "Profile":
          viewHandler.openView("ProfileView");
          viewHandler.getViewModelFactory().getProfileViewModel().setInformation();
          break;
        case "Bookings":
          viewHandler.openView("BookingsView");
          viewHandler.getViewModelFactory().getBookingsViewModel().setInformation();
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
    if (viewModel.getSearchInformationList().getRoomInformationByIndex(searchListView.getSelectionModel().getSelectedIndex())!=null&&viewModel.clickSearchListView())
    {
      viewHandler.openView("RoomView");
      viewHandler.getViewModelFactory().getRoomViewModel().setInformation(viewModel.getTimeRangeListFromCheckInAndCheckOut(),viewModel.getSearchInformationList().getRoomInformationByIndex(searchListView.getSelectionModel().getSelectedIndex()));
    }
  }
}
