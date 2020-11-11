package view;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import viewmodel.CancelViewModel;

public class CancelViewController
{
  private Region root;
  private ViewHandler viewHandler;
  private CancelViewModel viewModel;

  public void init(ViewHandler viewHandler,CancelViewModel viewModel, Region root)
  {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;
    this.root = root;
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void clickYesBtn()
  {
    viewModel.clickYesButton();
    viewHandler.openView("BookingsView");
    viewHandler.getViewModelFactory().getBookingsViewModel().setInformation();
  }

  @FXML private void clickNoBtn()
  {
    viewHandler.openView("OrderView");
    viewHandler.getViewModelFactory().getOrderViewModel().setInformation(viewModel.getOrder());
  }
}
