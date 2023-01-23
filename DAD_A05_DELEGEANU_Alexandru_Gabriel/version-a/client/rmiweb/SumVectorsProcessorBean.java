package rmiweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SumVectorsProcessorBean {
  private static int currentServer = 0;
  private static String[] rmiServers = new String[] { "127.0.0.1", "127.0.0.1", "127.0.0.1" };

  public SumVectorsProcessorBean() {
  }

  public static void setRMIs(String[] rmis) {
    for (int i = 0; i < 3; i++)
      rmiServers[i] = rmis[i];
  }

  public int processSumVectors(String v1FileName, String v2FileName, String sumFileName) {
    int result = 1;

    String url = "rmi://";
    if (currentServer == 0 || currentServer == 1) {
      url += rmiServers[currentServer];
      currentServer++;
    } else if (currentServer == 2) {
      url += rmiServers[currentServer];
      currentServer = 0;
    }
    url += ":1099/SUM-VECTORS";

    try {
      int[] v1 = readVectorFromFile(v1FileName);
      int[] v2 = readVectorFromFile(v2FileName);

      int[] sum = null;
      SumVectorsInterface remoteObject = (SumVectorsInterface) Naming.lookup(url);
      sum = remoteObject.remoteSumVectors(v1, v2);

      if (sum == null)
        return 1;

      writeVectorToFile(sum, sumFileName);
      result = 0;
    } catch (RemoteException exc) {
      System.out.println("Error in lookup: " + exc.toString());
    } catch (MalformedURLException exc) {
      System.out.println("Malformed URL: " + exc.toString());
    } catch (NotBoundException exc) {
      System.out.println("NotBound: " + exc.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }

  private static int[] readVectorFromFile(String fileName) throws FileNotFoundException {
    Scanner scanner = new Scanner(new File(fileName));

    int[] arr = new int[scanner.nextInt()];
    for (int i = 0; i < arr.length; i++)
      arr[i] = scanner.nextInt();

    scanner.close();
    return arr;
  }

  private static void writeVectorToFile(int[] vector, String fileName) throws IOException {
    Writer writer = new FileWriter(new File(fileName));

    for (int i = 0; i < vector.length; i++)
      writer.write("" + vector[i] + "\n");

    writer.close();
  }
}
