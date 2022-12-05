#include "SocketBase.hpp"

#include <unistd.h> 
#include <string.h>

namespace udp::sockets {
  SocketBase::SocketBase(uint16_t port) {
    memset(&m_Address, 0, sizeof(m_Address));

    m_Address.sin_family = AF_INET;
    m_Address.sin_port = htons(port);
    m_Address.sin_addr.s_addr = INADDR_ANY;
  }

  SocketBase::~SocketBase() {
    close();
  }

  void SocketBase::create() {
    m_FileDescriptor = socket(AF_INET, SOCK_DGRAM, 0);

    if (m_FileDescriptor < 0)
      throw "> Socket creation failed...";
  }

  void SocketBase::close() {
    ::close(m_FileDescriptor);
  }

  void SocketBase::_send(const sockaddr* address, const void* buffer, size_t size) {
    sendto(m_FileDescriptor, buffer, size, MSG_CONFIRM, address, sizeof(sockaddr));
  }

  int SocketBase::_receive(sockaddr* address, void* buffer, size_t size) {
    uint len = sizeof(sockaddr);

    return recvfrom(m_FileDescriptor, buffer, size, MSG_WAITALL, address, &len);
  }
} // namespace udp::socket
