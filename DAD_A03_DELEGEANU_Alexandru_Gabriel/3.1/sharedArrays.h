#pragma once

#include <stdio.h>
#include <sys/mman.h>

void fillArray(float* arr, size_t size, float value) {
  for (int i = 0; i < size; i++)
    arr[i] = value;
}

float* newSharedArray(size_t elementsCount, float value) {
  float* arr = (float*) mmap(NULL, elementsCount * sizeof(float), PROT_READ | PROT_WRITE, MAP_SHARED | MAP_ANONYMOUS, -1, 0);

  if (arr == MAP_FAILED) {
    printf("Mapping Failed\n");
    exit(EXIT_FAILURE);
  }

  fillArray(arr, elementsCount, value);

  return arr;
}

void unmapSharedArray(float* arr, size_t elementsCount) {
  munmap(arr, elementsCount * sizeof(float));
}
