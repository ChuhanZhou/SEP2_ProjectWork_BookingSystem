package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.ServiceViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ServiceViewController implements PropertyChangeListener
{
  private Region root;
  private ViewHandler viewHandler;
  private ServiceViewModel viewModel;
  @FXML private TextField serviceTypeText;
  @FXML private TextField priceText;
  @FXML private Label errorLabel;
  private boolean clickSaveButton;

  public void init(ViewHandler viewHandler, ServiceViewModel viewModel, Region root)
  {
    clickSaveButton = false;
    this.viewModel = viewModel;
    this.viewHandler=viewHandler;
    this.root=root;
    serviceTypeText.textProperty().bindBidirectional(viewModel.serviceTypeTextProperty());
    errorLabel.textProperty().bindBidirectional(this.viewModel.errorLabelProperty());
    priceText.textProperty().bindBidirectional(this.viewModel.priceTextProperty());
    this.viewModel.addListener("error",this);
  }

  public void reset()
  {
    viewModel.clear();
  }

  public Region getRoot()
  {
    return root;
  }

  @FXML private void onSaveBtn()
  {
    clickSaveButton = true;
    viewModel.clickSaveButton();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        if (evt.getNewValue()==null&&clickSaveButton)
        {
          clickSaveButton = false;
          viewHandler.openView("SearchView");
          viewHandler.getViewModelFactory().getSearchViewModel().setInformation("Service");
        }
        else
        {
          clickSaveButton = false;
        }
        break;
    }
  }
}
