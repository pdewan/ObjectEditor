
JAVAFILES =   Attribute.java AttributeManager.java CopyAttributeVector.java \
	Connector.java \
	HashtableToInstanceAttributes.java InstanceAttributesToHashtable.java \
	InvokeButton.java \
	LocalAttributeDescriptor.java LocalAttributeListener.java \
	ObjectValueChangedListener.java Selectable.java \
	VectorListener.java componentDictionary.java \
	myLockManager.java primitiveClassList.java \
	propertyClass.java uiAddComponentEvent.java \
	uiAttributeCollection.java uiAttributeEditor.java \
	uiAttributeManager.java uiBean.java uiChangeComponentEvent.java \
	uiClassAdapter.java uiClassAttributeManager.java uiClassFinder.java \
	uiClassMapper.java \
	uiComponentValueChangedListener.java uiConfigurable.java \
	uiConstantMenuItem.java uiConstructorChooser.java \
	uiConstructorMenuItem.java uiContainerAdapter.java \
	uiDeleteComponentEvent.java uiFrame.java \
	uiExtendedMenu.java \
	uiFrameList.java uiGenerator.java \
	uiGenericWidget.java uiGridLayout.java \
	uiHashtableAdapter.java uiHashtableWidget.java \
	uiInstantiator.java \
	uiMethodInvocationManager.java uiMethodMenuItem.java \
	uiObjectAdapter.java \
	uiPopupMenu.java uiProxyInterface.java uiProxy.java \
	uiComponentWithPopupSupport.java uiLabelWithPopupSupport.java \
	uiPopupMenuActionListener.java \
	uiPreference.java uiPreferenceSupport.java \
	uiPrimitive.java uiPrimitiveAdapter.java \
	uiPrimitiveSupport.java uiPropertySetter.java \
	uiReplaceableChildren.java uiSelectionManager.java \
	uiValueChangedEvent.java \
	uiVectorAdapter.java uiVectorEvent.java \
	uiWidgetAdapter.java \
	RegisterObject.java \
	ViewInfo.java ClassDescriptorBeanInfo.java \
	ViewIntrospector.java ClassDescriptor.java \
	FieldDescriptor.java ConstantDescriptor.java \
	ConstructorDescriptor.java \
	ClassDescriptorCache.java AttributeNames.java RightMenu.java \
	MethodAction.java RightMenuCache.java RightMenuItem.java \
	ObjectClipboard.java uiParameters.java uiParametersViewInfo.java\
	htElement.java htElementBeanInfo.java \
	BeanInfoWriter.java uiGenAction.java uiGenMenuItem.java \
	SimpleViewInfo.java AdapterVisitor.java

CLASSFILES = $(JAVAFILES:.java=.class)
DEBUG = -g
JARFILE = bus-uigen.jar
EXPORT = /afs/unc/home/konglath/public

.SUFFIXES: .java .class

.java.class:
	javac $(DEBUG) -classpath ../..:.:../../bus:${CLASSPATH} $<

all: $(CLASSFILES) 
	cd translator;make; cd ..
	cd adapters;make; cd ..
	cd editors; make; cd ..

jar: all
	mkdir -p bus/uigen/translator
	mkdir -p bus/uigen/adapters
	mkdir -p bus/uigen/editors
	cp *.class bus/uigen
	cp translator/*.class bus/uigen/translator
	cp adapters/*.class bus/uigen/adapters
	cp editors/*.class bus/uigen/editors
	jar cf0 ${JARFILE} bus
	cp ${JARFILE} ../..
	\rm -rf bus

export: jar
	mv ${JARFILE} ${EXPORT}

export2: all
	cp ${CLASSFILES} ${EXPORT}/bus/uigen/

clean:
	\rm *.class *~
