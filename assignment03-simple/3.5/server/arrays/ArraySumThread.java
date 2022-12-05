package server.arrays;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * m_Source contains both arrays to be added;
 * arr1: 1 1 1 1 1
 * arr2: 2 2 2 2 2
 * m_Source: 1 1 1 1 1 2 2 2 2 2
 * 
 * m_Destination: 3 3 3 3 3
 */
public class ArraySumThread implements Runnable {
  private final FloatBuffer m_Source;
  private final FloatBuffer m_Destination;

  private final int m_Start;
  private final int m_End;

  public ArraySumThread(ByteBuffer source, ByteBuffer destination, int start, int end) {
    m_Source = source.asFloatBuffer();
    m_Destination = destination.asFloatBuffer();

    m_Start = start;
    m_End = end;
  }

  @Override
  public void run() {
    final int MID = m_Source.limit() / 2;
    for (int it = m_Start; it <= m_End; it++) {
      m_Destination.put(it, m_Source.get(it) + m_Source.get(it + MID));
    }
  }
}
