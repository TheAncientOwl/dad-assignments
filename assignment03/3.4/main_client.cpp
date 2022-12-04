#include "udp/addarrays/client.hpp"

const int ARRAY_SIZE = 100;
const uint16_t PORT = 8080;

int main() {
  udp::addarrays::runClient(ARRAY_SIZE, PORT);
}
