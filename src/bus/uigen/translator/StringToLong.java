package bus.uigen.translator;

public class StringToLong implements Translator {
  public Object translate(Object string) throws FormatException {
    try {
      return new Long((String) string);
    } catch (Exception e) {
      throw new FormatException();
    }
  }
}
