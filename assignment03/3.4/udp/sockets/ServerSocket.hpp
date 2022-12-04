#pragma once

#include "SocketBase.hpp"

namespace udp::sockets {
  class ServerSocket : public SocketBase {
  public:
    ServerSocket(uint16_t port);

    void bind();

    int receive(void* buffer, size_t size);

    void respond(const void* buffer, size_t size);

  private:
    sockaddr_in m_ClientAddress;
  };
} // namespace udp::sockets
