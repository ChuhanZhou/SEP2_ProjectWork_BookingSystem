package information;

import model.domain.user.AccountInformation;
import model.domain.user.User;
import model.domain.user.UserInterface;

public class UserPackage extends InformationPackage
{
  private User user;

  public UserPackage(AccountInformation accountInformation)
  {
    super(InformationType.USER);
    user = new User(accountInformation.getEmail(),accountInformation.getPassword());
  }

  public UserPackage(UserInterface user)
  {
    super(InformationType.USER);
    this.user = user.copy();
  }

  public UserInterface getUser()
  {
    return user;
  }
}
