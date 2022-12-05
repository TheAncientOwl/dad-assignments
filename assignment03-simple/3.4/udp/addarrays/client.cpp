#include "client.hpp"

#include <iostream>
#include <vector>

#include "../sockets/ClientSocket.hpp"

#include "../../../utils/ANSIColors.h"

namespace udp::addarrays {
  using array = std::vector<float>;

  void clientLog(const std::string& message) {
    static const std::string TAG = ANSI_COLOR_RESET "["  ANSI_COLOR_BLUE "Client" ANSI_COLOR_RESET "] "  ANSI_COLOR_BLUE;
    std::cout << TAG + message + ANSI_COLOR_RESET << std::endl;
  }

  void clientLogArraySum(const array& arr) {
    clientLog("Received arrays sum:");

    for (std::size_t it = 0; it < 10; it++)
      std::cout << arr[it] << " | ";
    std::cout << "..." << std::endl;
  }

  void runClient(const int ARRAY_SIZE, const uint16_t PORT) {
    clientLog("Starting...");

    array arr1(ARRAY_SIZE, 2.2f);
    array arr2(ARRAY_SIZE, 3.3f);
    array arr3(ARRAY_SIZE, 0.0f);

    udp::sockets::ClientSocket clientSocket(PORT);

    try {
      clientSocket.create();

      array mergedArray;
      mergedArray.reserve(2 * ARRAY_SIZE);
      mergedArray.insert(mergedArray.end(), arr1.begin(), arr1.end());
      mergedArray.insert(mergedArray.end(), arr2.begin(), arr2.end());

      clientLog("Sending arrays...");
      clientSocket.send(mergedArray.data(), 2 * ARRAY_SIZE * sizeof(float));
      clientLog("Arrays sent.");

      clientLog("Receiving arrays sum.");
      clientSocket.receive(arr3.data(), ARRAY_SIZE * sizeof(float));
      clientLogArraySum(arr3);

    } catch (const char* error) {
      std::cout << error << '\n';
      exit(EXIT_FAILURE);
    }
  }
} // namespace udp::addarrays
