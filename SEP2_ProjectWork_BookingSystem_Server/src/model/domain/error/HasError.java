package model.domain.error;

public class HasError extends Error
{
  public HasError(String error)
  {
    super(true, error);
  }

}
