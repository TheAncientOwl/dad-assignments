package rmiweb;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISumVectors extends Remote {
  public byte[] remoteSumVectors(byte[] vectors) throws RemoteException;
}
