import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Functions {
  public static void main(String[] args) throws FileNotFoundException, IOException, RemoteException {
    int[] arr1 = readVectorFromFile("arr1.txt");
    int[] arr2 = readVectorFromFile("arr2.txt");

    writeVectorToFile(remoteSumVectors(arr1, arr2), "test.txt");
  }

  public static int[] remoteSumVectors(int[] v1, int[] v2) throws RemoteException {
    int[] sum = new int[v1.length];

    for (int i = 0; i < sum.length; i++)
      sum[i] = v1[i] + v2[i];

    return sum;
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
