#include <signal.h>
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main(int argc, char* argv[]) {
  if (argc < 2) {
    printf("Usage: killpg <pid>\n");
    return 1;
  }
  pid_t pid = atoi(argv[1]);
  pid_t pgrp = getpgid(pid);
  int ret = killpg(pgrp, 9);
  if (ret != 0) {
    perror("Cannot kill process group");
    return 1;
  }
  return 0;
}
