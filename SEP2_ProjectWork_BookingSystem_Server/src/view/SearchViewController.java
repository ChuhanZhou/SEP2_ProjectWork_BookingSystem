package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import viewmodel.SearchViewModel;

public class SearchViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private SearchViewModel viewModel;
  @FXML private ComboBox<String> typeBox;
  @FXML private ComboBox<String> keyWordBox;
  @FXML private TextField searchField;
  @FXML private ListView<String> listView;
  @FXML private Label errorLabel;
  @FXML private Button deleteButton;
  @FXML private Button addButton;

  public void init(ViewHandler viewHandler, SearchViewModel viewModel, Region root)
  {
    this.viewModel = viewModel;
    this.viewHandler=viewHandler;
    this.root=root;
    typeBox.setItems(this.viewModel.getTypeBox());
    typeBox.valueProperty().bindBidirectional(this.viewModel.typeBoxValueProperty());
    keyWordBox.setItems(this.viewModel.getKeyWordBox());
    keyWordBox.valueProperty().bindBidirectional(this.viewModel.keyWordBoxValueProperty());
    searchField.textProperty().bindBidirectional(this.viewModel.searchFieldProperty());
    listView.setItems(this.viewModel.getListView());
    errorLabel.textProperty().bindBidirectional(this.viewModel.errorLabelProperty());
    addButton.textProperty().bindBidirectional(this.viewModel.addButtonValueProperty());
    addButton.disableProperty().bindBidirectional(this.viewModel.addButtonStyleProperty());
    deleteButton.textProperty().bindBidirectional(this.viewModel.deleteButtonValueProperty());
    deleteButton.disableProperty().bindBidirectional(this.viewModel.deleteButtonStyleProperty());
  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickTypeBox()
  {
    viewModel.clickTypeBox();
  }

  @FXML private void onSearchBtn()
  {
    viewModel.clickSearchButton();
  }

  @FXML private void onAddBtn()
  {
    String viewName = viewModel.clickAddButton();
    if (viewName!=null)
    {
      viewHandler.openView(viewName);
      switch (viewName)
      {
        case "RoomView":
          viewHandler.getViewModelFactory().getRoomViewModel().setInformation();
          break;
        case "RoomTypeUpdateView":
          viewHandler.getViewModelFactory().getRoomTypeUpdateViewModel().setInformation();
          break;
        case "ServiceView":
          viewHandler.getViewModelFactory().getServiceViewModel().setInformation();
          break;
      }
    }
  }

  @FXML private void onDeleteBtn()
  {
    viewModel.clickDeleteButton(listView.getSelectionModel().getSelectedIndex());
  }

  @FXML private void clickListView()
  {
    String viewName = viewModel.clickListView(listView.getSelectionModel().getSelectedIndex());
    if (viewName!=null)
    {
      viewHandler.openView(viewName);
      switch (viewName)
      {
        case "OrderView":
          viewHandler.getViewModelFactory().getOrderViewModel().setInformation(viewModel.getSearchOrderByIndex(listView.getSelectionModel().getSelectedIndex()),"SearchView");
        case "RoomView":
          viewHandler.getViewModelFactory().getRoomViewModel().setInformation(viewModel.getSearchRoomByIndex(listView.getSelectionModel().getSelectedIndex()));
        case "RoomTypeView":
          viewHandler.getViewModelFactory().getRoomTypeViewModel().setInformation(viewModel.getSearchRoomInformationByIndex(listView.getSelectionModel().getSelectedIndex()));
        case "ServiceView":
          viewHandler.getViewModelFactory().getServiceViewModel().setInformation(viewModel.getSearchOptionServiceByIndex(listView.getSelectionModel().getSelectedIndex()));
      }
    }
  }

  @FXML private void onBackBtn()
  {
    viewHandler.openView("HotelView");
    viewHandler.getViewModelFactory().getHotelViewModel().setInformation();
  }
}
