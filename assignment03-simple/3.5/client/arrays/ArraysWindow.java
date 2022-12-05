package client.arrays;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class ArraysWindow {
  private final float[] m_Arr1;
  private final float[] m_Arr2;
  private final float[] m_ArrOut;

  private final int m_StartIdx;
  private final int m_ChunkSize;

  public ArraysWindow(int startIdx, int chunkSize, float[] arr1, float[] arr2, float[] arrOut) {
    m_Arr1 = arr1;
    m_Arr2 = arr2;
    m_ArrOut = arrOut;

    m_StartIdx = startIdx;
    m_ChunkSize = chunkSize;
  }

  public ByteBuffer toByteBufferArr12() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(m_ChunkSize * 4 * 2);
    FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();

    floatBuffer.put(m_Arr1, m_StartIdx, m_ChunkSize);
    floatBuffer.put(m_Arr2, m_StartIdx, m_ChunkSize);

    return byteBuffer;
  }

  public void copyResultBuffer(final FloatBuffer sumBuffer) {
    for (int i = 0; i < m_ChunkSize; i++)
      m_ArrOut[i + m_StartIdx] = sumBuffer.get(i);
  }

  public ByteBuffer allocateResultBuffer() {
    ByteBuffer resultBuffer = ByteBuffer.allocate(m_ChunkSize * 4);

    return resultBuffer;
  }
}
