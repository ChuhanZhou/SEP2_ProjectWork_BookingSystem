package model.domain;

import model.domain.user.User;

public class DomainListReaderWriter implements DomainListReaderWriterInterface
{
  private OrderList orderList;
  private int reader;
  private int writer;

  public DomainListReaderWriter(OrderList orderList)
  {
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
