package bus.uigen;
// Maintains a BusAgent 
// used to check for externally registered components
// and to make any local connections.


import bus.agent.*;


public class OEBusAgent {
  private static BusAgent localBusAgent = null;
  private String localAgentName = "uiLocalAgent"; // Need to add a uniquifier
  private String centralServer  = "michigan";     // Need to read there from
  private int rmiRegistryPort   = 1099;           // System properties

  private void initBusAgent() {
    if (localBusAgent == null) {		try {
      localBusAgent = new BusAgentImpl(localAgentName,
				   centralServer,
				   rmiRegistryPort);		} catch (Exception e) {
			System.err.println("could not create bus agent");		}
      if (localBusAgent == null) {
	System.err.println("Unable to create a local BusAgent");
      }
    }
  }
}
