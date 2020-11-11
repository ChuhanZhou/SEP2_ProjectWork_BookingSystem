package information;

import model.domain.error.Error;
import model.domain.error.ErrorInterface;
import model.domain.error.HasError;
import model.domain.error.NoError;

public class ErrorPackage extends InformationPackage
{
  private Error error;

  public ErrorPackage(String error)
  {
    super(InformationType.ERROR);
    this.error = new HasError(error);
  }

  public ErrorPackage()
  {
    super(InformationType.ERROR);
    this.error = new NoError();
  }

  public ErrorInterface getError()
  {
    return error;
  }

  public String getErrorInformation()
  {
    return error.getError();
  }

  public boolean isError()
  {
    return error.isError();
  }

  @Override public String toString()
  {
    return super.toString() + error.getError() + "<" + error.isError() + ">";
  }
}
