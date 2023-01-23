package rmiweb;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ComputePi {
  public static void main(String args[]) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      String name = "Compute";
      Registry registry = LocateRegistry.getRegistry("127.0.0.1");
      Compute comp = (Compute) registry.lookup(name);
      SumVectors task = new SumVectors(null, null);
      int[] sum = comp.executeTask(task);
      System.out.println(sum);
    } catch (Exception e) {
      System.err.println("ComputePi exception:");
      e.printStackTrace();
    }
  }
}
