package bus.uigen.translator;

public class IntegerToBoolean implements Translator {
  public Object translate(Object string) throws FormatException {
    try {
      return new Boolean((String) string);
    } catch (Exception e) {
      throw new FormatException();
    }
  }
}
