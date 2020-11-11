package model.domain.error;

public class NoError extends Error
{
  public NoError()
  {
    super(false, null);
  }
}
