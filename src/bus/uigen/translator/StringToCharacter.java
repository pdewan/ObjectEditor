package bus.uigen.translator;

public class StringToCharacter implements Translator<String, Character> {
  public Character translate(String string) throws FormatException {
    try {
      return new Character(((String) string).charAt(0));
    } catch (ClassCastException e) {
      throw new FormatException();
    }
  }
}
