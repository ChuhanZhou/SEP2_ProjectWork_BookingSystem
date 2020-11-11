import javafx.application.Application;
import javafx.stage.Stage;
import mediator.ServerConnector;
import model.BookingModel;
import model.BookingModelManager;
import view.ViewHandler;
import viewmodel.ViewModelFactory;

public class MyApplication extends Application
{
  @Override public void start(Stage stage) throws Exception
  {
    BookingModel bookingModel = new BookingModelManager("jdbc:postgresql://localhost:5432/postgres","postgres","294409");
    ServerConnector server = new ServerConnector(bookingModel);
    ViewModelFactory viewModelFactory = new ViewModelFactory(bookingModel);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(stage);
    Thread thread = new Thread(server,"[Server]");
    thread.setDaemon(true);
    thread.start();
  }
}
