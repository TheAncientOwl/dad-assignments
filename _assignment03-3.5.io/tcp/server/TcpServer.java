package tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tcp.LogTypes;
import tcp.Logger;

public class TcpServer implements Runnable {
  private ServerSocket m_Socket;
  private boolean m_Listening;
  private int m_Port;

  public TcpServer(int port) throws IOException {
    m_Port = port;
    m_Socket = new ServerSocket(port);
    Logger.log(LogTypes.SERVER, m_Port, "Started.");
    m_Listening = true;
  }

  @Override
  public void run() {
    while (m_Listening) {
      try {
        Socket clientSocket = m_Socket.accept();
        Logger.log(LogTypes.SERVER, m_Port, "Accepted connection.");

        ClientHandlerThread clientThread = new ClientHandlerThread(clientSocket);
        clientThread.start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
