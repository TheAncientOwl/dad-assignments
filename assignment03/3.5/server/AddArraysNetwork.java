package server;

import java.io.IOException;

public class AddArraysNetwork implements Runnable {
  private final int[] m_Ports;
  private final int m_ArraySize;

  public AddArraysNetwork(int[] ports, int arraySize) {
    m_Ports = ports;
    m_ArraySize = arraySize;
  }

  @Override
  public void run() {
    Thread[] serverThreads = new Thread[m_Ports.length];

    for (int i = 0; i < serverThreads.length; i++)
      try {
        serverThreads[i] = new Thread(new AddArraysServer(m_Ports[i], m_ArraySize));
      } catch (IOException e) {
        e.printStackTrace();
      }

    for (Thread serverThread : serverThreads)
      serverThread.start();
  }
}
