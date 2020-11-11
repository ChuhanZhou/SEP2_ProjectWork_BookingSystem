package view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.util.converter.NumberStringConverter;
import viewmodel.HotelUpdateViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HotelUpdateViewController implements PropertyChangeListener
{
  private Region root;
  private ViewHandler viewHandler;
  private HotelUpdateViewModel viewModel;
  @FXML private TextField hotelNameText;
  @FXML private TextField hotelAddressText;
  @FXML private TextArea hotelDescriptionText;
  @FXML private TextArea characteristicText;
  @FXML private TextField checkInTimeText;
  @FXML private TextField checkOutTimeText;
  @FXML private TextArea facilitiesText;
  @FXML private TextArea rulesText;
  @FXML private TextField contactNumText;
  @FXML private Label errorLabel;
  private boolean clickSaveButton;

  public void init(ViewHandler viewHandler, HotelUpdateViewModel viewModel, Region root)
  {
    this.viewModel = viewModel;
    this.viewHandler=viewHandler;
    this.root=root;
    hotelNameText.textProperty().bindBidirectional(viewModel.hotelNameTextProperty());
    hotelAddressText.textProperty().bindBidirectional(viewModel.hotelAddressTextProperty());
    hotelDescriptionText.textProperty().bindBidirectional(viewModel.hotelDescriptionTextProperty());
    characteristicText.textProperty().bindBidirectional(viewModel.characteristicTextProperty());
    checkInTimeText.textProperty().bindBidirectional(viewModel.checkInTimeTextProperty());
    checkOutTimeText.textProperty().bindBidirectional(viewModel.checkOutTimeTextProperty());
    facilitiesText.textProperty().bindBidirectional(viewModel.facilitiesTextProperty());
    rulesText.textProperty().bindBidirectional(viewModel.rulesTextProperty());
    contactNumText.textProperty().bindBidirectional(viewModel.contactNumTextProperty());
    errorLabel.textProperty().bindBidirectional(viewModel.errorLabelProperty());
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

  public void onSaveBn()
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
          viewHandler.openView("HotelView");
          viewHandler.getViewModelFactory().getHotelViewModel().setInformation();
          clickSaveButton = false;
        }
        else
        {
          clickSaveButton = false;
        }
        break;
    }
  }
}
