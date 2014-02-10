package bus.uigen.models;
import java.util.ArrayList;
import java.util.List;
import util.annotations.Visible;
import util.models.AListenableVector;
import util.models.VectorChangeSupport;
import util.remote.ProcessExecer;
import bus.uigen.misc.OEMisc;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.LIST_PATTERN)
public class AMainClassListLauncher /*extends AListenableVector<Class>*/  implements MainClassListLauncher {
	List<ProcessExecer> executed = new ArrayList();	
	List<Class> mainClasses  = new ArrayList();
	List<String> mainArgs = new ArrayList();
	transient protected VectorChangeSupport<Class> vectorChangeSupport = new VectorChangeSupport(
			this);
	public AMainClassListLauncher() {
		Thread thread = new Thread(this);
		Runtime.getRuntime().addShutdownHook(thread);
	}
	public int size() {
		return mainClasses.size();
	}
	public Class get(int index) {
		return mainClasses.get(index);
	}
	public void open(Class element) {
		executed.add(OEMisc.runWithObjectEditorConsole(element, ""));
	}	
	public void execute(Class element) {
		open(element);
	}	
	public void terminateChildren() {
		killAllChildren();
	}	
	public void terminateAll() {
		System.exit(0);
	}	
	void killAllChildren() {
		for (ProcessExecer processExecer: executed) {
			processExecer.getProcess().destroy();
		}
	}
	@Visible(false)
	public void run() {
		killAllChildren();
	}
	@Override
	public boolean add(Class element) {
		return add(element, "" );
	}
	@Override
	public boolean add(Class element, String args) {
		boolean retVal = mainClasses.add(element);
		retVal = retVal && mainArgs.add(args);
		vectorChangeSupport.elementAdded(element);
		return retVal;
	}
}
