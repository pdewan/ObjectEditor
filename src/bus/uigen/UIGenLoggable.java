package bus.uigen;

import java.rmi.RemoteException;
import logging.loggable.*;
import logging.logger.*;
import java.util.Vector;
import java.io.*;import java.rmi.*;

public class UIGenLoggable extends Loggable implements UIGenLoggableInterface
{
  private String eventLogClass = "eventLogClass";
  private UIGenLoggableObjectID eventLog = new UIGenLoggableObjectID("eventLog","*");
  private String objectEvents = "objectEvents";
  private String loggableEvents = "loggableEvents";
  private String[] subEvents = { objectEvents, loggableEvents };
  
  private UIGenLoggableObjectID uigenCanvas = new UIGenLoggableObjectID("uigenCanvas","*");
  
  private Vector loggableGivenReply = new Vector();
  
  private UIGenLoggableInterface programComponent = null;
  private UIGenLoggableInterface uiComponent = null;
  
  private boolean getChildAdapterCountReqSent = false;
  
  private boolean framesRefreshed = false;
  
  public void masterChangedTo(String name){
	  if(!framesRefreshed && !ObjectEditor.coupleElides){
		  framesRefreshed = true;
		  ObjectRegistry.doRefreshes();
	  }
	  super.masterChangedTo(name);
  }	 
  
  public void inputLogReplayFinish(){
	  if(!framesRefreshed && !ObjectEditor.coupleElides){
		  framesRefreshed = true;
		  ObjectRegistry.doRefreshes();
	  }
	  super.inputLogReplayFinish();
  }	  
  private void checkAndSetProgramUIComponent(){
	  try{
		if(ObjectRegistry.loggableRole.equals(ObjectRegistry.UI_ROLE)){
			  if(!ObjectEditor.runUIComponentOnly && programComponent==null){
				programComponent = (UIGenLoggableInterface) Naming.lookup(myRMIName+ObjectRegistry.ProgramComponentNameExtension);
			  }
		} else{
			  if(uiComponent==null){
				String rmiName = myRMIName.substring(0,myRMIName.indexOf(ObjectRegistry.ProgramComponentNameExtension));
				uiComponent = (UIGenLoggableInterface) Naming.lookup(rmiName);
			  }
		}
	  } catch (Exception e){
		  System.out.println("UIGenLoggable.checkAndSetProgramUIComponent(): "+e.getMessage());
		  e.printStackTrace();
	  }
  }
  
  public UIGenLoggable(String toolName, String myName, String myRMIName, boolean userHasProgram, boolean userRunsProgram, String timeServer, String loggerUIRMIName) throws RemoteException
  {    super(toolName, myName, myRMIName, userHasProgram, userRunsProgram, timeServer, 0, false, false, true, true, false, loggerUIRMIName);    myLogger.classDescription(eventLogClass,subEvents);
    myLogger.preExistingObject(eventLogClass,eventLog);
    myLogger.preExistingObject(classCanvas,uigenCanvas);
    myLogger.irrcProcessInstantiate(classCanvas,uigenCanvas,masksWithDrawing);
    myLogger.irrcSetVisible(uigenCanvas,true);      
    framesRefreshed = true;  }

  public UIGenLoggable(String myName, String myRMIName, boolean userHasProgram, boolean userRunsProgram, String timeServer, String loggerUIRMIName) throws RemoteException  {    super(myName, myRMIName, userHasProgram, userRunsProgram, timeServer, 0, false, false, true, true, false, loggerUIRMIName);
  }    protected void startMyUISServer(){
  }
  
  public void inInsert(Object command){
	try{	  if(operation.equals(record)){		  recordInput(command);
	  } else if(operation.equals(replay) && replayStarted==false){
		startReplay();		return;
	  }
	  myLogger.processEvent(new UIGenLoggableObjectID("uigenCanvas",((UIGenLoggableEvent) command).getUIGenInternalID()),attribsWithDrawing,command);
    } catch(Exception e){
	  System.out.println("Loggable(inInsert): Exception "+e.getMessage());
      e.printStackTrace();
    }
  }
  
  public void reset() throws RemoteException  {
	  super.reset();      if(initialUser){
        myLogger.preExistingObject(classCanvas,uigenCanvas);
      }
      myLogger.irrcProcessInstantiate(classCanvas,uigenCanvas,masksWithDrawing);
      myLogger.irrcSetVisible(uigenCanvas,true);  }  
  protected void startMyProgram(){
	super.startMyProgram();
    try{
		if(!initialUser){
			myLogger.irrcProcessInstantiate(classCanvas,uigenCanvas,masksWithDrawing);
			myLogger.irrcSetVisible(uigenCanvas,true);
		}
    } catch (Exception e){
      System.out.println("SharedShapes: Exception "+e.getMessage());
      e.printStackTrace();
    }
  }
  
  protected void replayToView(Object command){
  }  
  protected void replayToModel(Object command)
  {
	  try{
		checkAndSetProgramUIComponent();		if(!ObjectEditor.runUIComponentOnly && ObjectRegistry.loggableRole.equals(ObjectRegistry.UI_ROLE)){
			System.out.println("UI: replayToModel called");			programComponent.uigenReplayToModel(command);		} else{ // PROGRAM_ROLE or UI_ROLE if runUIComponentOnly			if(command instanceof ForwardReturnValue){
				System.out.println(ObjectRegistry.loggableRole+": ZZZ method call");				ForwardReturnValue frv = (ForwardReturnValue) command;
				UnivMethodInvocation umi = (UnivMethodInvocation) frv.obj;				// System.out.println("replaying "+umi);
				ForwardReturnValue result = new ForwardReturnValue(frv.destName,umi.execute());				// System.out.println("returning "+result.obj+" to "+result.destName);				if(ObjectEditor.runUIComponentOnly){ // UI_ROLE and runUIComponentOnly
					myLogger.returnValue(result);				} else{					// PROGRAM_ROLE and !runUIComponentOnly
					uiComponent.uigenReturnValue(result);
				}				// myLogger.returnValue(result);
			} else if(command instanceof AutomaticRefresh){				System.out.println(ObjectRegistry.loggableRole+": automatic refresh");				if(!ObjectEditor.runUIComponentOnly){
					uiComponent.uigenGiveNotifyOnLoggableEvent(command);
				}				giveNotifyOnLoggableEvent(command);
			} else{				System.out.println("ReplayToModel: Unrecognized command: "+command);				System.exit(1);			}
		}
	  } catch (Exception e){		  System.out.println("UIGenLoggable.replayToModel(): "+e.getMessage());		  e.printStackTrace();	  }
  }

  public void uigenReplayToModel(Object command) throws RemoteException{ // PROGRAM_ROLE
	  replayToModel(command);
  }
  
  public void uigenReturnValue(ForwardReturnValue result) throws RemoteException{ // UI_ROLE
	  System.out.println("UI: PPP returning method call return value");
	  myLogger.returnValue(result);
  }
  
  public void uigenGiveNotifyOnLoggableEvent(Object command) throws RemoteException{ // UI_ROLE
	  System.out.println("UI: uigenGiveNotifyOnLoggableEvent called");	  giveNotifyOnLoggableEvent(command);
  }
  public void notify(Object objectName, String[] attribs){
	System.out.println("UIGenLoggable.notify(): being notified with "+attribs[0]);
	if(attribs[0].equals(loggableEvents)){
		try{
			System.out.println(ObjectRegistry.loggableRole+": RRR issuing a get for automatic refresh");
			Object command = null;
			if(ObjectRegistry.loggableRole.equals(ObjectRegistry.UI_ROLE)){
				if(getChildAdapterCountReqSent==true){
					getChildAdapterCountReqSent = false;
					return;
				}
				LogEvent evt = myLogger.get(objectName,attribs);
				command = evt.arr[0].attribValue;
			} else{
				command = loggableGivenReply.elementAt(0);
				loggableGivenReply.removeElementAt(0);

				AttribValue[] a = new AttribValue[1];
				Object[] o = new Object[1];
				o[0] = new UIGenLoggableObjectID("eventLog",((UIGenLoggableObjectID) objectName).getUIGenInternalID());
				a[0] = new AttribValue(loggableEvents,command,true,o);
				checkAndSetProgramUIComponent();
				uiComponent.uigenProcessModify(new UIGenLoggableObjectID("eventLog",((UIGenLoggableObjectID) objectName).getUIGenInternalID()),a);
			}
			System.out.println(ObjectRegistry.loggableRole+": AAA got "+command);
			if(command instanceof UnivPropertyChange){
				UnivPropertyChange upc = (UnivPropertyChange) command;				upc.execute();
			} else if(command instanceof UnivVectorEvent){				// if(ObjectEditor.coupleElides || !inOutputReplay){					UnivVectorEvent uve = (UnivVectorEvent) command;					uve.execute();				// }
			} else if(command instanceof AutomaticRefresh){
				if(ObjectEditor.coupleElides || !inOutputReplay){					AutomaticRefresh ar = (AutomaticRefresh) command;
					ar.execute(myName);
				}			} else{
				System.out.println("Notify: Unrecognized command: "+command.getClass());
				System.exit(1);
			}
			if(ObjectRegistry.loggableRole.equals(ObjectRegistry.UI_ROLE)){
				super.notify(objectName,attribs); // acknowledging in case inOutputReplay
			}
		} catch (Exception e){
			System.out.println("UIGenLoggable.notify(): "+e.getMessage());
			e.printStackTrace();
		}	} else if(attribs[0].equals(objectEvents)){
	} else {
	  System.out.println("UIGenLoggable.notify(): unrecognized attrib = "+attribs[0]);
	  System.exit(1);
	}
  }
  
  public synchronized void produceReply(Object objectName, String[] attribs, Object option) throws RemoteException{
	  if(!ObjectEditor.runUIComponentOnly){
		if(ObjectRegistry.loggableRole.equals(ObjectRegistry.UI_ROLE)) return;
		else{			System.out.println("UIGenLoggable.produceReply(): unexpected invocation on programComponent");			System.exit(1);		}
	  } else {		AttribValue[] a = new AttribValue[1];
			Object[] o = new Object[1];
			o[0] = new UIGenLoggableObjectID("eventLog",((UIGenLoggableObjectID) objectName).getUIGenInternalID());
		if(attribs[0].equals(objectEvents)){
			UnivMethodInvocation umi = (UnivMethodInvocation) option;
			Object result = umi.execute();
			a[0] = new AttribValue(objectEvents,result,true,o);
		} else{ // loggableEvents
			    if(loggableGivenReply.size()==0){
				    System.out.println("UIGenLoggable.produceReply(): loggableGivenReply not ready");
				    (new Exception()).printStackTrace();
				    System.exit(1);
			    }
			    a[0] = new AttribValue(loggableEvents,loggableGivenReply.elementAt(0),true,o);
			    loggableGivenReply.removeElementAt(0);
		}
		try{
			    System.out.println("Logging reply to a get method call");
			myLogger.processModify(new UIGenLoggableObjectID("eventLog",((UIGenLoggableObjectID) objectName).getUIGenInternalID()),a);
		} catch(Exception e){
			System.out.println("UIGenLoggable.produceReply(): Exception "+e.getMessage());
			e.printStackTrace();
		}
	  }
  }    /*
  private void temp(Object objectName, String[] attribs, Object option){
    checkAndSetProgramUIComponent();    AttribValue[] a = new AttribValue[1];
    Object[] o = new Object[1];
    o[0] = eventLog;
    if(attribs[0].equals(objectEvents)){
		UnivMethodInvocation umi = (UnivMethodInvocation) option;
		Object result = umi.execute();
		a[0] = new AttribValue(objectEvents,result,true,o);
		System.out.println(ObjectRegistry.loggableRole+": AAA logging a reply for a get");
    } else{ // loggableEvents
	    if(loggableGivenReply.size()==0){
		    System.out.println("UIGenLoggable.produceReply(): loggableGivenReply not ready");
		    (new Exception()).printStackTrace();
		    System.exit(1);
	    }
	    a[0] = new AttribValue(loggableEvents,loggableGivenReply.elementAt(0),true,o);
	    loggableGivenReply.removeElementAt(0);
	    System.out.println(ObjectRegistry.loggableRole+": AAA logging a reply for automatic refresh");
    }
    try{
	myLogger.processModify(eventLog,a);
	uiComponent.uigenProcessModify(eventLog,a);
    } catch(Exception e){
	System.out.println("UIGenLoggable.produceReply(): Exception "+e.getMessage());
	e.printStackTrace();
    }
		    
  }
  */

  public void uigenProcessModify(Object objectName, AttribValue values[]) throws RemoteException{
	  if(values[0].attribName.equals(objectEvents)){
		System.out.println(ObjectRegistry.loggableRole+": AAA logging a reply for a get");
	  } else {
		  System.out.println(ObjectRegistry.loggableRole+": AAA logging a reply for automatic refresh");
	  }
	  myLogger.processModify(objectName,values);
  }
  
  public Object logUnivMethodInvocation(UnivMethodInvocation umi){
	if(ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE)){
		System.out.println("UIGenLoggable.logUnivMethodInvocation(): unexpected invocation on the program component");
		System.exit(1);
	}
	Object retVal = null;
	try{
		System.out.println("ZZZ logUnivMethodInvocation");
		retVal = myLogger.invokeMethod(new UIGenLoggableObjectID("uigenCanvas",((UIGenLoggableEvent) umi).getUIGenInternalID()),attribsWithDrawing,umi);
		System.out.println("ZZZ got "+retVal);
	} catch ( Exception e){
		System.out.println("UIGenLoggable.logUnivMethodInvocation: Exception "+e.getMessage());
		e.printStackTrace();
	}
	return(retVal);
  }
  
  public Object logIndependentReadMethodInvocation(UnivMethodInvocation umi){
	if(ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE)){
		System.out.println("UIGenLoggable.logIndependentReadMethodInvocation(): unexpected invocation on the program component");
		System.exit(1);
	}
	Object retVal = null;
	try{
		System.out.println("ZZZ logIndependentReadMethodInvocation");
		retVal = myLogger.getIndependent(new UIGenLoggableObjectID("uigenCanvas",((UIGenLoggableEvent) umi).getUIGenInternalID()),attribsWithDrawing,umi);
		System.out.println("ZZZ got "+retVal);
	} catch ( Exception e){
		System.out.println("UIGenLoggable.logUnivMethodInvocation: Exception "+e.getMessage());
		e.printStackTrace();
	}
	return(retVal);
  }
  
  public Object logReadMethodInvocation(UnivMethodInvocation umi){
	  Object retVal = null;
	  try{
		  System.out.println(ObjectRegistry.loggableRole+": AAA issuing a get for a property");
		  if(ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE)){
			  retVal = umi.execute();
			  AttribValue[] a = new AttribValue[1];
			  Object[] o = new Object[1];
			  o[0] = new UIGenLoggableObjectID("eventLog",((UIGenLoggableEvent)umi).getUIGenInternalID());
			  a[0] = new AttribValue(objectEvents,retVal,true,o);
			  if(ObjectEditor.coupleElides){
				checkAndSetProgramUIComponent();
				uiComponent.uigenProcessModify(new UIGenLoggableObjectID("eventLog",((UIGenLoggableEvent) umi).getUIGenInternalID()),a);
			  }
		  } else{
			String[] s = new String[1];
			if(uiFrame.getChildAdapterCountReq!=null){
				AutomaticRefresh ar = uiFrame.getChildAdapterCountReq;
				uiFrame.getChildAdapterCountReq = null;
				getChildAdapterCountReqSent = true;
				logAutomaticRefresh(ar);
				s[0] = loggableEvents;
				if(inOutputReplay){
					System.out.println("UIGenLoggable.logReadMethodInvocation: unexpected inOutputReplay state");
					System.exit(1);
				}
				LogEvent evt = myLogger.get(new UIGenLoggableObjectID("eventLog",((UIGenLoggableEvent) umi).getUIGenInternalID()),s);
				Object tmp = evt.arr[0].attribValue;
				System.out.println(ObjectRegistry.loggableRole+": UIGenLoggable.logReadMethodInvocation(): got but discarding "+tmp);
			}
			s[0] = objectEvents;
			if(inOutputReplay)				myLogger.orrcReplayNextOutputMessage();			LogEvent evt = myLogger.get(new UIGenLoggableObjectID("eventLog",((UIGenLoggableEvent) umi).getUIGenInternalID()),s,umi);
			retVal = evt.arr[0].attribValue;
		  }
		System.out.println(ObjectRegistry.loggableRole+": got "+retVal);
		/*
		if(retVal instanceof AListenableVector){
			System.out.println("vector size = "+((AListenableVector) retVal).size());
		}
		  */

	} catch ( Exception e){
		System.out.println("UIGenLoggable.logReadMethodInvocation: Exception "+e.getMessage());
		e.printStackTrace();
	}
	return(retVal);
  }
  
  public void logAutomaticRefresh(AutomaticRefresh ar){
	if(ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE)){
		System.out.println("UIGenLoggable.logAutomaticRefresh(): unexpected invocation on the program component");
		System.exit(1);
	}
	ar.setSource(myName);
	System.out.println(ObjectRegistry.loggableRole+": PPP logging "+ar);
	inInsert(ar);
  }

  private synchronized void giveNotifyOnLoggableEvent(Object command){	System.out.println(ObjectRegistry.loggableRole+": AAA logging loggableEvents notify with "+command);
	if(ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE) || ObjectEditor.runUIComponentOnly){
	  loggableGivenReply.addElement(command);
	}	String[] a = new String[1];	a[0] = loggableEvents;
	try{		if(ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE)){			notify(new UIGenLoggableObjectID("eventLog",((UIGenLoggableEvent) command).getUIGenInternalID()),a);
		} else{			myLogger.processNotify(new UIGenLoggableObjectID("eventLog",((UIGenLoggableEvent) command).getUIGenInternalID()),a);		}
	} catch ( Exception e){
		System.out.println("UIGenLoggable.giveNotifyOnLoggableEvent: Exception "+e.getMessage());
		e.printStackTrace();
	}
  }
  
  public void logUnivPropertyChange(UnivPropertyChange upc){
	  try{
		  System.out.println(ObjectRegistry.loggableRole+": UnivPropertyChange occurred");
		if(ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE)){
			  checkAndSetProgramUIComponent();
			  uiComponent.uigenLogUnivPropertyChange(upc);
		}
		giveNotifyOnLoggableEvent(upc);
	  } catch ( Exception e){
		System.out.println("UIGenLoggable.logUnivPropertyChange: Exception "+e.getMessage());
		e.printStackTrace();
	  }
  }

  public void uigenLogUnivPropertyChange(UnivPropertyChange upc) throws RemoteException{
	  logUnivPropertyChange(upc);
  }

  public void logUnivVectorEvent(UnivVectorEvent uve){
	  try{
		  System.out.println(ObjectRegistry.loggableRole+": UnivVectorEvent occurred");
		if(ObjectRegistry.loggableRole.equals(ObjectRegistry.PROGRAM_ROLE)){
			  checkAndSetProgramUIComponent();
			  uiComponent.uigenLogUnivVectorEvent(uve);
		}
		giveNotifyOnLoggableEvent(uve);
	  } catch ( Exception e){
		System.out.println("UIGenLoggable.logUnivVectorEvent: Exception "+e.getMessage());
		e.printStackTrace();
	  }
  }
  
  public void uigenLogUnivVectorEvent(UnivVectorEvent uve) throws RemoteException{
	  logUnivVectorEvent(uve);
  }

	public void logAddMulticastUser(String objectPath, String userName){		try{
				myLogger.addMulticastUser(new UIGenLoggableObjectID("",objectPath),userName);
		} catch ( Exception e){			e.printStackTrace();		}
	}	
	public void logDeleteMulticastUser(String objectPath, String userName)
	{		try{
				myLogger.deleteMulticastUser(new UIGenLoggableObjectID("",objectPath),userName);
		} catch ( Exception e){			e.printStackTrace();		}
	}	
	public void logDeleteAllMulticastUsers(String objectPath)	{		try{
				myLogger.deleteAllMulticastUsers(new UIGenLoggableObjectID("",objectPath));
		} catch ( Exception e){			e.printStackTrace();		}
	}	
	public void logResetToBroadcast(String objectPath)
	{		try{
				myLogger.resetToBroadcast(new UIGenLoggableObjectID("",objectPath));
		} catch ( Exception e){			e.printStackTrace();		}
	}	
	public void logResetToMulticast(String objectPath)	{		try{
				myLogger.resetToMulticast(new UIGenLoggableObjectID("",objectPath));
		} catch ( Exception e){			e.printStackTrace();		}
	}	
	public void logCoupleProgramReplicas(String objectPath, boolean coupled)
	{		try{
				myLogger.coupleProgramReplicas(new UIGenLoggableObjectID("",objectPath),coupled);
		} catch ( Exception e){			e.printStackTrace();		}
	}  	public void logSetBroadMultiCastDomain(String objectPath, boolean includeDescendant)	{		try{
				myLogger.setBroadMultiCastDomain(new UIGenLoggableObjectID("",objectPath),includeDescendant);
		} catch ( Exception e){			e.printStackTrace();		}
	}}