package model.domain;

import model.domain.user.User;
import model.domain.user.UserInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserListTest
{
  private UserList userList;

  private int randomNumber()
  {
    return (int) (Math.random() * 100 + 0);
  }

  private int randomNumber(int max)
  {
    return (int) (Math.random() * max + 0);
  }

  private int randomNumber(int max,int min)
  {
    return (int) (Math.random() * (max - min) + min);
  }

  private void checkUserEquals(UserInterface user1,UserInterface user2)
  {
    assertEquals(user1.getAccountInformation().getEmail(),user2.getAccountInformation().getEmail());
    assertEquals(user1.getAccountInformation().getPassword(),user2.getAccountInformation().getPassword());
    assertEquals(user1.getBasicInformation().getEmail(),user2.getBasicInformation().getEmail());
    assertEquals(user1.getBasicInformation().getNationality(),user2.getBasicInformation().getNationality());
    assertEquals(user1.getBasicInformation().getPhoneNumber(),user2.getBasicInformation().getPhoneNumber());
    assertEquals(user1.getBasicInformation().getFirstName(),user2.getBasicInformation().getFirstName());
    assertEquals(user1.getBasicInformation().getLastName(),user2.getBasicInformation().getLastName());
    assertEquals(user1.getBasicInformation().getGender(),user2.getBasicInformation().getGender());
  }

  @BeforeEach void setUp()
  {
    userList = new UserList();
  }

  @AfterEach void tearDown()
  {
  }

  @Test void addNewUserZero()
  {
    assertEquals(0,userList.getSize());
    assertDoesNotThrow(()->userList.addNewUser(null));
    assertEquals(0,userList.getSize());
  }

  @Test void addNewUserOne()
  {
    assertEquals(0,userList.getSize());
    UserInterface user = new User("Email " + randomNumber(),"Password " + randomNumber());
    userList.addNewUser(user);
    assertEquals(1,userList.getSize());
    checkUserEquals(user,userList.getUserByIndex(0));
  }

  @Test void addNewUserMany()
  {
    ArrayList<UserInterface> userArrayList = new ArrayList<>();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      UserInterface user = new User("Email " + randomNumber(),"Password " + randomNumber());
      while (userList.getUserByEmail(user.getAccountInformation().getEmail())!=null)
      {
        user = new User("Email " + randomNumber(),"Password " + randomNumber());
      }
      userList.addNewUser(user);
      userArrayList.add(user);
    }
    for (int x=0;x<userList.getSize();x++)
    {
      checkUserEquals(userArrayList.get(x),userList.getUserByIndex(x));
    }
  }

  @Test void addNewUserBoundary()
  {
    assertEquals(0,userList.getSize());
    UserInterface user = new User("Email " + randomNumber(),"Password " + randomNumber());
    userList.addNewUser(user);
    assertEquals(1,userList.getSize());
    assertDoesNotThrow(()->userList.addNewUser(user));
    assertEquals(1,userList.getSize());
    UserInterface otherUser = new User(user.getAccountInformation().getEmail(),"Password " + randomNumber());
    assertDoesNotThrow(()->userList.addNewUser(otherUser));
    assertEquals(1,userList.getSize());
    checkUserEquals(user,userList.getUserByIndex(0));
  }

  @Test void addNewUserException()
  {
    //no exception
  }

  @Test void updateUserAccountInformationZero()
  {
    addNewUserMany();
    UserList userListCopy = userList.copy();
    assertDoesNotThrow(()->userList.updateUserAccountInformation(null));
    for (int x=0;x<userList.getSize();x++)
    {
      checkUserEquals(userList.getUserByIndex(x),userListCopy.getUserByIndex(x));
    }
  }

  @Test void updateUserAccountInformationOne()
  {
    addNewUserMany();
    int index = randomNumber(userList.getSize());
    UserInterface user = userList.getUserByIndex(index).copy();
    user.setPassword("Password " + randomNumber());
    userList.updateUserAccountInformation(user.getAccountInformation());
    checkUserEquals(userList.getUserByIndex(index),user);
  }

  @Test void updateUserAccountInformationMany()
  {
    addNewUserMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(userList.getSize());
      UserInterface user = userList.getUserByIndex(index).copy();
      user.setPassword("New Password " + randomNumber());
      userList.updateUserAccountInformation(user.getAccountInformation());
      checkUserEquals(userList.getUserByIndex(index),user);
    }
  }

  @Test void updateUserAccountInformationBoundary()
  {
    UserInterface user = new User("Email " + randomNumber(),"Password " + randomNumber());
    userList.addNewUser(user);
    UserInterface otherUser = new User("New " + user.getAccountInformation().getEmail(),user.getAccountInformation().getPassword());
    otherUser.setPassword("New Password " + randomNumber());
    userList.updateUserAccountInformation(otherUser.getAccountInformation());
    checkUserEquals(userList.getUserByIndex(0),user);
  }

  @Test void updateUserAccountInformationException()
  {
    //no exception
  }

  @Test void updateUserBasicInformationZero()
  {
    addNewUserMany();
    UserList userListCopy = userList.copy();
    assertDoesNotThrow(()->userList.updateUserBasicInformation(null));
    for (int x=0;x<userList.getSize();x++)
    {
      checkUserEquals(userList.getUserByIndex(x),userListCopy.getUserByIndex(x));
    }
  }

  @Test void updateUserBasicInformationOne()
  {
    addNewUserMany();
    int index = randomNumber(userList.getSize());
    UserInterface user = userList.getUserByIndex(index).copy();
    user.setBasicInformation("First Name " + randomNumber(),"Last Name " + randomNumber(),"Gender " + randomNumber(),"Nationality " + randomNumber(),"Phone Number" + randomNumber());
    userList.updateUserBasicInformation(user.getBasicInformation());
    checkUserEquals(userList.getUserByIndex(index),user);
  }

  @Test void updateUserBasicInformationMany()
  {
    addNewUserMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(userList.getSize());
      UserInterface user = userList.getUserByIndex(index).copy();
      user.setBasicInformation("First Name " + randomNumber(),"Last Name " + randomNumber(),"Gender " + randomNumber(),"Nationality " + randomNumber(),"Phone Number" + randomNumber());
      userList.updateUserBasicInformation(user.getBasicInformation());
      checkUserEquals(userList.getUserByIndex(index),user);
    }
  }

  @Test void updateUserBasicInformationBoundary()
  {
    UserInterface user = new User("Email " + randomNumber(),"Password " + randomNumber());
    userList.addNewUser(user);
    UserInterface otherUser = new User("New " + user.getAccountInformation().getEmail(),user.getAccountInformation().getPassword());
    otherUser.setBasicInformation("First Name " + randomNumber(),"Last Name " + randomNumber(),"Gender " + randomNumber(),"Nationality " + randomNumber(),"Phone Number" + randomNumber());
    userList.updateUserBasicInformation(otherUser.getBasicInformation());
    checkUserEquals(userList.getUserByIndex(0),user);
  }

  @Test void updateUserBasicInformationException()
  {
    //no exception
  }

  @Test void getUserByIndexZero()
  {
    assertNull(userList.getUserByIndex(-1));
    addNewUserMany();
    assertNull(userList.getUserByIndex(-1));
  }

  @Test void getUserByIndexOne()
  {
    addNewUserOne();
  }

  @Test void getUserByIndexMany()
  {
    addNewUserMany();
  }

  @Test void getUserByIndexBoundary()
  {
    addNewUserMany();
    assertNull(userList.getUserByIndex(userList.getSize()));
  }

  @Test void getUserByIndexException()
  {
    //no exception
  }

  @Test void getUserByEmailZero()
  {
    assertNull(userList.getUserByEmail(null));
    addNewUserMany();
    assertNull(userList.getUserByEmail(null));
  }

  @Test void getUserByEmailOne()
  {
    addNewUserMany();
    int index = randomNumber(userList.getSize());
    UserInterface user = userList.getUserByIndex(index).copy();
    checkUserEquals(userList.getUserByEmail(user.getAccountInformation().getEmail()),user);
  }

  @Test void getUserByEmailMany()
  {
    addNewUserMany();
    int time = randomNumber(100,1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(userList.getSize());
      UserInterface user = userList.getUserByIndex(index).copy();
      checkUserEquals(userList.getUserByEmail(user.getAccountInformation().getEmail()),user);
    }
  }

  @Test void getUserByEmailBoundary()
  {
    addNewUserMany();
    assertNull(userList.getUserByEmail("Email"));
  }

  @Test void getUserByEmailException()
  {
    //no exception
  }

  @Test void removeUserByIndexZero()
  {
    addNewUserMany();
    UserList userListCopy = userList.copy();
    userList.removeUserByIndex(-1);
    for (int x=0;x<userList.getSize();x++)
    {
      checkUserEquals(userList.getUserByIndex(x),userListCopy.getUserByIndex(x));
    }
  }

  @Test void removeUserByIndexOne()
  {
    addNewUserMany();
    int index = randomNumber(userList.getSize());
    UserInterface user = userList.getUserByIndex(index);
    userList.removeUserByIndex(index);
    assertNull(userList.getUserByEmail(user.getAccountInformation().getEmail()));
  }

  @Test void removeUserByIndexMany()
  {
    addNewUserMany();
    int time = randomNumber(userList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(userList.getSize());
      UserInterface user = userList.getUserByIndex(index);
      userList.removeUserByIndex(index);
      assertNull(userList.getUserByEmail(user.getAccountInformation().getEmail()));
    }
  }

  @Test void removeUserByIndexBoundary()
  {
    addNewUserMany();
    UserList userListCopy = userList.copy();
    userList.removeUserByIndex(userList.getSize());
    for (int x=0;x<userList.getSize();x++)
    {
      checkUserEquals(userList.getUserByIndex(x),userListCopy.getUserByIndex(x));
    }
  }

  @Test void removeUserByIndexException()
  {
    //no exception
  }

  @Test void removeUserByEmailZero()
  {
    addNewUserMany();
    UserList userListCopy = userList.copy();
    userList.removeUserByEmail(null);
    for (int x=0;x<userList.getSize();x++)
    {
      checkUserEquals(userList.getUserByIndex(x),userListCopy.getUserByIndex(x));
    }
  }

  @Test void removeUserByEmailOne()
  {
    addNewUserMany();
    int index = randomNumber(userList.getSize());
    UserInterface user = userList.getUserByIndex(index);
    userList.removeUserByEmail(user.getAccountInformation().getEmail());
    assertNull(userList.getUserByEmail(user.getAccountInformation().getEmail()));
  }

  @Test void removeUserByEmailMany()
  {
    addNewUserMany();
    int time = randomNumber(userList.getSize(),1);
    for (int x=0;x<time;x++)
    {
      int index = randomNumber(userList.getSize());
      UserInterface user = userList.getUserByIndex(index);
      userList.removeUserByEmail(user.getAccountInformation().getEmail());
      assertNull(userList.getUserByEmail(user.getAccountInformation().getEmail()));
    }
  }

  @Test void removeUserByEmailBoundary()
  {
    addNewUserMany();
    UserList userListCopy = userList.copy();
    userList.removeUserByEmail("Email");
    for (int x=0;x<userList.getSize();x++)
    {
      checkUserEquals(userList.getUserByIndex(x),userListCopy.getUserByIndex(x));
    }
  }

  @Test void removeUserByEmailException()
  {
    //no exception
  }

  @Test void copyZero()
  {
    UserList other = userList.copy();
    assertNotEquals(userList,other);
  }

  @Test void copyOne()
  {
    userList.addNewUser(new User("Email " + randomNumber(),"Password " + randomNumber()));
    UserList other = userList.copy();
    assertNotEquals(userList,other);
    assertNotEquals(userList.getUserByIndex(0).hashCode(),other.getUserByIndex(0).hashCode());
    assertNotEquals(userList.getUserByIndex(0).getAccountInformation(),other.getUserByIndex(0).getAccountInformation());
    assertNotEquals(userList.getUserByIndex(0).getBasicInformation(),other.getUserByIndex(0).getBasicInformation());
    checkUserEquals(userList.getUserByIndex(0),other.getUserByIndex(0));
  }

  @Test void copyMany()
  {
    int time = randomNumber();
    for (int x=0;x<time;x++)
    {
      userList.addNewUser(new User("Email " + randomNumber(),"Password " + randomNumber()));
    }
    UserList other = userList.copy();
    assertNotEquals(userList,other);
    for (int x=0;x<userList.getSize();x++)
    {
      assertNotEquals(userList.getUserByIndex(x).hashCode(),other.getUserByIndex(x).hashCode());
      assertNotEquals(userList.getUserByIndex(x).getAccountInformation(),other.getUserByIndex(x).getAccountInformation());
      assertNotEquals(userList.getUserByIndex(x).getBasicInformation(),other.getUserByIndex(x).getBasicInformation());
      checkUserEquals(userList.getUserByIndex(x),other.getUserByIndex(x));
    }
  }

  @Test void copyBoundary()
  {
    //no boundary;
  }

  @Test void copyException()
  {
    //no exception
  }
}