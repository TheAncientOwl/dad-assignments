package tcp.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import arrays.ArrayView;

public class TcpClient implements Runnable {
  private static final String HOSTNAME = "localhost";

  private int[] m_Ports;

  private float[] m_Arr1;
  private float[] m_Arr2;
  private float[] m_ArrSum;

  public TcpClient(int[] ports) {
    m_Ports = ports;

    m_Arr1 = readArray("arr1.txt");
    m_Arr2 = readArray("arr2.txt");

    m_ArrSum = new float[m_Arr1.length];
    for (int i = 0; i < m_ArrSum.length; i++)
      m_ArrSum[i] = 0;
  }

  @Override
  public void run() {
    Thread[] clientThreads = new Thread[m_Ports.length];

    int chunkSize = m_ArrSum.length / m_Ports.length;
    for (int it = 0; it < clientThreads.length; it++) {
      int startIdx = it * chunkSize;

      ArrayView arr1 = new ArrayView(m_Arr1, startIdx, chunkSize);
      ArrayView arr2 = new ArrayView(m_Arr2, startIdx, chunkSize);
      ArrayView arrOut = new ArrayView(m_ArrSum, startIdx, chunkSize);

      try {
        clientThreads[it] = new Thread(new TcpArrayViewSum(HOSTNAME, m_Ports[it], arr1, arr2, arrOut));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    for (Thread thread : clientThreads)
      thread.start();

    try {
      for (Thread thread : clientThreads)
        thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println();
    for (int i = 0; i < 30; i++)
      System.out.print("" + m_ArrSum[i] + " ");
    System.out.println();
    System.out.println("^C to exit...");
  }

  private float[] readArray(String fileName) {
    float[] result = null;

    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(fileName));
      int elementsCount = Integer.parseInt(reader.readLine());

      result = new float[elementsCount];

      for (int i = 0; i < elementsCount; i++)
        result[i] = Float.parseFloat(reader.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

}
