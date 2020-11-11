package utility.doubleClick;

public class ClickTime0 extends MouseState
{
  ClickTime0()
  {
    super(0);
  }

  @Override public void click(Mouse mouse)
  {
    mouse.setMouseState(new ClickTime1(mouse));
  }
}
