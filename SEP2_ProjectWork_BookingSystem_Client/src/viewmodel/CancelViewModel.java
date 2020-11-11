package viewmodel;

import model.BookingModel;
import model.domain.order.OrderInterface;

public class CancelViewModel
{
  private BookingModel model;
  private OrderInterface order;

  public CancelViewModel(BookingModel model)
  {
    this.model = model;
  }

  public void setInformation(OrderInterface order)
  {
    this.order = model.getOrder(order.getOrderNumber()).copy();
  }

  public OrderInterface getOrder()
  {
    return order;
  }

  public void clickYesButton()
  {
    order.cancelOrder();
    model.sendOrderPackage(order);
  }
}
