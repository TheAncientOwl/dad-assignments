#pragma once

#include <stdio.h>
#include <sys/time.h>

long long currentTimeMillis() {
  struct timeval t;
  gettimeofday(&t, NULL);

  return  t.tv_sec * 1000LL + t.tv_usec / 1000;
}

typedef void(*Callable)(void* args);

void runTimer(const char* tag, const char* ansiColor, Callable callable, void* args) {
  if (ansiColor != NULL) printf("%s", ansiColor);
  if (tag != NULL) printf("<%s>\n", tag);

  long long startTime = currentTimeMillis();

  callable(args);

  if (ansiColor != NULL) printf("%s", ansiColor);
  printf("* Elapsed time: %lldms\n", currentTimeMillis() - startTime);
  if (tag != NULL) printf("</%s>\n\n", tag); else printf("\n");
}
