Task:
  Create multiple processes for communicating one each other, through UPD multicast/broadcast packages, 
  in order to find out the best mark in the room. 

  Each process has its own mark from 1 to 10 stored in command line for launching or it is stored in a file on the disk. 

  The problem and the solutions have been explained in the lecture 01. 
  You may develop the solution for this problem in Java/Kotlin or C++ / C# / node.js / Swift / Python."

Logics:
  UDPServerThread: 
    ->  1. get marks + keep the best mark;
    ->  2. send best mark on request (getmax).

  UDPMarkSenderThread:
   -> send mark to UDPServerThread.

  Main:
    -> 1. create & start server thread;
    -> 2. read marks;
    -> 3. create, start, join sender threads;
    -> 4. print best mark;

Entry Point: Main

OS: Ubuntu 22.04.1 LTS on Windows 10 x86_64

marks.txt format:
  -> first line = number of marks
  -> each line = a mark

How to run: ./compile-and-run.sh
