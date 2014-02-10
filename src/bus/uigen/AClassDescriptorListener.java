package bus.uigen;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.ClassDescriptorListener;
import bus.uigen.reflect.ClassProxy;

public class AClassDescriptorListener implements ClassDescriptorListener {

	@Override
	public void attributeAdded(ClassDescriptorInterface cd,
			String attributeName, Object value) {
		ClassProxy realClass = cd.getRealClass();
		if (realClass != null) {
			  String keywordName = AttributeNames.CLASS_ATTRIBUTES_KEYWORD + 
			  	AttributeNames.KEYWORD_SEPARATOR + attributeName;
			  	ObjectEditor.associateKeywordWithClassName(keywordName, realClass);
			  }
		
	}
	

}
