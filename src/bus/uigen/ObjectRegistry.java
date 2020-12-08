package bus.uigen;

import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Vector;

import logging.loggable.GenericLoggableInterface;
import logging.loggable.GenericLoggerUI;
import logging.loggable.PseudoServerFront;
import util.models.Hashcodetable;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.WidgetShell;
public class ObjectRegistry implements GenericLoggableInterface
{
//	static Hashcodetable<Object, ObjectAdapter> objectToObjectAdapterMapping = new Hashcodetable();
	static ObjectAdapter currentAdapter = null;
	static Vector frameList = new Vector();
	static UIGenLoggable logger = null;
	static GenericLoggableInterface gli = null;
	static GenericLoggerUI myUI = null;
	static final String UI_ROLE = "UI";
	static final String PROGRAM_ROLE = "Program";
	static final String ProgramComponentNameExtension = "_UIGenProgramComponent";
	static String loggableRole = UI_ROLE; // ProgramRole or UIRole
	static String loggerUIRMIName;
	static BasicObjectRegistry basicObjectRegistry = new ABasicObjectRegistry();

	public static void newAdapter(ObjectAdapter n){
		currentAdapter = n;
	}

	public static void mapObjectToAdapter(Object obj, ObjectAdapter uioa){
		if(obj != null){
			basicObjectRegistry.mapObjectToAdapter(obj, uioa);
		}
	}
	// returns the latest adapter in all frames
	public static ObjectAdapter getObjectAdapter(Object obj) {
		return basicObjectRegistry.getObjectAdapter(obj);
	}
	
	public static ObjectAdapter getOrCreateObjectAdapter(Object obj) {
		ObjectAdapter retVal = basicObjectRegistry.getObjectAdapter(obj);
		if (retVal == null) {
			retVal = ObjectEditor.toObjectAdapter(obj);
			mapObjectToAdapter(obj, retVal);
		}
		return retVal;
	}

	public static String getAdapterPathFor(Object obj){
//		ObjectAdapter uioa = (ObjectAdapter) objectToObjectAdapterMapping.get(obj);
		ObjectAdapter uioa = (ObjectAdapter) basicObjectRegistry.getObjectAdapter(obj);

		if(uioa==null) return null;
		return(uioa.getCompletePathOnly());
	}
	
	public static ObjectAdapter getAdapter(String path){
		ObjectAdapter retVal = null;
		Vector pathVector = ObjectAdapter.pathToVector(path);
		System.err.println("PPP "+path+" "+pathVector);
		if(currentAdapter!=null){
			try{
				retVal = currentAdapter.pathToObjectAdapter(pathVector);
			} catch (NullPointerException e){
			} catch (Exception e){
				  System.err.println(e.getMessage());
				  e.printStackTrace();
			}
		}
		return retVal;
	}
	
	public static void addUIFrame(uiFrame f){
		System.err.println("LLL adding frame "+f+" to list of size "+frameList.size());
		frameList.addElement(f);
	}
	
	public static int indexOfUIFrame(uiFrame f){
		return(frameList.indexOf(f));
	}
	
	public static uiFrame uiFrameAt(int index){
		return((uiFrame) frameList.elementAt(index));
	}
	
	public static void doRefreshes(){
		for(int i=0;i<frameList.size();i++){
			uiFrame uif = (uiFrame) frameList.elementAt(i);
			uif.doRefresh();
			//uif.refresh();
		}
	}
	
	public static void createLoggerUI(){
		if(gli==null){
			gli = new ObjectRegistry();
			myUI = new GenericLoggerUI(gli);
			// uiFrame uif = uiGenerator.generateUIFrame(myUI);
			// uif.setVisible(true);
		}
	}
	
	public static void createLoggerUI(String progName, String rmiName, String userName, String initial, String runsProgram, boolean firstRun, String uiRmiName){
		loggerUIRMIName = uiRmiName;
		createLoggerUI();
		String extension = "";
		if(firstRun){
			extension = ProgramComponentNameExtension;
			loggableRole = PROGRAM_ROLE;
		}
		myUI.setProgramName(progName+extension);
		myUI.setUserName(userName);
		if(firstRun){
			myUI.setInitialUser(true);
			myUI.setUserRunsProgram(true);
		} else{
			myUI.setInitialUser(initial.equals("true"));
			myUI.setUserRunsProgram(runsProgram.equals("true"));
			if(initial.equals("false")){
				myUI.setRmiName(rmiName);
			}
		}
		myUI.joinSession();
	}
	public static void logAutomaticRefresh(AutomaticRefresh ar){
		if(logger==null){
			ar.execute(null);
		} else{
			logger.logAutomaticRefresh(ar);
		}
	}
	public static Object logUnivMethodInvocation(UnivMethodInvocation umi){
		if(logger==null) return(umi.execute());
		return(logger.logUnivMethodInvocation(umi));
	}
	
	public static Object logReadMethodInvocation(UnivMethodInvocation umi){
		if(logger==null) return(umi.execute());
		if(ObjectEditor.coupleElides || ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE)){
			return(logger.logReadMethodInvocation(umi));
		} else{
			return(logger.logIndependentReadMethodInvocation(umi));
		}
	}
	
	public static void logUnivPropertyChange(UnivPropertyChange upc){
		if(logger==null){
			upc.execute();
		} else{
			logger.logUnivPropertyChange(upc);
		}
	}
	
	public static void logUnivVectorEvent(UnivVectorEvent uve){
		if(logger==null){
			uve.execute();
		} else{
			logger.logUnivVectorEvent(uve);
		}
	}
	
	static void logAddMulticastUser(String objectPath, String userName){
		if(logger!=null){
			logger.logAddMulticastUser(objectPath,userName);
		}	}	
	static void logDeleteMulticastUser(String objectPath, String userName)
	{		if(logger!=null){
			logger.logDeleteMulticastUser(objectPath,userName);
		}	}	
	static void logDeleteAllMulticastUsers(String objectPath)	{		if(logger!=null){
			logger.logDeleteAllMulticastUsers(objectPath);
		}	}	
	static void logResetToBroadcast(String objectPath)
	{		if(logger!=null){
			logger.logResetToBroadcast(objectPath);
		}	}	
	static void logResetToMulticast(String objectPath)	{		if(logger!=null){
			logger.logResetToMulticast(objectPath);
		}	}	
	static void logCoupleProgramReplicas(String objectPath, boolean coupled)
	{		if(logger!=null){
			logger.logCoupleProgramReplicas(objectPath,coupled);
		}	}  	static void logSetBroadMultiCastDomain(String objectPath, boolean includeDescendant)	{		if(logger!=null){
			logger.logSetBroadMultiCastDomain(objectPath,includeDescendant);
		}	}	
	public PseudoServerFront createJoiner(String UserName,String myRmiName,boolean UserHasProgram,boolean UserRunsProgram,String TimeServerName){	    try{
		logger = new UIGenLoggable(UserName,myRmiName,UserHasProgram,UserRunsProgram,TimeServerName,loggerUIRMIName);	    } catch (Exception e){
		System.err.println("ObjectRegistry.createJoiner(): Exception "+e.getMessage());
		e.printStackTrace();
	    }	    return((PseudoServerFront) logger);
	}
	
	public PseudoServerFront createInitiator(String ProgramName,String UserName,String myRmiName,boolean UserHasProgram,boolean UserRunsProgram,String TimeServerName){
	    try{
		logger = new UIGenLoggable(ProgramName,UserName,myRmiName,UserHasProgram,UserRunsProgram,TimeServerName,loggerUIRMIName);
	    } catch (Exception e){
		System.err.println("ObjectRegistry.createInitiator(): Exception "+e.getMessage());
		e.printStackTrace();
	    }	    return((PseudoServerFront) logger);	}
	
	static Vector list = new Vector();
	static Hashtable listHash = new Hashtable();
	static Vector adapterList = new Vector();
	static Vector widgetList = new Vector();

	public static void replaceObject(Object oldObj, Object newObj){
		if(oldObj==null){
			addObject(newObj);
		} else if(oldObj != newObj){
			Integer htVal = (Integer) listHash.get(oldObj);
			if(htVal!=null){
				int index = htVal.intValue();
				System.err.println("LLL replacing (or removing) "+oldObj+" with "+newObj+" at index "+index);
				if(newObj != null){
					list.setElementAt(newObj,index);
					listHash.put(newObj,htVal);
				} else{
					list.removeElementAt(index);
				}
				listHash.remove(oldObj);
			}
		}
	}
	
	public static void addObject(Object obj){
		if(obj != null){
			System.err.println("LLL adding object "+obj+" to list of size "+list.size());
			list.addElement(obj);
			listHash.put(obj,new Integer(list.size()-1));
		}
	}

	public static int indexOf(Object obj){
		Integer htVal = (Integer) listHash.get(obj);
		if(htVal!=null){
			return(htVal.intValue());
		}
		return(-1);
		// return(list.indexOf(obj));
	}
	
	public static Object objectAt(int index){
		return(list.elementAt(index));
	}
	
	public static void addAdapter(ObjectAdapter pcl){
		//System.out.println("LLL adding adapter "+pcl+" to list of size "+adapterList.size());
		adapterList.addElement(pcl);
	}
	
	public static int indexOfAdapter(PropertyChangeListener pcl){
		return(adapterList.indexOf(pcl));
	}
	
	public static ObjectAdapter lastAdapter(){
		return((ObjectAdapter) adapterList.lastElement());
	}
	
	public static ObjectAdapter adapterAt(int index){
		return((ObjectAdapter) adapterList.elementAt(index));
	}
	
	public static void addWidget(WidgetShell w){
		System.err.println("LLL adding widget "+w+" to list of size "+widgetList.size());
		widgetList.addElement(w);
	}
	
	public static int indexOfWidget(WidgetShell w){
		return(widgetList.indexOf(w));
	}
	
	public static WidgetShell widgetAt(int index){
		return((WidgetShell) widgetList.elementAt(index));
	}
}
