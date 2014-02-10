package bus.uigen.models;

public class AComputerDefinition implements ComputerDefinition {
	String word;
	String meaning;
	public AComputerDefinition(String word, String meaning) {
		super();
		this.word = word;
		this.meaning = meaning;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.models.ComputerDefinition#getWord()
	 */
	public String getWord() {
		return word;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.models.ComputerDefinition#getMeaning()
	 */
	public String getMeaning() {
		return meaning;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.models.ComputerDefinition#setWord(java.lang.String)
	 */
	public void setWord(String word) {
		this.word = word;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.models.ComputerDefinition#setMeaning(java.lang.String)
	 */
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	

}
