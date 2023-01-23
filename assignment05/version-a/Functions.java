import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Functions {
  public static void main(String[] args) throws FileNotFoundException, IOException, RemoteException {
    int[] arr1 = readVectorFromFile("arr1.txt");
    int[] arr2 = readVectorFromFile("arr2.txt");

    writeVectorToFile(remoteSumVectors(mergeVectors(arr1, arr2)), "test.txt");
  }

  public static byte[] remoteSumVectors(byte[] mergedVectors) throws RemoteException {
    IntBuffer merged = ByteBuffer.wrap(mergedVectors).asIntBuffer();

    int length = merged.capacity() / 2;
    ByteBuffer sumBuffer = ByteBuffer.allocate(length * 4);
    IntBuffer sum = sumBuffer.asIntBuffer();

    for (int i = 0; i < length; i++) {
      int x1 = merged.get(i);
      int x2 = merged.get(i + length);
      sum.put(i, x1 + x2);
    }

    return sumBuffer.array();
  }

  private static int[] readVectorFromFile(String fileName) throws FileNotFoundException {
    Scanner scanner = new Scanner(new File(fileName));

    int[] arr = new int[scanner.nextInt()];
    for (int i = 0; i < arr.length; i++)
      arr[i] = scanner.nextInt();

    scanner.close();
    return arr;
  }

  private static byte[] mergeVectors(int[] v1, int[] v2) {
    ByteBuffer byteBuffer = ByteBuffer.allocate((v1.length + v2.length) * 4);
    IntBuffer merged = byteBuffer.asIntBuffer();

    merged.put(v1);
    merged.put(v2);

    return byteBuffer.array();
  }

  private static void writeVectorToFile(byte[] vector, String fileName) throws IOException {
    Writer writer = new FileWriter(new File(fileName));

    IntBuffer buffer = ByteBuffer.wrap(vector).asIntBuffer();
    for (int i = 0; i < buffer.capacity(); i++)
      writer.write("" + buffer.get(i) + "\n");

    writer.close();
  }
}
