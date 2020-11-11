package view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.OrderViewModel;

public class OrderViewController
{
  private OrderViewModel orderViewModel;
  private Region root;
  private ViewHandler viewHandler;
  @FXML private Label numLabel;
  @FXML private Label nameLabel;
  @FXML private Label typeLabel;
  @FXML private ListView<String> servicesList;
  @FXML private Label roomNumLabel;
  @FXML private Label inLabel;
  @FXML private Label outLabel;
  @FXML private Label priceLabel;
  @FXML private Label errorLabel;

  public void init(ViewHandler viewHandler,OrderViewModel orderViewModel,Region root)
  {
    this.orderViewModel = orderViewModel;
    this.root = root;
    this.viewHandler = viewHandler;
    Bindings.bindBidirectional(numLabel.textProperty(),this.orderViewModel.getNumLabelProperty(), new NumberStringConverter());
    nameLabel.textProperty().bindBidirectional(this.orderViewModel.getNameLabelProperty());
    typeLabel.textProperty().bindBidirectional(this.orderViewModel.getTypeLabelProperty());
    Bindings.bindBidirectional(roomNumLabel.textProperty(),this.orderViewModel.getRnumLabelProperty(), new NumberStringConverter());
    inLabel.textProperty().bindBidirectional(this.orderViewModel.getInLabelProperty());
    outLabel.textProperty().bindBidirectional(this.orderViewModel.getOutLabelProperty());
    priceLabel.textProperty().bindBidirectional(this.orderViewModel.getPriceLabelProperty());
    errorLabel.textProperty().bindBidirectional(this.orderViewModel.errorLabelProperty());
    servicesList.setItems(this.orderViewModel.getServicesList());
  }

  public void reset()
  {
    orderViewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickBackBtn()
  {
    viewHandler.openView(orderViewModel.getLastWindow());
    switch (orderViewModel.getLastWindow())
    {
      case "HotelView":
        viewHandler.getViewModelFactory().getHotelViewModel().setInformation();
        break;
      case "SearchView":
        viewHandler.getViewModelFactory().getSearchViewModel().setInformation("Order");
        break;
    }
  }
}
