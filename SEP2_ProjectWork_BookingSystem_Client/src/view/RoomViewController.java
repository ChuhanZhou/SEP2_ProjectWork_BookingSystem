package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.RoomViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RoomViewController implements PropertyChangeListener
{
  private ViewHandler viewHandler;
  private Region root;
  private RoomViewModel roomViewModel;
  @FXML private Label sizeLabel;
  @FXML private Label serviceTypeLabel;
  @FXML private Label facilitiesLabel;
  @FXML private Label errorLabel;
  @FXML private ListView<String> freeServiceListView;
  @FXML private ListView<String> allServiceListView;
  @FXML private ListView<String> choiceListView;
  private boolean clickReservebtn;

  public void init(ViewHandler viewHandler,RoomViewModel roomViewModel,Region root)
  {
    clickReservebtn = false;
    this.roomViewModel=roomViewModel;
    this.root=root;
    this.viewHandler=viewHandler;
    Bindings.bindBidirectional(sizeLabel.textProperty(),this.roomViewModel.sizeLabelProperty(),new NumberStringConverter());
    serviceTypeLabel.textProperty().bindBidirectional(this.roomViewModel.serviceTypeLabelProperty());
    facilitiesLabel.textProperty().bindBidirectional(this.roomViewModel.facilitiesLabelProperty());
    errorLabel.textProperty().bindBidirectional(this.roomViewModel.errorLabelProperty());
    freeServiceListView.setItems(this.roomViewModel.getFreeServiceListView());
    allServiceListView.setItems(this.roomViewModel.getAllServiceListView());
    choiceListView.setItems(this.roomViewModel.getChoiceListView());
    this.roomViewModel.addListener("error",this);
  }

  public void reset()
  {
    roomViewModel.clear();
  }

  public Region getRoot(){
    return root;
  }

  @FXML private void clickAllServiceListView()
  {
    roomViewModel.clickAllServiceListView(allServiceListView.getSelectionModel().getSelectedIndex());
  }

  @FXML private void clickChoiceListView()
  {
    roomViewModel.clickChoiceListView(choiceListView.getSelectionModel().getSelectedIndex());
  }

  @FXML private void clickBackbtn()
  {
    viewHandler.openView("ReserveView");
    viewHandler.getViewModelFactory().getReserveViewModel().setInformation();
  }

  @FXML private void clickReservebtn()
  {
    clickReservebtn = true;
    if (roomViewModel.clickReservebtn())
    {
      clickReservebtn = false;
      viewHandler.openView("BasicInformationView");
      viewHandler.getViewModelFactory().getBasicInformationViewModel().setOrder(roomViewModel.getOrder());
    }
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "error":
        if (evt.getNewValue()==null&&clickReservebtn)
        {
          viewHandler.openView("EndView");
          viewHandler.getViewModelFactory().getEndViewModel().setInformation(roomViewModel.getOrder());
          clickReservebtn = false;
        }
        else
        {
          clickReservebtn = false;
        }
        break;
    }
  }
}
