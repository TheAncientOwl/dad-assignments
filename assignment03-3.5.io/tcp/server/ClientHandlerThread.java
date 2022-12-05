package tcp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import arrays.MultiThreadArraySum;
import tcp.arrays.TcpArray;
import tcp.arrays.TcpArrayPair;

public class ClientHandlerThread extends Thread {
  public static final int ARRAY_SUM_THREADS_COUNT = 4;
  private Socket m_Socket;

  public ClientHandlerThread(Socket socket) {
    m_Socket = socket;
  }

  @Override
  public void run() {
    try {
      // read arrays
      InputStream inputStream = m_Socket.getInputStream();
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

      TcpArrayPair arrays = (TcpArrayPair) objectInputStream.readObject();

      // compute sum
      MultiThreadArraySum arraySum = new MultiThreadArraySum(arrays.getArr1(), arrays.getArr2(),
          ARRAY_SUM_THREADS_COUNT);
      TcpArray result = new TcpArray(arraySum.compute());

      // send sum
      OutputStream outputStream = m_Socket.getOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
      objectOutputStream.writeObject(result);

      m_Socket.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
