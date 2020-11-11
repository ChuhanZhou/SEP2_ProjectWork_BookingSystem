package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.EndViewModel;


public class EndViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private EndViewModel viewModel;
  @FXML private Label orderNum;
  @FXML private Label name;
  @FXML private Label type;
  @FXML private Label roomNum;
  @FXML private Label checkInDate;
  @FXML private Label checkOutDate;
  @FXML private Label price;
  @FXML private Label email;
  @FXML private ListView<String> services;

  public void init(ViewHandler viewHandler,EndViewModel viewModel, javafx.scene.layout.Region root)
  {
    this.viewModel = viewModel;
    this.viewHandler=viewHandler;
    this.root=root;
    Bindings.bindBidirectional(orderNum.textProperty(),this.viewModel.orderNumProperty(),new NumberStringConverter());
    Bindings.bindBidirectional(roomNum.textProperty(),this.viewModel.roomNumProperty(),new NumberStringConverter());
    price.textProperty().bindBidirectional(this.viewModel.priceProperty());
    name.textProperty().bindBidirectional(this.viewModel.nameProperty());
    type.textProperty().bindBidirectional(this.viewModel.typeProperty());
    checkInDate.textProperty().bindBidirectional(this.viewModel.checkInDateProperty());
    checkOutDate.textProperty().bindBidirectional(this.viewModel.checkOutDateProperty());
    email.textProperty().bindBidirectional(this.viewModel.emailProperty());
    services.setItems(this.viewModel.getServices());
  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickBackButton()
  {
    viewHandler.openView("ReserveView");
    viewHandler.getViewModelFactory().getReserveViewModel().setInformation();
  }
}
