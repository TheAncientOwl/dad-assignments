#include "ClientSocket.hpp"

namespace udp::sockets {
  ClientSocket::ClientSocket(uint16_t port) : SocketBase(port) {}

  void ClientSocket::send(const void* buffer, size_t size) {
    _send((sockaddr*) &m_Address, buffer, size);
  }

  int ClientSocket::receive(void* buffer, size_t size) {
    return _receive((sockaddr*) &m_Address, buffer, size);
  }
} // namespace udp::sockets
