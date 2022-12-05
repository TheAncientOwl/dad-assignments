import client.AddArraysClient;
import logger.LogTypes;
import logger.Logger;
import server.AddArraysNetwork;
import server.arrays.MultiThreadArraySum;

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

/**
 * -> 5 servers -> add arrays of 1000 elements using multithreading
 * 
 * -> client -> 100_000 elements
 * -> split elements between 5 servers
 * -> split again by 1000 elements chunk
 * -> send elements to sum (multithreading)
 */

public class Main {
  public static final String YELLOW = "\u001B[33m";

  private static final int[] PORTS = { 8080, 8081, 8082, 8083, 8084 };
  private static final int NETWORK_SERVER_ARRAY_SIZE = 1000;

  public static void main(String[] args) {
    System.out.println(YELLOW + "> 3.5. Add arrays -> java client-server (" + PORTS.length
        + " servers, each can sum " + NETWORK_SERVER_ARRAY_SIZE + " elements / request, using multithreading with "
        + MultiThreadArraySum.WORKERS_COUNT + " workers / request)");

    Thread networkThread = new Thread(new AddArraysNetwork(PORTS, NETWORK_SERVER_ARRAY_SIZE));
    Logger.log(LogTypes.INFO, -1, null, "Starting servers...");
    networkThread.start();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    AddArraysClient client = new AddArraysClient(PORTS, NETWORK_SERVER_ARRAY_SIZE, "arr1.txt", "arr2.txt");
    Logger.log(LogTypes.INFO, -1, null, "Starting client...");
    client.run();
  }
}
