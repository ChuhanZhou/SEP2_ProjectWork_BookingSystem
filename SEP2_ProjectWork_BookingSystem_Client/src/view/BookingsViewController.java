package view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import viewmodel.BookingsViewModel;

public class BookingsViewController
{
  private ViewHandler viewHandler;
  private Region root;
  private BookingsViewModel bookingsViewModel;
  @FXML private ListView<String> listView;

  public void init(ViewHandler viewHandler,BookingsViewModel bookingsViewModel,Region root)
  {
    this.bookingsViewModel=bookingsViewModel;
    this.root=root;
    this.viewHandler=viewHandler;
    listView.setItems(this.bookingsViewModel.getListView());
  }

  public void reset(){
    bookingsViewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickOrderList()
  {
    if (bookingsViewModel.clickOrderList())
    {
      viewHandler.openView("OrderView");
      viewHandler.getViewModelFactory().getOrderViewModel().setInformation(bookingsViewModel.getOrder(listView.getSelectionModel().getSelectedIndex()));
    }
  }

  @FXML private void clickBackBtn()
  {
    viewHandler.openView("ReserveView");
    viewHandler.getViewModelFactory().getReserveViewModel().setInformation();
  }
}
