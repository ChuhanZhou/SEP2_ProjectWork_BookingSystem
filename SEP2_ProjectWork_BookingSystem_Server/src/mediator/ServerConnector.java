package mediator;

import model.BookingModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnector implements Runnable
{
  private BookingModel bookingModel;
  private int PORT = 8888;
  private ServerSocket welcomeSocket;

  public ServerConnector(BookingModel bookingModel)
  {
    this.bookingModel = bookingModel;
  }

  public void start() throws IOException
  {
    System.out.println("Starting server...");
    welcomeSocket = new ServerSocket(PORT);

    while (true)
    {
      System.out.println("Wait for a client...");
      Socket socket = welcomeSocket.accept();
      ServerHandler server = new ServerHandler(bookingModel,socket);
      Thread thread = new Thread(server);
      thread.setDaemon(true);
      thread.start();
    }
  }

  @Override public void run()
  {
    try
    {
      start();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
