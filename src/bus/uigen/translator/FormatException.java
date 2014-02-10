package bus.uigen.translator;

public class FormatException extends RuntimeException {
  public String getMessage() {
    return "Illegal argument format";
  }
}
