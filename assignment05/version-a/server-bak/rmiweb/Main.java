package rmiweb;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Main {
  public static void main(String[] args) {
    String registryIP = "0.0.0.0";
    String registryPort = "1099";

    try {
      System.setSecurityManager(new RMISecurityManager());

      SumVectors sumVectors = new SumVectors();

      Naming.rebind("rmi://" + registryIP + ":" + registryPort + "/sum-vectors", sumVectors);

      System.out.println("> Server for sum vectors waiting...");
      System.out.flush();
    } catch (RemoteException | MalformedURLException e) {
      e.printStackTrace();
    }
  }
}
