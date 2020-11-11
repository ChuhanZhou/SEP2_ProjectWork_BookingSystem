package mediator;

import com.google.gson.Gson;
import information.*;
import model.BookingModel;
import model.domain.OrderList;
import model.domain.error.ErrorInterface;
import model.domain.hotel.RoomInformationList;
import model.domain.order.Order;
import model.domain.order.OrderInterface;
import model.domain.room.TimeRangeList;
import model.domain.user.AccountInformation;
import model.domain.user.User;
import model.domain.user.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements ClientModel
{
  private static final int PORT = 8888;
  private static final String HOST = "localhost";
  private int port;
  private String host;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private Gson gson;
  private boolean running;
  private boolean login;
  private BookingModel bookingModel;

  public Client()
  {
    this(PORT,HOST);
  }

  public Client(int port)
  {
    this(port,HOST);
  }

  public Client(String host)
  {
    this(PORT,host);
  }

  public Client(int port,String host)
  {
    this.port = port;
    this.host = host;
  }

  @Override public boolean connect(BookingModel bookingModel)
  {
    try
    {
      socket = new Socket(host,port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(),true);
      running = true;
      login = false;
      this.bookingModel = bookingModel;
      gson = new Gson();
      return true;
    }
    catch (IOException e)
    {
      bookingModel.updateError(e.getMessage());
      return false;
    }
  }

  @Override public void disconnect()
  {
    try
    {
      socket.close();
      in.close();
      out.close();
      running = false;
      login = false;
    }
    catch (IOException e)
    {
      bookingModel.updateError(e.getMessage());
    }
  }

  private void sendInformationPackage(InformationPackage informationPackage)
  {
    String send = gson.toJson(informationPackage);
    out.println(send);
  }

  @Override public void sendUserPackage(UserInterface user)
  {
    sendInformationPackage(new UserPackage(user));
  }

  @Override public void sendOrderPackage(OrderInterface order)
  {
    sendInformationPackage(new OrderPackage(order));
  }

  @Override public void sendSearchPackage(RoomInformationList roomInformationList, TimeRangeList timeRangeList)
  {
    sendInformationPackage(new SearchPackage(roomInformationList,timeRangeList));
  }

  @Override public void start(String action, UserInterface user)
  {
    running = true;
    String receive;
    ErrorPackage errorPackage;
    InformationPackage informationPackage;
    OrderPackage orderPackage;
    UserPackage userPackage;
    HotelPackage hotelPackage;
    SearchPackage searchPackage;
    try
    {
      while (running)
      {
        out.println(action);
        sendUserPackage(user);
        receive = in.readLine();
        errorPackage = gson.fromJson(receive,ErrorPackage.class);
        bookingModel.updateError(errorPackage.getErrorInformation());
        login = !errorPackage.isError();
        running = !errorPackage.isError();
        while (login)
        {
          receive = in.readLine();
          informationPackage = gson.fromJson(receive,InformationPackage.class);
          switch (informationPackage.getInformationType())
          {
            case HOTEL:
              hotelPackage = gson.fromJson(receive,HotelPackage.class);
              bookingModel.updateHotel(hotelPackage.getHotel());
              break;
            case USER:
              userPackage = gson.fromJson(receive,UserPackage.class);
              bookingModel.updateUser(userPackage.getUser());
              break;
            case ORDER:
              orderPackage = gson.fromJson(receive,OrderPackage.class);
              OrderList orderList = orderPackage.getOrderList();
              for (int x=0;x<orderList.getSize();x++)
              {
                if (bookingModel.getOrder(orderList.getOrderByIndex(x).getOrderNumber())==null)
                {
                  bookingModel.addNewOrder(orderList.getOrderByIndex(x));
                }
                else
                {
                  bookingModel.updateOrder(orderList.getOrderByIndex(x));
                }
              }
              break;
            case SEARCH:
              searchPackage = gson.fromJson(receive,SearchPackage.class);
              bookingModel.updateSearchInformation(searchPackage.getRoomInformationList());
            case ERROR:
              errorPackage = gson.fromJson(receive,ErrorPackage.class);
              bookingModel.updateError(errorPackage.getErrorInformation());
              break;
          }
        }
      }
    }
    catch (IOException e)
    {
      bookingModel.updateError(e.getMessage());
    }
  }
}
