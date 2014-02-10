package bus.uigen.translator;

public class StringToFloat implements Translator<String, Float> {
  public Float translate(String string) throws FormatException {
    try {
      return new Float((String) string);
    } catch (Exception e) {
      throw new FormatException();
    }
  }
}
