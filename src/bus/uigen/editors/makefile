
JAVAFILES = EditorRegistry.java uiColorEditor.java

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
