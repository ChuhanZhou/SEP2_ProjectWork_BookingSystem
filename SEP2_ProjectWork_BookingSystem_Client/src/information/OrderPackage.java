package information;

import model.domain.OrderList;
import model.domain.order.OrderInterface;

public class OrderPackage extends InformationPackage
{
  private OrderList orderList;

  public OrderPackage(OrderList orderList)
  {
    super(InformationType.ORDER);
    this.orderList = orderList;
  }

  public OrderPackage(OrderInterface order)
  {
    super(InformationType.ORDER);
    orderList = new OrderList();
    orderList.addNewOrder(order.copy());
  }

  public OrderList getOrderList()
  {
    return orderList;
  }

  public OrderInterface getFirstOrder()
  {
    return orderList.getOrderByIndex(0);
  }
}
