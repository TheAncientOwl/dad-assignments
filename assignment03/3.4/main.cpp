/** @Task
 * 3.4 Create a C/C++ program under Linux for adding to two arrays of float using socket programming
 *  - https://gist.github.com/saxbophone/f770e86ceff9d488396c0c32d47b757e
 *  - https://www.geeksforgeeks.org/udp-server-client-implementation-c/
*/

#include "stdio.h"
#include <iostream>
#include <thread>

#include "udp/addarrays/client.hpp"
#include "udp/addarrays/server.hpp"

#include "../utils/ANSIColors.h"

const int ARRAY_SIZE = 1000;
const uint16_t PORT = 8090;

int main() {
  printf(ANSI_COLOR_YELLOW "> 3.4. Add arrays -> udp socket programming. (%d elements)\n" ANSI_COLOR_RESET, ARRAY_SIZE);

  std::thread serverThread([]() {
    udp::addarrays::runServer(ARRAY_SIZE, PORT);
    });

  std::this_thread::sleep_for(std::chrono::seconds(1));

  std::thread clientThread([]() {
    udp::addarrays::runClient(ARRAY_SIZE, PORT);
    exit(EXIT_SUCCESS);
    });

  clientThread.join();
  serverThread.join();

  return 0;
}
