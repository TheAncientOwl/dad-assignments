import snmp.TrapReceiver;
import snmp.TrapSender;

public class Main {
  public static void main(String[] args) {
    Thread receiverThread = new Thread(() -> {
      TrapReceiver.main(null);
    });
    receiverThread.start();

    Thread senderThread = new Thread(() -> {
      TrapSender.main(null);
    });
    senderThread.start();

    try {
      receiverThread.join();
      senderThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
