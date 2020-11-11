package model.domain;

public interface DomainListReaderWriterInterface
{
  OrderList acquireReadOrderList();
  OrderList acquireWriteOrderList();
  void releaseRead();
  void releaseWrite();
}
