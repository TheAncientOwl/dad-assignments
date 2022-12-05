/** @Task
 * 3.2 Create a C program under Linux for adding to two arrays of float using POSIX threads
 *  - https://www.cs.cmu.edu/afs/cs/academic/class/15492-f07/www/pthreads.html
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "arrays.h"
#include "../utils/utils.h"

const int ARRAY_SIZE = 10000000;
const int THREADS_COUNT = 8;
const int CHUNK_SIZE = ARRAY_SIZE / THREADS_COUNT;

struct Arrays {
  float* arr1;
  float* arr2;
  float* arrOut;
};

struct AddArraysArgs {
  struct Arrays* arrays;
  int startIdx;
  int stopIdx;
};

// ----------------------------------------------------------------------------
void* addArrays(void* arguments) {
  struct AddArraysArgs* args = arguments;
  struct Arrays* arrays = args->arrays;

  for (int i = args->startIdx; i < args->stopIdx; i++)
    arrays->arrOut[i] = arrays->arr1[i] + arrays->arr2[i];
}
// ----------------------------------------------------------------------------
void singleThreadAddArrays(void* arguments) {
  struct Arrays* arrays = arguments;

  struct AddArraysArgs args;
  args.arrays = arrays;
  args.startIdx = 0;
  args.stopIdx = ARRAY_SIZE - 1;

  addArrays(&args);
}

// ----------------------------------------------------------------------------
void multiThreadAddArrays(void* arguments) {
  struct Arrays* arrays = arguments;

  pthread_t threads[THREADS_COUNT];
  struct AddArraysArgs allArgs[THREADS_COUNT];

  for (int it = 0; it < THREADS_COUNT; it++) {
    struct AddArraysArgs* args = &allArgs[it];
    args->arrays = arrays;
    args->startIdx = it * CHUNK_SIZE;
    args->stopIdx = (it + 1) * CHUNK_SIZE - 1;

    pthread_create(&threads[it], NULL, addArrays, (void*) args);
  }

  for (int it = 0; it < THREADS_COUNT; it++)
    pthread_join(threads[it], NULL);
}

// ----------------------------------------------------------------------------
int main() {
  printf(ANSI_COLOR_YELLOW "> 3.2. Add arrays -> c pthread. (%d elements, %d threads)\n\n", ARRAY_SIZE, THREADS_COUNT);

  struct Arrays arrays;
  arrays.arr1 = newArray(ARRAY_SIZE, 2.2f);
  arrays.arr2 = newArray(ARRAY_SIZE, 3.3f);
  arrays.arrOut = newArray(ARRAY_SIZE, 0.0f);

  runTimer("one_thread", ANSI_COLOR_BLUE, singleThreadAddArrays, &arrays);

  fillArray(arrays.arrOut, ARRAY_SIZE, 0.0f);

  runTimer("multi_thread", ANSI_COLOR_GREEN, multiThreadAddArrays, &arrays);

  printResultArray(arrays.arrOut, 10);

  free(arrays.arr1);
  free(arrays.arr2);
  free(arrays.arrOut);

  return EXIT_SUCCESS;
}
