package bus.uigen.translator;

public class StringToInteger implements Translator<String, Integer> {
  public Integer translate(String string) throws FormatException {
    try {
      return Integer.decode((String) string);
    } catch (Exception e) {
      throw new FormatException();
    }
  }
}
