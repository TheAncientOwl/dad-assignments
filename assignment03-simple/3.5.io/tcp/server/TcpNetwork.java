package tcp.server;

import java.io.IOException;

public class TcpNetwork implements Runnable {
  private int[] m_Ports;

  public TcpNetwork(int[] ports) {
    m_Ports = ports;
  }

  @Override
  public void run() {
    Thread[] serverThreads = new Thread[m_Ports.length];

    try {
      for (int i = 0; i < serverThreads.length; i++) {
        serverThreads[i] = new Thread(new TcpServer(m_Ports[i]));
      }

      for (Thread thread : serverThreads)
        thread.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
