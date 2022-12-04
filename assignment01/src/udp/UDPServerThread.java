package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServerThread extends Thread {
  private static final String BLUE = "\033[0;34m";

  private int m_Port;
  private int m_BestMark;

  public UDPServerThread(int port) {
    m_Port = port;
    m_BestMark = 0;
  }

  @Override
  public void run() {
    DatagramSocket socket = null;

    try {
      socket = new DatagramSocket(m_Port);

      while (true) {
        byte[] bufRecv = new byte[10];
        DatagramPacket packet = new DatagramPacket(bufRecv, bufRecv.length);
        socket.receive(packet);

        String request = new String(bufRecv).trim();
        System.out.println(BLUE + "* Got " + request + " from client.");

        handleRequest(request, socket, packet);
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public void handleRequest(String request, DatagramSocket socket, DatagramPacket packet) throws IOException {
    if ("getmax".equals(request)) {
      String response = "" + m_BestMark;
      byte[] bytes = response.getBytes();

      InetAddress address = packet.getAddress();
      int port = packet.getPort();

      DatagramPacket responsePacket = new DatagramPacket(bytes, bytes.length, address, port);
      socket.send(responsePacket);
    } else {
      int mark = Integer.parseInt(request);

      m_BestMark = Math.max(mark, m_BestMark);
    }
  }
}
