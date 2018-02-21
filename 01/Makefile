CC = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z -g
JAVANAMES := $(wildcard *.java)

test: all
	@echo -e '\nJava:'
	java Test 4 0 1 8 -1 aaaaa
	@echo -e '\nc++:'
	./program 4 0 1 8 -1 aaaaa

all: Test.class program

Test.class: 
	javac Test.java

program: PascalTriangleRow.cpp
	$(CC) $(CXXFLAGS) -o $@ $^

clean: 
	rm -f program $(patsubst %.java,%.class,$(JAVANAMES))

PHONY: test all clean