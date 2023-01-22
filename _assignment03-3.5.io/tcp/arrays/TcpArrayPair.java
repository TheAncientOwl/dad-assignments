package tcp.arrays;

import java.io.Serializable;

public class TcpArrayPair implements Serializable {
  private static final long serialVersionUID = 1L;
  private TcpArray m_Arr1;
  private TcpArray m_Arr2;

  public TcpArrayPair(int size, float value1, float value2) {
    m_Arr1 = new TcpArray(size, value1);
    m_Arr2 = new TcpArray(size, value2);
  }

  public TcpArrayPair(float[] arr1, float[] arr2) throws IllegalArgumentException {
    if (arr1.length != arr2.length)
      throw new IllegalArgumentException("Array lengths must be equal.");

    m_Arr1 = new TcpArray(arr1);
    m_Arr2 = new TcpArray(arr2);
  }

  public float[] getArr1() {
    return m_Arr1.getData();
  }

  public float[] getArr2() {
    return m_Arr2.getData();
  }
};
