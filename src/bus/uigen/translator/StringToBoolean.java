package bus.uigen.translator;

public class StringToBoolean implements Translator<String, Boolean> {
  public Boolean translate(String string) throws FormatException {
    try {
      return new Boolean((String) string);
    } catch (Exception e) {
      throw new FormatException();
    }
  }
}
