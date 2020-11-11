package model.domain;

import model.domain.user.User;

public class DomainListReaderWriter implements DomainListReaderWriterInterface
{
  private UserList userList;
  private RoomList roomList;
  private OrderList orderList;
  private int reader;
  private int writer;

  public DomainListReaderWriter(UserList userList,RoomList roomList,OrderList orderList)
  {
    this.userList = userList;
    this.roomList = roomList;
    this.orderList = orderList;
    reader = 0;
    writer = 0;
  }

  private synchronized void acquireRead()
  {
    while (writer>0)
    {
      try
      {
        wait();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    reader++;
  }

  private synchronized void acquireWrite()
  {
    while (reader>0||writer>0)
    {
      try
      {
        wait();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    writer++;
  }

  @Override public synchronized UserList acquireReadUserList()
  {
    acquireRead();
    return userList.copy();
  }

  @Override public synchronized UserList acquireWriteUserList()
  {
    acquireWrite();
    return userList;
  }

  @Override public synchronized RoomList acquireReadRoomList()
  {
    acquireRead();
    return roomList.copy();
  }

  @Override public synchronized RoomList acquireWriteRoomList()
  {
    acquireWrite();
    return roomList;
  }

  @Override public synchronized OrderList acquireReadOrderList()
  {
    acquireRead();
    return orderList.copy();
  }

  @Override public synchronized OrderList acquireWriteOrderList()
  {
    acquireWrite();
    return orderList;
  }

  @Override public synchronized void releaseRead()
  {
    reader--;
    notifyAll();
  }

  @Override public synchronized void releaseWrite()
  {
    writer--;
    notifyAll();
  }
}
