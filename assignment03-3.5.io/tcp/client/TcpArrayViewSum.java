package tcp.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import arrays.ArrayView;
import tcp.LogTypes;
import tcp.Logger;
import tcp.arrays.TcpArray;
import tcp.arrays.TcpArrayPair;

public class TcpArrayViewSum implements Runnable {
  private Socket m_Socket;
  private TcpArrayPair m_Arrays;
  private ArrayView m_Result;
  private int m_Port;

  public TcpArrayViewSum(String host, int port, ArrayView arr1, ArrayView arr2, ArrayView result)
      throws IOException {
    Logger.log(LogTypes.CLIENT, port, "Connecting...");
    m_Socket = new Socket(host, port);
    m_Port = port;

    m_Arrays = new TcpArrayPair(arr1.makeDataCopy(), arr2.makeDataCopy());

    m_Result = result;
  }

  @Override
  public void run() {
    try {
      OutputStream outputStream = m_Socket.getOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
      Logger.log(LogTypes.CLIENT, m_Port, "Sending arrays...");
      objectOutputStream.writeObject(m_Arrays);

      InputStream inputStream = m_Socket.getInputStream();
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
      Logger.log(LogTypes.CLIENT, m_Port, Logger.PURPLE + "Reciving result...");
      TcpArray result = (TcpArray) objectInputStream.readObject();

      m_Result.copy(result.getData());

      m_Socket.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
