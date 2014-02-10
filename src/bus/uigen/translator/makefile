
JAVAFILES = ClassTranslator.java DefaultRegistry.java \
	FormatException.java IllegalTranslatorClassException.java \
	IntegerToBoolean.java StringToBoolean.java \
	StringToCharacter.java StringToFloat.java \
	StringToInteger.java Translator.java \
	TranslatorRegistry.java

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
