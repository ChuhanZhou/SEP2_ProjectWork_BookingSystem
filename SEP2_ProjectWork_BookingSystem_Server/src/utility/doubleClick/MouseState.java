package utility.doubleClick;

public abstract class MouseState
{
  private int clickTime;

  MouseState(int clickTime)
  {
    this.clickTime = clickTime;
  }

  public abstract void click(Mouse mouse);

  public int getClickTime()
  {
    return clickTime;
  }
}
