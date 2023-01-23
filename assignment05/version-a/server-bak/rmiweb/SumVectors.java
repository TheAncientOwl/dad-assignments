package rmiweb;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SumVectors extends UnicastRemoteObject implements ISumVectors {
  SumVectors() throws RemoteException {
    super();
  }

  public byte[] remoteSumVectors(byte[] vectors) throws RemoteException {
    System.out.println("> Summing vectors");
    IntBuffer vectorsBuffer = ByteBuffer.wrap(vectors).asIntBuffer();

    int length = vectorsBuffer.get(0);
    ByteBuffer sumBuffer = ByteBuffer.allocate(length * 4);
    IntBuffer sum = sumBuffer.asIntBuffer();

    for (int i = 0; i < length; i++) {
      int x1 = vectorsBuffer.get(1 + i);
      int x2 = vectorsBuffer.get(1 + length + i);
      sum.put(i, x1 + x2);
    }

    return sumBuffer.array();
  }
}
