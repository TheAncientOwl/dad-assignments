
/**
 * @Task
 *       >> 3.5 Build client-server programs in Java.
 * 
 *       >> The client read the arrays from files and send them using Sockets
 *       and it transmits TCP packets.
 * 
 *       >> The servers receives the array and process them using
 *       multi-threading.
 * 
 *       >> The results are sent back asynchronous by the server to the clients
 *       by using Java NIO API.
 * 
 *       >> Please see the discussion thread:
 *       https://stackoverflow.com/questions/56747881/how-to-use-multiple-cores-in-the-java-non-blocking-i-o-nio-api"
 */

import java.io.IOException;

import tcp.LogTypes;
import tcp.Logger;
import tcp.client.TcpClient;
import tcp.server.ClientHandlerThread;
import tcp.server.TcpNetwork;

public class Main {
  private static final int[] PORTS = { 8080, 8081, 8082, 8083 };

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    System.out.println(Logger.YELLOW + "> 3.5. Add arrays -> java client-server (" + PORTS.length
        + " servers, each server " + ClientHandlerThread.ARRAY_SUM_THREADS_COUNT + " threads for array sum)");

    Thread networkThread = new Thread(new TcpNetwork(PORTS));
    Logger.log(LogTypes.INFO, -1, "Starting servers...");
    networkThread.start();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Thread clientThread = new Thread(new TcpClient(PORTS));
    Logger.log(LogTypes.INFO, -1, "Starting client...");
    clientThread.start();

    try {
      clientThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
