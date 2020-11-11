package utility.doubleClick;

public class ClickTime1 extends MouseState
{
  private boolean timeout;

  ClickTime1(Mouse mouse)
  {
    super(1);
    timeout = false;
    new Thread(()->timer(mouse)).start();
  }

  private void timer(Mouse mouse)
  {
    try
    {
      Thread.sleep(200);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    if (!timeout)
    {
      timeout = true;
      mouse.setMouseState(new ClickTime0());
    }
  }

  @Override public void click(Mouse mouse)
  {
    if (!timeout)
    {
      timeout = true;
      mouse.setMouseState(new ClickTime2(mouse));
    }
  }
}
