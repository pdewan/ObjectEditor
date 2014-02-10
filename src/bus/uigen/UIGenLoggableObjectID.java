package bus.uigen;

public class UIGenLoggableObjectID implements java.io.Serializable, logging.logger.LoggerMulticastObjectID
{
	private Object idForLogger;
	private String uigenInternalID;
	
	public UIGenLoggableObjectID(Object idForLogger, String uigenInternalID){
		this.idForLogger = idForLogger;
		this.uigenInternalID = uigenInternalID;
	}
	
	public String getUIGenInternalID(){ return uigenInternalID; }
	
	public boolean equals(Object another){
		if(another == null || !(another instanceof UIGenLoggableObjectID)){
			return false;
		}
		return idForLogger.equals(((UIGenLoggableObjectID) another).idForLogger);
	}
	
	public String toString(){
		return "uigen:"+idForLogger.toString()+":"+uigenInternalID;
	}
	public boolean multicastSubsumes(logging.logger.LoggerMulticastObjectID objID){
		if(objID==null || !(objID instanceof UIGenLoggableObjectID)){
			return false;
		} else{
			UIGenLoggableObjectID ugid = (UIGenLoggableObjectID) objID;
			return (ugid.uigenInternalID.equals("*") || ugid.uigenInternalID.startsWith(this.uigenInternalID));
		}
	}
	
	public boolean multicastMatches(logging.logger.LoggerMulticastObjectID objID){
		if(objID==null || !(objID instanceof UIGenLoggableObjectID)){
			return false;
		} else{
			UIGenLoggableObjectID ugid = (UIGenLoggableObjectID) objID;
			return (ugid.uigenInternalID.equals("*") || ugid.uigenInternalID.equals(this.uigenInternalID));
		}
	}
}
