package mediator;

import com.google.gson.Gson;
import information.*;
import model.BookingModel;
import model.domain.RoomList;
import model.domain.error.ErrorInterface;
import model.domain.hotel.HotelInterface;
import model.domain.hotel.RoomInformationList;
import model.domain.order.OrderCancel;
import model.domain.order.OrderConfirm;
import model.domain.order.OrderInterface;
import model.domain.order.OrderState;
import model.domain.user.User;
import model.domain.user.UserInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler implements Runnable, PropertyChangeListener
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private BookingModel bookingModel;
  private Gson gson;
  private String email;
  private boolean connect;
  private boolean login;

  public ServerHandler(BookingModel bookingModel,Socket socket)
      throws IOException
  {
    this.bookingModel = bookingModel;
    this.socket = socket;
    gson = new Gson();
    in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    out = new PrintWriter(this.socket.getOutputStream(),true);
    connect = true;
    login = false;
    this.bookingModel.addListener("user",this);
    this.bookingModel.addListener("addOrder",this);
    this.bookingModel.addListener("updateOrder",this);
    this.bookingModel.addListener("cancelOrder",this);
    this.bookingModel.addListener("hotel",this);
  }

  public void close()
  {
    try
    {
      socket.close();
      in.close();
      out.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void sendErrorPackage()
  {
    sendInformationPackage(new ErrorPackage());
  }

  private void sendErrorPackage(String error)
  {
    sendInformationPackage(new ErrorPackage(error));
  }

  private void sendErrorPackage(ErrorInterface error)
  {
    if (error.isError())
    {
      sendErrorPackage(error.getError());
    }
    else
    {
      sendErrorPackage();
    }
  }

  private void sendInformationPackage(InformationPackage informationPackage)
  {
    String send = gson.toJson(informationPackage);
    out.println(send);
  }

  private void login() throws IOException
  {
    String receive;
    receive = in.readLine();
    InformationPackage informationPackage = gson.fromJson(receive,InformationPackage.class);
    if (informationPackage.getInformationType() == InformationType.USER)
    {
      UserPackage userPackage = gson.fromJson(receive,UserPackage.class);
      UserInterface receiveUser = userPackage.getUser();
      UserInterface searchUser = bookingModel.getUser(receiveUser.getAccountInformation().getEmail());
      if (searchUser!=null)
      {
        if (searchUser.getAccountInformation().securityCheck(receiveUser.getAccountInformation()))
        {
          sendErrorPackage();
          afterLogin(searchUser);
        }
        else
        {
          sendErrorPackage("Wrong Password!");
        }
      }
      else
      {
        sendErrorPackage("Wrong Email!");
      }
    }
    else
    {
      sendErrorPackage("Wrong Package!");
    }
  }

  private void register()
  {
    try
    {
      String receive;
      receive = in.readLine();
      InformationPackage informationPackage = gson.fromJson(receive,InformationPackage.class);
      if (informationPackage.getInformationType() == InformationType.USER)
      {
        UserPackage userPackage = gson.fromJson(receive,UserPackage.class);
        UserInterface receiveUser = userPackage.getUser();
        ErrorInterface error = bookingModel.addNewUser(receiveUser.getAccountInformation());
        sendErrorPackage(error);
        if (!error.isError())
        {
          afterLogin(bookingModel.getUser(receiveUser.getAccountInformation().getEmail()));
        }
      }
      else
      {
        sendErrorPackage("Wrong Package!");
      }
    }
    catch (IOException e)
    {
      //e.printStackTrace();
    }
  }

  private void afterLogin(UserInterface user)
  {
    try
    {
      String receive;
      InformationPackage informationPackage;
      UserPackage userPackage;
      OrderPackage orderPackage;
      SearchPackage searchPackage;
      ErrorPackage errorPackage;
      login = true;
      email = user.getAccountInformation().getEmail();
      sendInformationPackage(new HotelPackage(bookingModel.getHotel()));
      sendInformationPackage(new UserPackage(user));
      sendInformationPackage(new OrderPackage(bookingModel.getOrders(user)));
      while (login)
      {
        receive = in.readLine();
        informationPackage = gson.fromJson(receive,InformationPackage.class);
        if (informationPackage==null)
        {
          break;
        }
        switch (informationPackage.getInformationType())
        {
          case HOTEL:
            break;
          case USER:
            userPackage = gson.fromJson(receive,UserPackage.class);
            sendErrorPackage(bookingModel.updateUser(userPackage.getUser()));
            break;
          case ORDER:
            orderPackage = gson.fromJson(receive,OrderPackage.class);
            if (orderPackage.getFirstOrder().getOrderNumber()==-1)
            {
              sendErrorPackage(bookingModel.addNewOrder(orderPackage.getFirstOrder()));
            }
            else
            {
              if (orderPackage.getFirstOrder().getOrderState().getState().equals("Cancel"))
              {
                bookingModel.cancelOrder(orderPackage.getFirstOrder().getOrderNumber());
              }
              sendErrorPackage(bookingModel.updateOrder(orderPackage.getFirstOrder()));
            }
            break;
          case SEARCH:
            searchPackage = gson.fromJson(receive,SearchPackage.class);
            RoomInformationList roomInformationList = new RoomInformationList();
            RoomList roomList = bookingModel.getRoomsByFreeTimeRangeList(searchPackage.getTimeRangeList());
            if (searchPackage.getRoomInformationList().getSize()==1)
            {
              roomList = roomList.getRoomsByType(searchPackage.getRoomInformationList().getRoomInformationByIndex(0).getType());
            }
            for (int x=0;x<roomList.getSize();x++)
            {
              roomInformationList.addNewRoomInformation(bookingModel.getRoomInformationList().getRoomInformationByType(roomList.getRoomByIndex(x).getType()));
            }
            sendInformationPackage(new SearchPackage(roomInformationList));
          case ERROR:
            errorPackage = gson.fromJson(receive,ErrorPackage.class);
            if (errorPackage.isError())
            {
              bookingModel.updateError(errorPackage.getErrorInformation());
            }
            break;
        }
      }
    }
    catch (IOException e)
    {
      //e.printStackTrace();
    }
  }

  @Override public void run()
  {
    String receive;
    try
    {
      while (connect)
      {
        receive = in.readLine();
        if (receive!=null)
        {
          switch (receive)
          {
            case "login":
              login();
              break;
            case "register":
              register();
              break;
            default:
              sendErrorPackage("Wrong keyword!");
              break;
          }
        }
      }
    }
    catch (IOException e)
    {
      close();
      //e.printStackTrace();
      connect = false;
      login = false;
    }
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (login)
    {
      switch (evt.getPropertyName())
      {
        case "user":
          User user = (User) evt.getNewValue();
          if (user.getAccountInformation().getEmail().equals(email))
          {
            sendInformationPackage(new UserPackage(user));
          }
          break;
        case "addOrder":
        case "updateOrder":
        case "cancelOrder":
          OrderInterface order = (OrderInterface) evt.getNewValue();
          if (order.getBasicInformation().getEmail().equals(email))
          {
            sendInformationPackage(new OrderPackage(order));
          }
          break;
        case "hotel":
          sendInformationPackage(new HotelPackage((HotelInterface) evt.getNewValue()));
          break;
      }
    }
  }
}
