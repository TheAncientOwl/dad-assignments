#include "server.hpp"

#include <iostream>
#include <vector>

#include "../sockets/ServerSocket.hpp"

#include "../../../utils/ANSIColors.h"

namespace udp::addarrays {
  using array = std::vector<float>;

  void serverLog(const std::string& message) {
    static const std::string TAG = ANSI_COLOR_RESET "["  ANSI_COLOR_GREEN "Server" ANSI_COLOR_RESET "] "  ANSI_COLOR_GREEN;
    std::cout << TAG + message + ANSI_COLOR_RESET << std::endl;
  }

  /**
   * arrays: 1 1 1 1 2 2 2 2
   * arrOut: 3 3 3 3
  */
  array addArrays(const array& arrays) {
    const std::size_t MID = arrays.size() / 2;
    array arrOut;
    arrOut.reserve(MID);

    for (int it = 0; it < MID; it++)
      arrOut.push_back(arrays[it] + arrays[it + MID]);

    return arrOut;
  }

  void runServer(const int ARRAY_SIZE, const uint16_t PORT) {
    serverLog("Starting...");

    udp::sockets::ServerSocket serverSocket(PORT);

    try {
      serverSocket.create();
      serverSocket.bind();

      bool running = true;

      while (running) {
        array arrIn(2 * ARRAY_SIZE, 0.0f);

        serverLog("Waiting for arrays...");
        serverSocket.receive(arrIn.data(), 2 * ARRAY_SIZE * sizeof(float));
        serverLog("Received arrays.");

        array arrOut = addArrays(arrIn);

        serverLog("Sending result.");
        serverSocket.respond(arrOut.data(), ARRAY_SIZE * sizeof(float));
        serverLog("Result sent.");
      }
    } catch (const char* error) {
      std::cout << error << '\n';
      exit(EXIT_FAILURE);
    }
  }
} // namespace udp::addarrays
