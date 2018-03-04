CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z -g
JAVANAMES := $(wildcard *.java)

test: all
	@echo -e '\nJava:'
	java Program01_2017 4 0 1 8 -1 aaaaa
	@echo -e '\nc++:'
	./program 4 0 1 8 -1 aaaaa

all: Program01_2017.class program

Program01_2017.class: Program01_2017.java
	javac Program01_2017.java
Program01_2017.java: PascalTriangleRow.java
	@touch $@

program: PascalTriangleRow.cpp
	$(CXX) $(CXXFLAGS) -o $@ $^

clean:
	rm -f program $(patsubst %.java,%.class,$(JAVANAMES))

PHONY: test all clean