package model.domain.user;

public interface UserInterface
{
  void setPassword(String password);
  void setPassword(AccountInformation accountInformation);
  void setBasicInformation(BasicInformation basicInformation);
  void setBasicInformation(String firstName,String lastName,String gender, String nationality,String phoneNumber);
  AccountInformation getAccountInformation();
  BasicInformation getBasicInformation();
  User copy();
  boolean equals(Object obj);
}
