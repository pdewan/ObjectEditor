package bus.uigen.translator;

public class IllegalTranslatorClassException extends Exception {
  public String getMessage() {
    return "Class needs to implement bus.uigen.translator.Translator\nto be registered with TranslatorRegistry";
  }
}
