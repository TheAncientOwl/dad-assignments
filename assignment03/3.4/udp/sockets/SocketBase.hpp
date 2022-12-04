#pragma once

#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>

namespace udp::sockets {
  class SocketBase {
  protected:
    void _send(const sockaddr* address, const void* buffer, size_t size);

    int _receive(sockaddr* address, void* buffer, size_t size);

  public:
    SocketBase(uint16_t port);
    ~SocketBase();

    void create();

    void close();

  protected:
    int m_FileDescriptor;
    sockaddr_in m_Address;
  };
} // namespace udp::socket
