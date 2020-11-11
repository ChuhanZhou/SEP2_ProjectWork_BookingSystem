package model.domain.user;

public class User implements UserInterface
{
  private AccountInformation accountInformation;
  private BasicInformation basicInformation;

  public User(String firstName,String lastName,String gender, String nationality,String phoneNumber,String email,String password)
  {
    accountInformation = new AccountInformation(email,password);
    basicInformation = new BasicInformation(firstName,lastName,gender,nationality,email,phoneNumber);
  }

  public User(String email,String password)
  {
    accountInformation = new AccountInformation(email,password);
    basicInformation = new BasicInformation(email);
  }

  public void setPassword(String password)
  {
    accountInformation.setPassword(password);
  }

  public void setPassword(AccountInformation accountInformation)
  {
    this.accountInformation.setPassword(accountInformation.getPassword());
  }

  public void setBasicInformation(BasicInformation basicInformation)
  {
    setBasicInformation(basicInformation.getFirstName(),basicInformation.getLastName(),basicInformation.getGender(),basicInformation.getNationality(),basicInformation.getPhoneNumber());
  }

  public void setBasicInformation(String firstName,String lastName,String gender, String nationality,String phoneNumber)
  {
    basicInformation.setFirstName(firstName);
    basicInformation.setLastName(lastName);
    basicInformation.setGender(gender);
    basicInformation.setNationality(nationality);
    basicInformation.setPhoneNumber(phoneNumber);
  }

  public AccountInformation getAccountInformation()
  {
    return accountInformation;
  }

  public BasicInformation getBasicInformation()
  {
    return basicInformation;
  }

  public User copy()
  {
    User other = new User(accountInformation.getEmail(),accountInformation.getPassword());
    other.setBasicInformation(basicInformation.copy());
    return other;
  }

  @Override public boolean equals(Object obj)
  {
    if (obj instanceof User)
    {
      if (((User) obj).getAccountInformation().getEmail()
          .equals(accountInformation.getEmail()))
      {
        return true;
      }
    }
    return false;
  }
}
