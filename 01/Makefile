CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z
JAVANAMES := $(wildcard *.java)

test: all
	@echo -e '\nJava:'
	java Program01 14 0 2 4 8 -1 aaaa 3asd 05 0x04 xd
	@echo -ne '\n'
	java Program01 -4 8 2
	@echo -ne '\n'
	java Program01 0
	@echo -ne '\n'
	java Program01 0 0 1

	@echo -e '\n\nC++:'
	./program 14 0 2 4 8 -1 aaaa 3asd 05 0x04 xd
	@echo -ne '\n'
	./program -4 8 2
	@echo -ne '\n'
	./program 0
	@echo -ne '\n'
	./program 0 0 1

all: Program01.class program

Program01.class: Program01.java
	javac Program01.java
Program01.java: PrimeNumbers.java
	@touch $@

program: PrimeNumbers.cpp
	$(CXX) $(CXXFLAGS) -o $@ $^

clean: 
	rm -f program $(patsubst %.java,%.class,$(JAVANAMES))

PHONY: test all clean
