package arrays;

public class ArraySumThread extends Thread {
  private float[] m_Arr1;
  private float[] m_Arr2;
  private float[] m_ArrOut;

  private int m_Start;
  private int m_End;

  public ArraySumThread(float[] arr1, float[] arr2, float[] arrOut, int start, int end) {
    m_Arr1 = arr1;
    m_Arr2 = arr2;
    m_ArrOut = arrOut;
    m_Start = start;
    m_End = end;
  }

  @Override
  public void run() {
    for (int it = m_Start; it <= m_End; it++)
      m_ArrOut[it] = m_Arr1[it] + m_Arr2[it];
  }
}
