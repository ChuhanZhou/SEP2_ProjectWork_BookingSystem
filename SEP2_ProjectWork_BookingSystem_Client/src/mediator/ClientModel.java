package mediator;

import model.BookingModel;
import model.domain.hotel.RoomInformationList;
import model.domain.order.Order;
import model.domain.order.OrderInterface;
import model.domain.room.TimeRangeList;
import model.domain.user.UserInterface;

public interface ClientModel
{
  boolean connect(BookingModel bookingModel);
  void disconnect();
  void sendUserPackage(UserInterface user);
  void sendOrderPackage(OrderInterface order);
  void sendSearchPackage(RoomInformationList roomInformationList, TimeRangeList timeRangeList);
  void start(String action, UserInterface user);
}
