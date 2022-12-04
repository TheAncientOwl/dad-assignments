package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPMarkSenderThread extends Thread {
  private static final String GREEN = "\033[0;32m";

  private int m_Mark;
  private String m_AddressName;
  private int m_ServerPort;

  public UDPMarkSenderThread(int mark, String addressName, int serverPort) {
    m_Mark = mark;
    m_AddressName = addressName;
    m_ServerPort = serverPort;
  }

  @Override
  public void run() {
    try {
      DatagramSocket socket = new DatagramSocket();
      InetAddress address = InetAddress.getByName(m_AddressName);
      byte[] markBytes = ("" + m_Mark).getBytes();
      DatagramPacket packet = new DatagramPacket(markBytes, markBytes.length, address, m_ServerPort);

      socket.send(packet);
      System.out.println(GREEN + "> Sent " + m_Mark + " to server.");

      socket.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
