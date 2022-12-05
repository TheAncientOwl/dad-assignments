/** @Task
 * 3.1 Create a C program under Linux for adding to two arrays of float using IPC multi-process techniques - e.g. fork
 *  - https://www.geeksforgeeks.org/fork-system-call/
 *  - https://www.includehelp.com/c-programs/c-fork-function-linux-example.aspx
 *  - https://stackoverflow.com/questions/15102328/how-does-fork-work
*/

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>

#include "sharedArrays.h"
#include "../utils/utils.h"

const int ARRAY_SIZE = 10000000;
const int PROCESSES_COUNT = 8;
const int CHUNK_SIZE = ARRAY_SIZE / PROCESSES_COUNT;

struct Arrays {
  float* arr1;
  float* arr2;
  float* arrOut;
};

// ----------------------------------------------------------------------------
void addArrays(struct Arrays* arrays, int startIdx, int stopIdx) {
  for (int i = startIdx; i <= stopIdx; i++)
    arrays->arrOut[i] = arrays->arr1[i] + arrays->arr2[i];
}

// ----------------------------------------------------------------------------
void singleProcessAddArrays(void* arguments) {
  struct Arrays* arrays = arguments;

  addArrays(arrays, 0, ARRAY_SIZE - 1);
}

// ----------------------------------------------------------------------------
void multiProcessAddArrays(void* arguments) {
  struct Arrays* arrays = arguments;

  pid_t childPids[PROCESSES_COUNT];

  for (int it = 0; it < PROCESSES_COUNT; it++) {
    int startIdx = it * CHUNK_SIZE;
    int stopIdx = (it + 1) * CHUNK_SIZE - 1;

    childPids[it] = fork();
    if (childPids[it] == 0) {
      printf(ANSI_COLOR_RED "> Forked Child (PID: %d) ~ add(%d -> %d).\n" ANSI_COLOR_RESET, getpid(), startIdx, stopIdx);

      addArrays(arrays, startIdx, stopIdx);

      printf(ANSI_COLOR_GREEN "> Finished Child (PID: %d)\n" ANSI_COLOR_RESET, getpid());

      exit(EXIT_SUCCESS);
    }
  }

  for (int it = 0; it < PROCESSES_COUNT; it++) {
    printf(ANSI_COLOR_BLUE "> Waiting Child PID: %d\n" ANSI_COLOR_RESET, childPids[it]);
    int status;
    waitpid(childPids[it], &status, WUNTRACED | WCONTINUED);
  }
}

// ----------------------------------------------------------------------------
int main() {
  printf(ANSI_COLOR_YELLOW "> 3.1. Add arrays -> c fork. (%d elements, %d processes)\n\n", ARRAY_SIZE, PROCESSES_COUNT);

  struct Arrays arrays;
  arrays.arr1 = newSharedArray(ARRAY_SIZE, 2.2f);
  arrays.arr2 = newSharedArray(ARRAY_SIZE, 3.3f);
  arrays.arrOut = newSharedArray(ARRAY_SIZE, 0.0f);

  runTimer("one_process", ANSI_COLOR_BLUE, singleProcessAddArrays, &arrays);

  fillArray(arrays.arrOut, ARRAY_SIZE, 0.0f);

  runTimer("multi_process", ANSI_COLOR_GREEN, multiProcessAddArrays, &arrays);

  printResultArray(arrays.arrOut, 10);

  unmapSharedArray(arrays.arr1, ARRAY_SIZE);
  unmapSharedArray(arrays.arr2, ARRAY_SIZE);
  unmapSharedArray(arrays.arrOut, ARRAY_SIZE);

  return EXIT_SUCCESS;
}
