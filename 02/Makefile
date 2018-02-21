CC = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z -g
JAVANAMES := $(wildcard Java/*.java)

test: all
	@echo -e '\nJava:'
	cd Java && java Test 	ctt 6 8 4 8 4 90 7 7 7 7 30
#	@echo -e '\nc++:'
#	cd cpp && ./program 	ctt 6 8 4 8 4 90 7 7 7 7 30

all: Java/Test.class

Java/Test.class: $(JAVANAMES)
	cd Java && javac Test.java

clean: 
	rm -f cpp/program $(patsubst %.java,%.class,$(JAVANAMES))

PHONY: test all clean