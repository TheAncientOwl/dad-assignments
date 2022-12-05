#pragma once

#include "SocketBase.hpp"

namespace udp::sockets {
  class ClientSocket : public SocketBase {
  public:
    ClientSocket(uint16_t port);

    void send(const void* buffer, size_t size);

    int receive(void* buffer, size_t size);
  };
} // namespace udp::sockets
