package logger;

import java.util.UUID;

public class Logger {
  public static final String GREEN = "\033[0;32m";
  public static final String BLUE = "\033[0;34m";
  public static final String PURPLE = "\033[0;35m";
  public static final String CYAN = "\033[0;36m";
  public static final String WHITE = "\033[0;37m";
  public static final String YELLOW = "\u001B[33m";

  public static void log(LogTypes type, int port, UUID uuid, String message) {
    StringBuilder str = new StringBuilder();

    str.append(WHITE).append("[");

    String color = "";
    String tag = "";

    switch (type) {
      case CLIENT: {
        tag = "Client";
        color = PURPLE;
        break;
      }
      case SERVER: {
        tag = "Server";
        color = YELLOW;
        break;
      }
      default:
        tag = "Info";
        color = BLUE;
        break;
    }

    str.append(color).append(tag);

    if (port > -1)
      str.append(WHITE).append(":").append(GREEN).append("" + port);

    str.append(WHITE).append("] ").append(color).append(String.format("%-30s", message)).append(WHITE);

    if (uuid != null)
      str.append(color).append("UUID ").append(tag).append(WHITE)
          .append("(")
          .append(GREEN).append(uuid).append(WHITE).append(")");

    System.out.println(str.toString());
  }
}
