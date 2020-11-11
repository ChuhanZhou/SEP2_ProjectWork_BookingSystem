package model.domain.order;

public class OrderConfirm extends OrderState
{
  public OrderConfirm()
  {
    super("Confirm");
  }

  @Override public void changeState(Order order)
  {
    order.setOrderState(new OrderCancel());
  }

  @Override public OrderState copy()
  {
    return new OrderConfirm();
  }
}
