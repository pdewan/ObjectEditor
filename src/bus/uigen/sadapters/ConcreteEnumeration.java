package bus.uigen.sadapters;
import java.util.List;

import bus.uigen.uiFrame;
import bus.uigen.undo.CommandCreator;
import bus.uigen.undo.CommandListener;
public interface ConcreteEnumeration extends ConcretePrimitive {
	public int choicesSize();
  public Object choiceAt(int i) ;  public Object getValue();  public void setValue(Object newVal, CommandListener commandListener);	
  public void addUserChoice(String newVal, CommandListener commandListener) ;  public boolean isEnumeration() ;
  public int getIndexOfSelection();
  public List getChoices();
  
	
}