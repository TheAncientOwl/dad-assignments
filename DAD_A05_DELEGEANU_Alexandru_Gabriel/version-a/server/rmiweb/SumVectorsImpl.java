package rmiweb;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SumVectorsImpl extends UnicastRemoteObject implements SumVectorsInterface {
  SumVectorsImpl() throws RemoteException {
    super();
  }

  @Override
  public int[] remoteSumVectors(int[] v1, int[] v2) throws RemoteException {
    int[] sum = new int[v1.length];

    for (int i = 0; i < sum.length; i++)
      sum[i] = v1[i] + v2[i];

    return sum;
  }

}
