package arrays;

public class ArrayView {
  private float[] m_Arr;
  private int m_Start;
  private int m_Length;

  public ArrayView(float[] arr, int start, int length) {
    m_Arr = arr;
    m_Start = start;
    m_Length = length;
  }

  public void copy(float[] arr) {
    if (m_Length < arr.length)
      throw new IllegalArgumentException("Not enough space to copy entire array");

    for (int i = 0; i < arr.length; i++)
      m_Arr[m_Start + i] = arr[i];
  }

  public float[] makeDataCopy() {
    float[] arr = new float[m_Length];

    for (int i = 0; i < arr.length; i++)
      arr[i] = m_Arr[i + m_Start];

    return arr;
  }
}
