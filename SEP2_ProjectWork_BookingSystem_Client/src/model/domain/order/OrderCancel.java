package model.domain.order;

public class OrderCancel extends OrderState
{
  public OrderCancel()
  {
    super("Cancel");
  }

  @Override public void changeState(Order order)
  {
    order.setOrderState(new OrderCancel());
  }

  @Override public OrderState copy()
  {
    return new OrderCancel();
  }
}
