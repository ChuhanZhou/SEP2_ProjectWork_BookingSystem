package model.domain;

import model.domain.user.AccountInformation;
import model.domain.user.BasicInformation;
import model.domain.user.UserInterface;

import java.util.ArrayList;

public class UserList
{
  private ArrayList<UserInterface> userList;

  public UserList()
  {
    userList = new ArrayList<>();
  }

  public void addNewUser(UserInterface newUser)
  {
    if (newUser!=null)
    {
      if (getUserByEmail(newUser.getAccountInformation().getEmail())==null)
      {
        userList.add(newUser);
      }
    }
  }

  public void updateUserAccountInformation(AccountInformation newAccountInformation)
  {
    if (newAccountInformation!=null)
    {
      if (getUserByEmail(newAccountInformation.getEmail())!=null)
      {
        getUserByEmail(newAccountInformation.getEmail()).setPassword(newAccountInformation);
      }
    }
  }

  public void updateUserBasicInformation(BasicInformation newBasicInformation)
  {
    if (newBasicInformation!=null)
    {
      if (getUserByEmail(newBasicInformation.getEmail())!=null)
      {
        getUserByEmail(newBasicInformation.getEmail()).setBasicInformation(newBasicInformation);
      }
    }
  }

  public int getSize()
  {
    return userList.size();
  }

  public UserInterface getUserByIndex(int index)
  {
    if (index<=userList.size()-1&&index>=0)
    {
      return userList.get(index);
    }
    return null;
  }

  public UserInterface getUserByEmail(String email)
  {
    for (int x=0;x<userList.size();x++)
    {
      if (userList.get(x).getAccountInformation().getEmail().equals(email))
      {
        return userList.get(x);
      }
    }
    return null;
  }

  public void removeUserByIndex(int index)
  {
    if (index<=userList.size()-1&&index>=0)
    {
      userList.remove(index);
    }
  }

  public void removeUserByEmail(String email)
  {
    userList.remove(getUserByEmail(email));
  }

  public UserList copy()
  {
    UserList other = new UserList();
    for (int x=0;x<userList.size();x++)
    {
      other.addNewUser(userList.get(x).copy());
    }
    return other;
  }
}
