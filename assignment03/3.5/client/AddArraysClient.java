package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import client.arrays.ArraysWindow;
import logger.LogTypes;
import logger.Logger;

public class AddArraysClient implements Runnable {
  private final int[] PORTS;
  private final int CHUNK_SIZE;

  private final int CHUNKS_COUNT;
  private final int CHUNKS_PER_SERVER;

  private final float[] m_Arr1;
  private final float[] m_Arr2;
  private final float[] m_ArrOut;

  public AddArraysClient(int[] ports, int serverArraySize, String arr1FileName, String arr2FileName) {
    m_Arr1 = readArray(arr1FileName);
    m_Arr2 = readArray(arr2FileName);

    m_ArrOut = new float[m_Arr1.length];
    for (int i = 0; i < m_ArrOut.length; i++)
      m_ArrOut[i] = 0;

    PORTS = ports;
    CHUNK_SIZE = serverArraySize;

    CHUNKS_COUNT = m_Arr1.length / CHUNK_SIZE;
    CHUNKS_PER_SERVER = CHUNKS_COUNT / PORTS.length;

    if (CHUNKS_PER_SERVER == 0) {
      Logger.log(LogTypes.CLIENT, -1, null,
          "\n>Chunks count = 0. \n>The array cannot be split between servers, too few elements. \n>Try to:\n  * add more elements; \n  * decrease servers count; \n  * decrease server array size. \nThe program will exit...");
      System.exit(0);
    }
  }

  @Override
  public void run() {
    final int MAX_PORT_INDEX = PORTS.length - 1;

    Thread[] threads = new Thread[CHUNKS_COUNT];

    for (int it = 0, portIndex = -1; it < threads.length; it++) {
      if (it % CHUNKS_PER_SERVER == 0)
        portIndex = Math.min(portIndex + 1, MAX_PORT_INDEX);

      int startIdx = it * CHUNK_SIZE;

      ArraysWindow window = new ArraysWindow(startIdx, CHUNK_SIZE, m_Arr1, m_Arr2, m_ArrOut);

      try {
        threads[it] = new Thread(new AddArraysWindowClient(PORTS[portIndex], window));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    for (var thread : threads)
      thread.start();

    for (var thread : threads)
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    for (int i = 0; i < 10; i++)
      System.out.print("" + m_ArrOut[i] + " | ");
    System.out.println("\n");
    System.exit(0);
  }

  private static float[] readArray(String fileName) {
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
