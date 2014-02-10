package bus.uigen.translator;

public class StringToDouble implements Translator<String, Double> {
  public Double translate(String string) throws FormatException {
    try {
      return new Double((String) string);
    } 
    
     catch (NumberFormatException nfe) {
        throw new FormatException();
      }
    
    catch (Exception e) {				if (new Double(Double.NEGATIVE_INFINITY).toString().equals(string))			return new Double (Double.NEGATIVE_INFINITY);		else if (new Double(Double.POSITIVE_INFINITY).toString().equals(string))			return new  Double(Double.POSITIVE_INFINITY);		
		else if (new Double(Double.NaN).toString().equals(string))			return new  Double(Double.NaN);
		else	{
						System.out.println("NEG" + new Double(Double.NEGATIVE_INFINITY).toString());			System.out.println("POS" + new Double(Double.POSITIVE_INFINITY).toString());			System.out.println("NaN" + new Double(Double.NaN).toString());
						System.out.println("EXCEP" + e );			return new Double(0);
			//e.printStackTrace();			//throw new FormatException();		}
    }
  }
}
