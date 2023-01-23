package rmiweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class SumVectorsProcessorBean {
  public SumVectorsProcessorBean() {
  }

  public String processSumVectors(String v1FileName, String v2FileName, String sumFileName) {
    try {
      int[] v1 = readVectorFromFile(v1FileName);
      int[] v2 = readVectorFromFile(v2FileName);

      int[] sum = null;

      Registry registry = LocateRegistry.getRegistry("localhost");
      Compute comp = (Compute) registry.lookup("Compute");
      SumVectors task = new SumVectors(v1, v2);
      sum = comp.executeTask(task);

      if (sum == null)
        return "Error";

      writeVectorToFile(sum, sumFileName);
      return "OK";
    } catch (Exception e) {
      return e.getMessage() + "<br/>" + e.getCause().toString();
    }
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
