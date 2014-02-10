package bus.uigen.translator;

public interface Translator<FromType, ToType> {
  // Not used now
  /*
  public String from();
  public String to();*/
  // Translate an object
  public ToType translate(FromType obj) throws FormatException;
}
