/** @Task
 * 3.3 Create a C++ '11/'17 program under Linux for adding to two arrays of float using objects from class thread
 *  - https://stackoverflow.com/questions/266168/simple-example-of-threading-in-c
 *  - https://www.geeksforgeeks.org/multithreading-in-cpp/
*/

#include <iostream>
#include <vector>
#include <thread>

#include "../utils/utils.h"

using array = std::vector<float>;

const int ARRAY_SIZE = 10000000;
const int THREADS_COUNT = 8;
const int CHUNK_SIZE = ARRAY_SIZE / THREADS_COUNT;

struct Arrays {
  array arr1;
  array arr2;
  array arrOut;

  Arrays(std::size_t size, float value1, float value2)
    : arr1(size, value1), arr2(size, value2), arrOut(size, 0.0f) {}
};

// ----------------------------------------------------------------------------
void addArrays(const array& arr1, const array& arr2, array& arrOut, int startIdx, int stopIdx) {
  for (int i = startIdx; i <= stopIdx; i++)
    arrOut[i] = arr1[i] + arr2[i];
}

// ----------------------------------------------------------------------------
void singleThreadAddArrays(void* arguments) {
  Arrays* arrays = (Arrays*) arguments;

  addArrays(arrays->arr1, arrays->arr2, arrays->arrOut, 0, ARRAY_SIZE - 1);
}

// ----------------------------------------------------------------------------
void multiThreadAddArrays(void* arguments) {
  Arrays* arrays = (Arrays*) arguments;

  std::vector<std::thread> threads;
  threads.reserve(THREADS_COUNT);

  for (int it = 0; it < THREADS_COUNT; it++) {
    int startIdx = it * CHUNK_SIZE;
    int stopIdx = (it + 1) * CHUNK_SIZE - 1;

    threads.emplace_back(addArrays, std::cref(arrays->arr1), std::cref(arrays->arr2), std::ref(arrays->arrOut), startIdx, stopIdx);
  }

  for (auto& thread : threads)
    thread.join();
}

// ----------------------------------------------------------------------------
int main() {
  printf(ANSI_COLOR_YELLOW "> 3.3. Add arrays -> c++ std::thread. (%d elements, %d threads)\n", ARRAY_SIZE, THREADS_COUNT);

  Arrays arrays(ARRAY_SIZE, 2.2f, 3.3f);

  runTimer("one_thread", ANSI_COLOR_BLUE, singleThreadAddArrays, &arrays);

  std::fill(arrays.arrOut.begin(), arrays.arrOut.end(), 0.0f);

  runTimer("multi_thread", ANSI_COLOR_GREEN, multiThreadAddArrays, &arrays);

  printResultArray(arrays.arrOut.data(), 10);

  return 0;
}
