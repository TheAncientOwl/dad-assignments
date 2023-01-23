package rmiweb;

import java.io.Serializable;

public class SumVectors implements Task<int[]>, Serializable {

  private static final long serialVersionUID = 227L;

  private int[] v1;
  private int[] v2;

  public SumVectors(int[] v1, int[] v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  public int[] execute() {
    int[] sum = new int[v1.length];

    for (int i = 0; i < sum.length; i++)
      sum[i] = v1[i] + v2[i];

    return sum;
  }

}
