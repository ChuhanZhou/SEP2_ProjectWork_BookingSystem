package model.domain;

public interface DomainListReaderWriterInterface
{
  UserList acquireReadUserList();
  UserList acquireWriteUserList();
  RoomList acquireReadRoomList();
  RoomList acquireWriteRoomList();
  OrderList acquireReadOrderList();
  OrderList acquireWriteOrderList();
  void releaseRead();
  void releaseWrite();
}
