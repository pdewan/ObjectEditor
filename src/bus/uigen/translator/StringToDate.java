package bus.uigen.translator;
import java.util.Date;
//looks like  this class was never completed
public class StringToDate implements Translator {
  public Object translate(Object string) throws FormatException {
    try {
      return  new Date ((String) string);
    } catch (Exception e) {				if (new Double(Double.NEGATIVE_INFINITY).toString().equals(string))			return new Double (Double.NEGATIVE_INFINITY);		else if (new Double(Double.POSITIVE_INFINITY).toString().equals(string))			return new  Double(Double.POSITIVE_INFINITY);		
		else if (new Double(Double.NaN).toString().equals(string))			return new  Double(Double.NaN);
		else	{
						System.err.println("NEG" + new Double(Double.NEGATIVE_INFINITY).toString());			System.err.println("POS" + new Double(Double.POSITIVE_INFINITY).toString());			System.err.println("NaN" + new Double(Double.NaN).toString());
						System.err.println("EXCEP" + e );			return new Double(0);
			//e.printStackTrace();			//throw new FormatException();		}
    }
  }
}
