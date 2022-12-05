package tcp;

public class Logger {
  public static final String GREEN = "\033[0;32m";
  public static final String BLUE = "\033[0;34m";
  public static final String PURPLE = "\033[0;35m";
  public static final String CYAN = "\033[0;36m";
  public static final String WHITE = "\033[0;37m";
  public static final String YELLOW = "\u001B[33m";

  public static void log(LogTypes type, int port, String message) {
    StringBuilder str = new StringBuilder();

    str.append(WHITE).append("[");

    String color = "";

    switch (type) {
      case CLIENT: {
        str.append(BLUE).append("CLIENT");
        color = BLUE;
        break;
      }
      case SERVER: {
        str.append(GREEN).append("SERVER");
        color = GREEN;
        break;
      }
      default:
        str.append(PURPLE).append("INFO");
        color = PURPLE;
        break;
    }

    if (port > -1)
      str.append(WHITE).append(":").append(CYAN).append("" + port);

    str.append(WHITE).append("] ").append(color).append(message).append(WHITE);

    System.out.println(str.toString());
  }
}
