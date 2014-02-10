
JAVAFILES = JPasswordFieldAdapter.java JSliderAdapter.java \
	uiHashtableWidgetAdapter.java uiJCheckBoxAdapter.java \
	uiJPanelAdapter.java uiJTextFieldAdapter.java \
	uiPanelAdapter.java uiTextFieldAdapter.java

CLASSFILES = $(JAVAFILES:.java=.class)
DEBUG = -g
JARFILE = bus-uigen.jar
EXPORT = /afs/unc/home/konglath/public

.SUFFIXES: .java .class

.java.class:
	javac $(DEBUG) -classpath ../..:.:../../bus:${CLASSPATH} $<

all: $(CLASSFILES) 

clean:
	\rm *.class *~
