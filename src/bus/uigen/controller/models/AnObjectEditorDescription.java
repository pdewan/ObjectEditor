package bus.uigen.controller.models;

import java.util.Date;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;

public class AnObjectEditorDescription implements ObjectEditorDescription {
	static String version = "22";
//	static String dateCompiled = "1/26/2012 12:30";
	static String copyRight = "Copyright Prasun Dewan, 2012, All rights reserved.";
	static String patent = "US Patent Appl. No.: 12/532,327";

	static String url = "http://www.cs.unc.edu/~dewan/comp114/f11/";
	static String dataCompiled;
	
	public static String getDateCompiled() {
		if (dataCompiled == null)
			dataCompiled = BuildTimeManager.getBuildTimeInTextFile();
//		return dateCompiled.toString();
		return dataCompiled;
	}
	
	/* (non-Javadoc)
	 * @see bus.uigen.controller.models.ObjectEditorDescription#getCopyRight()
	 */
	public String getCopyRight() {
		return copyRight;
	}	
	/* (non-Javadoc)
	 * @see bus.uigen.controller.models.ObjectEditorDescription#getVersion()
	 */
	public String getVersion() {
		return version;
	}
	/* (non-Javadoc)
	 * @see bus.uigen.controller.models.ObjectEditorDescription#getUrl()
	 */
	public String getUrl() {
		return url;
	}
	public String toString() {
		return "ObjectEditor(Version " + getVersion() + ", built on " + getDateCompiled() + "). "+  getCopyRight() + " " + getPatent();
//				"\nObject Editor Version: " + getVersion() +
//				"\nBuilt On: " + getDateCompiled();
		 
	}
	static {
		ObjectEditor.setAttributeOfAllProperties(AnObjectEditorDescription.class, AttributeNames.COMPONENT_WIDTH, 300);
		
	}
	@Override
	public String getPatent() {
		// TODO Auto-generated method stub
		return patent;
	}
	
	

}
