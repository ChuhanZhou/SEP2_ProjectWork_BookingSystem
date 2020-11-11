package utility.doubleClick;

public class Mouse implements VirtualMouse
{
  private MouseState mouseState;

  public Mouse()
  {
    mouseState = new ClickTime0();
  }

  void setMouseState(MouseState mouseState)
  {
    this.mouseState = mouseState;
  }

  @Override public void click()
  {
    mouseState.click(this);
  }

  @Override public int getClickTime()
  {
    return mouseState.getClickTime();
  }
}
