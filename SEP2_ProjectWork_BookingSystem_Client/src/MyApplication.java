import javafx.application.Application;
import javafx.stage.Stage;
import model.BookingModel;
import model.BookingModelManager;
import view.ViewHandler;
import viewmodel.ViewModelFactory;

import java.io.IOException;

public class MyApplication extends Application
{
  @Override public void start(Stage stage) throws IOException
  {
    BookingModel bookingModel = new BookingModelManager();
    ViewModelFactory viewModelFactory = new ViewModelFactory(bookingModel);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(stage);
  }
}
