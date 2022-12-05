#include "ServerSocket.hpp"

#include <string.h>

namespace udp::sockets {
  ServerSocket::ServerSocket(uint16_t port) : SocketBase(port) {
    memset(&m_ClientAddress, 0, sizeof(m_ClientAddress));
  }

  void ServerSocket::bind() {
    if (::bind(m_FileDescriptor, (const sockaddr*) &m_Address, sizeof(m_Address)) < 0)
      throw "> Bind failed...";
  }

  int ServerSocket::receive(void* buffer, size_t size) {
    return _receive((sockaddr*) &m_ClientAddress, buffer, size);
  }

  void ServerSocket::respond(const void* buffer, size_t size) {
    _send((sockaddr*) &m_ClientAddress, buffer, size);
  }
} // namespace udp::sockets
