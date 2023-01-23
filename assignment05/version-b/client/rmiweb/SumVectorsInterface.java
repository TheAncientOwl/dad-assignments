package rmiweb;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SumVectorsInterface extends Remote {
  public int[] remoteSumVectors(int[] v1, int[] v2) throws RemoteException;
}
