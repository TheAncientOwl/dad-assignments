package rmiweb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.rmi.Naming;
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

  public int sumVectors(String vector1FileName, String vector2FileName, String vectorSumFileName) {
    try {
      int[] vector1 = readVectorFromFile(vector1FileName);
      int[] vector2 = readVectorFromFile(vector2FileName);

      ISumVectors remoteObject = (ISumVectors) Naming.lookup(getNextRmiUrl());

      byte[] vectorsBuffRes = remoteObject.remoteSumVectors(mergeVectors(vector1, vector2));

      if (vectorsBuffRes != null) {
        writeVectorToFile(vectorsBuffRes, vectorSumFileName);
        return 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return 1;
  }

  private String getNextRmiUrl() {
    String url = "rmi://";
    if (currentServer < 2) {
      url += rmiServers[currentServer];
      currentServer++;
    } else {
      url += rmiServers[currentServer];
      currentServer = 0;
    }
    url += ":1099/sum-vectors";
    return url;
  }

  private byte[] mergeVectors(int[] v1, int[] v2) {
    ByteBuffer byteBuffer = ByteBuffer.allocate((1 + v1.length + v2.length) * 4);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();

    intBuffer.put(v1.length);
    intBuffer.put(v1);
    intBuffer.put(v2);

    return byteBuffer.array();
  }

  private int[] readVectorFromFile(String fileName) throws FileNotFoundException {
    Scanner scanner = new Scanner(new File(fileName));

    int[] arr = new int[scanner.nextInt()];
    for (int i = 0; i < arr.length; i++)
      arr[i] = scanner.nextInt();

    scanner.close();
    return arr;
  }

  private void writeVectorToFile(byte[] vector, String fileName) throws IOException {
    Writer writer = new FileWriter(new File(fileName));

    IntBuffer buff = ByteBuffer.wrap(vector).asIntBuffer();
    for (int i = 0; i < buff.capacity(); i++)
      writer.write("" + buff.get(i) + "\n");

    writer.close();
  }
}
