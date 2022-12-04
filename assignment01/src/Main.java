import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import udp.UDPMarkSenderThread;
import udp.UDPServerThread;

public class Main {
  private static final String PURPLE = "\033[0;35m";

  public static final int SERVER_PORT = 1475;
  public static final String ADDRESS_NAME = "127.0.0.1";

  public static void main(String[] args) {
    // create & start server
    UDPServerThread serverThread = new UDPServerThread(SERVER_PORT);
    serverThread.start();

    // read marks & create mark threads
    int[] marks = readMarks();
    UDPMarkSenderThread[] markSenderThreads = new UDPMarkSenderThread[marks.length];
    for (int i = 0; i < marks.length; i++)
      markSenderThreads[i] = new UDPMarkSenderThread(marks[i], ADDRESS_NAME, SERVER_PORT);

    // start mark threads
    for (UDPMarkSenderThread thread : markSenderThreads)
      thread.start();

    // join mark threads
    try {
      for (UDPMarkSenderThread thread : markSenderThreads)
        thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // get best mark
    try {
      DatagramSocket socket = new DatagramSocket();

      // send getmax
      byte[] buffReq = "getmax".getBytes();
      InetAddress address = InetAddress.getByName(ADDRESS_NAME);
      DatagramPacket reqPacket = new DatagramPacket(buffReq, buffReq.length, address, SERVER_PORT);
      socket.send(reqPacket);

      // get max
      byte[] buffResp = new byte[10];
      DatagramPacket packet = new DatagramPacket(buffResp, buffResp.length);
      socket.receive(packet);

      socket.close();

      System.out.println(PURPLE + ">> Best mark: " + Integer.parseInt(new String(buffResp).trim()));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private static int[] readMarks() {
    int[] marks = null;

    try {
      File marksFile = new File("marks.txt");
      Scanner scanner = new Scanner(marksFile);

      int marksCount = Integer.parseInt(scanner.nextLine());
      marks = new int[marksCount];

      for (int i = 0; i < marksCount; i++) {
        marks[i] = Integer.parseInt(scanner.nextLine());
      }

      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return marks;
  }
}
