package model.domain.order;

public class OrderState
{
  private String state;

  public OrderState(String state)
  {
    this.state = state;
  }

  public String getState()
  {
    return state;
  }

  public void changeState(Order order)
  {
    order.setOrderState(new OrderCancel());
  }

  public OrderState copy()
  {
    switch (state)
    {
      case "Confirm":
        return new OrderConfirm();
      case "Cancel":
        return new OrderCancel();
    }
    return null;
  }
}
