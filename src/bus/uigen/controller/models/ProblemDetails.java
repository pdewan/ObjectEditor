package bus.uigen.controller.models;


public interface ProblemDetails {
	public String getExplanation() ;
	public int getNumberOfCasesThisSession() ;
	public boolean preSetShowExperienceOfOthers();
	public boolean getShowExperienceOfOthers() ;
	public void setShowExperienceOfOthers(boolean showClassInformation);
	public void showAllCasesThisSession();

}
