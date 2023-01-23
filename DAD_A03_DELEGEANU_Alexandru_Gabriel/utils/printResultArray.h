#pragma once

#include <stdio.h>
#include "./ANSIColors.h"

void printResultArray(const float* arr, int size) {
  printf(ANSI_COLOR_RED "> Result: ");
  for (int i = 0; i < 10; i++)
    printf("%.1f | ", arr[i]);
  printf("...\n\n");
}
