package bus.uigen.undo;

public class NameTokenizer implements StringEnumeration
{
	String string;
	int nextWordStart = 0;
	int prevWordStart = 0;
	int nextWordEnd = 0;
	public NameTokenizer(String theString) {
		string = theString;
		advanceToNextWordEnd();
	}
	public boolean hasMoreElements() {
		return nextWordStart < string.length();  
	}
	public String currentSuffix() {
		if (hasMoreElements())
				//return string.substring(nextWordEnd, string.length()).toLowerCase();
				return string.substring(nextWordStart, string.length()).toLowerCase();
		else
			return "";
	}
	public String currentPrefix() {
		
		return string.substring(0, prevWordStart).toLowerCase();
		
	}
	public String nextElement() {
		String retVal = string.substring(nextWordStart, nextWordEnd).toLowerCase();
		prevWordStart = nextWordStart;
		nextWordStart = nextWordEnd;		
		skipNonLetters();
		advanceToNextWordEnd();
		return retVal;
	}
	public void skipNonLetters() {
		//advance past non letters
		while (nextWordStart < string.length() && !Character.isLetter(string.charAt(nextWordStart)))
			nextWordStart++;
	}
	void advanceToNextWordEnd() {				
		nextWordEnd= nextWordStart + 1;
		if (nextWordEnd < string.length() && Character.isUpperCase(string.charAt(nextWordEnd))) {
			//Word with all upppercases
			while (nextWordEnd < string.length() &&
				   Character.isUpperCase(string.charAt(nextWordEnd)))
				   nextWordEnd++;
			if (nextWordEnd < string.length() && Character.isLowerCase(string.charAt(nextWordEnd)))
				// e.g. getBMIValue. Want to go back to 'V'
				nextWordEnd--;
			
		} else  {
			while (nextWordEnd < string.length() && 
			 Character.isLowerCase(string.charAt(nextWordEnd) ))
			   nextWordEnd++;
		}
		
			
	}
}
