package arrays;

public class MultiThreadArraySum {
  private float[] m_Arr1;
  private float[] m_Arr2;
  private int m_ThreadsCount;

  public MultiThreadArraySum(float[] arr1, float[] arr2, int threadsCount) {
    m_Arr1 = arr1;
    m_Arr2 = arr2;
    m_ThreadsCount = threadsCount;
  }

  public float[] compute() {
    float[] result = new float[m_Arr1.length];
    for (int i = 0; i < result.length; i++)
      result[i] = 0;

    Thread[] threads = new Thread[m_ThreadsCount];

    int chunkSize = m_Arr1.length / m_ThreadsCount;
    for (int it = 0; it < m_ThreadsCount; it++) {
      int startIdx = it * chunkSize;
      int stopIdx = (it + 1) * chunkSize - 1;
      if (it == m_ThreadsCount - 1 && chunkSize * m_ThreadsCount != m_Arr1.length)
        stopIdx = m_Arr1.length - 1;

      threads[it] = new Thread(
          new ArraySumThread(m_Arr1, m_Arr2, result, startIdx, stopIdx));
    }

    for (Thread thread : threads)
      thread.start();

    try {
      for (Thread thread : threads)
        thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return result;
  }
}
