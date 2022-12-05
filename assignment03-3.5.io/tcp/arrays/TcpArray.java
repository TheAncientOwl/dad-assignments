package tcp.arrays;

import java.io.Serializable;

public class TcpArray implements Serializable {
  private static final long serialVersionUID = 1L;
  private float[] m_Data;

  public TcpArray(int size, float value) {
    m_Data = new float[size];
    for (int i = 0; i < size; i++)
      m_Data[i] = value;
  }

  public TcpArray(float[] arr) {
    m_Data = arr;
  }

  public float[] getData() {
    return m_Data;
  }
}
