package server.arrays;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * m_Source contains both arrays to be added;
 * arr1: 1 1 1 1 1
 * arr2: 2 2 2 2 2
 * m_Source: 1 1 1 1 1 2 2 2 2 2
 * 
 * m_Destination: 3 3 3 3 3
 */
public class MultiThreadArraySum {
  public static final int WORKERS_COUNT = 10;

  private final ByteBuffer m_Source;
  private final ByteBuffer m_Destination;

  public MultiThreadArraySum(ByteBuffer source, ByteBuffer destination) {
    m_Source = source;
    m_Destination = destination;
  }

  public void compute() {
    ExecutorService workers = Executors.newFixedThreadPool(WORKERS_COUNT);

    final int CHUNK_SIZE = (m_Source.asFloatBuffer().limit() / 2) / WORKERS_COUNT;

    for (int it = 0; it < WORKERS_COUNT; it++) {
      int startIdx = it * CHUNK_SIZE;
      int stopIdx = (it + 1) * CHUNK_SIZE - 1;

      workers.execute(new ArraySumThread(m_Source, m_Destination, startIdx, stopIdx));
    }

    workers.shutdown();
    try {
      workers.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
