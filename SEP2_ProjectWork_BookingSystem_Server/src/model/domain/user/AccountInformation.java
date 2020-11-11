package model.domain.user;

public class AccountInformation
{
  private String email;
  private String password;

  public AccountInformation(String email, String password)
  {
    this.email = email;
    this.password = password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public boolean securityCheck(String email,String password)
  {
    return this.email.equals(email)&&this.password.equals(password);
  }

  public boolean securityCheck(AccountInformation accountInformation)
  {
    return securityCheck(accountInformation.getEmail(),accountInformation.getPassword());
  }
}
