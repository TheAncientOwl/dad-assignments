#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>

/**
 * @Task
 *   Download activity with OpenMPI examples - proof of concept, in Linux Ubuntu
 * (download the Docker container / virtual machine from the current page - http://acs.ase.ro/dad),
 * in order to highlight 'broadcast', 'gather', 'scatter'
 * on addition operation of two large arrays of integers
 * - e.g. each having 100 000 000 elements. => each array elements occuping in RAM aprox. 300 - 400 MB
*/

struct World {
  int rank;
  int size;
};

struct Array {
  int* data;
  int size;
};

struct Array makeArray(int size);
void freeArray(struct Array arr);
struct Array generateArray(int delay, int size);

const int ARRAY_SIZE = 100000000;
const int REQUIRED_WORLD_SIZE = 4;
const int ROOT_NODE = 0;

void main(int argc, char** argv) {
  struct World world;

  int scatteredArraySize;
  struct Array arr1, arr2, arrSum;
  struct Array subArr1, subArr2, subArrSum;
  arr1.data = arr2.data = subArr1.data = subArr2.data = NULL;
  arr1.size = arr2.size = subArr1.size = subArr2.size = 0;

  MPI_Init(NULL, NULL);
  MPI_Comm_rank(MPI_COMM_WORLD, &world.rank);
  MPI_Comm_size(MPI_COMM_WORLD, &world.size);

  // Check world size.
  if (world.size != REQUIRED_WORLD_SIZE) {
    printf("> World must be %d!\n", REQUIRED_WORLD_SIZE);

    MPI_Finalize();

    return;
  }

  // Generate arrays.
  if (world.rank == ROOT_NODE) {
    printf("> Rank [%d]: generating arrays...\n", world.rank);
    arr1 = generateArray(0, ARRAY_SIZE);
    arr2 = generateArray(5, ARRAY_SIZE);
    printf("> Rank [%d]: generated arrays.\n", world.rank);

    scatteredArraySize = arr1.size / REQUIRED_WORLD_SIZE;
  }

  // Broadcast scattered array size & allocate memory.
  MPI_Bcast(&scatteredArraySize, 1, MPI_INT, ROOT_NODE, MPI_COMM_WORLD);
  subArr1 = makeArray(scatteredArraySize);
  subArr2 = makeArray(scatteredArraySize);

  printf("> Rank [%d]: received array size (%d).\n", world.rank, scatteredArraySize);

  // Scatter the arrays.
  MPI_Scatter(arr1.data, subArr1.size, MPI_INT, subArr1.data, subArr1.size, MPI_INT, ROOT_NODE, MPI_COMM_WORLD);
  MPI_Scatter(arr2.data, subArr2.size, MPI_INT, subArr2.data, subArr2.size, MPI_INT, ROOT_NODE, MPI_COMM_WORLD);

  printf("> Rank [%d]: sub arr 1: %d %d %d...\n", world.rank, subArr1.data[0], subArr1.data[1], subArr1.data[2]);
  printf("> Rank [%d]: sub arr 2: %d %d %d...\n", world.rank, subArr2.data[0], subArr2.data[1], subArr2.data[2]);

  // Sum scattered arrays.
  subArrSum = makeArray(subArr1.size);
  for (int i = 0; i < subArrSum.size; i++)
    subArrSum.data[i] = subArr1.data[i] + subArr2.data[i];

  // Allocate sum array & gather the sum.
  if (world.rank == ROOT_NODE)
    arrSum = makeArray(arr1.size);
  MPI_Gather(subArrSum.data, subArrSum.size, MPI_INT, arrSum.data, subArrSum.size, MPI_INT, ROOT_NODE, MPI_COMM_WORLD);

  // Display the sum
  if (world.rank == ROOT_NODE) {
    for (int i = 0; i < arrSum.size; i++) {
      if (i % 50 == 0) {
        printf("> Display next 50 items?: [%d-%d)/%d\n", i + 1, i + 50 + 1, arrSum.size);
        fflush(stdout);

        char answer[10];
        do {
          printf("y/n?: ");
          fflush(stdout);
          scanf("%s", answer);
        } while (!(answer[0] == 'n' || answer[0] == 'y'));
        if (answer[0] == 'n')
          break;
      }

      char buffer[100];
      sprintf(buffer, "%2d + %2d = %2d", arr1.data[i], arr2.data[i], arrSum.data[i]);
      printf("%16s |", buffer);
      if ((i + 1) % 5 == 0)
        printf("\n");
    }
  }

  // Cleanup.
  if (world.rank == ROOT_NODE) {
    freeArray(arr1);
    freeArray(arr2);
    freeArray(arrSum);
  }

  freeArray(subArr1);
  freeArray(subArr2);
  freeArray(subArrSum);

  // MPI End.
  MPI_Barrier(MPI_COMM_WORLD);
  MPI_Finalize();
}

struct Array makeArray(int size) {
  struct Array arr;
  arr.size = size;
  arr.data = (int*) malloc(arr.size * sizeof(int));

  return arr;
}

void freeArray(struct Array arr) {
  if (arr.data != NULL)
    free(arr.data);
}

struct Array generateArray(int delay, int size) {
  struct Array arr = makeArray(size);

  for (int i = 0; i < arr.size; i++)
    arr.data[i] = i + delay;

  return arr;
}
