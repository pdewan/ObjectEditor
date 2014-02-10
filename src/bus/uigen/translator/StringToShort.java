package bus.uigen.translator;

public class StringToShort implements Translator {
  public Object translate(Object string) throws FormatException {
    try {
      return new Short((String) string);
    } catch (Exception e) {
      throw new FormatException();
    }
  }
}
