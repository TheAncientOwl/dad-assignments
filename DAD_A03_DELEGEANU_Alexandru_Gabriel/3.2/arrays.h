#pragma once
#include <stdio.h>
#include <stdlib.h>

void fillArray(float* arr, size_t size, float value) {
  for (int i = 0; i < size; i++)
    arr[i] = value;
}

float* newArray(int elementsCount, float value) {
  float* arr = (float*) malloc(elementsCount * sizeof(float));

  fillArray(arr, elementsCount, value);

  return arr;
}
