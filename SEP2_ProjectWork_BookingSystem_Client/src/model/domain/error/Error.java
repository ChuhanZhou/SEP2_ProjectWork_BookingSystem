package model.domain.error;

public class Error implements ErrorInterface
{
  private String error;
  private boolean isError;

  public Error(boolean isError,String error)
  {
    this.error = error;
    this.isError = isError;
  }

  public String getError()
  {
    return error;
  }

  public boolean isError()
  {
    return isError;
  }
}
