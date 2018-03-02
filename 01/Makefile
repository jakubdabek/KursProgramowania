CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z
JAVANAMES := $(wildcard *.java)

test: all
	@echo -e '\nJava:'
	java Test 14 0 2 4 8 -1 aaaa 3asd 05 0x04 xd
	@echo -ne '\n'
	java Test -4 8 2
	@echo -ne '\n'
	java Test 0
	@echo -ne '\n'
	java Test 0 0 1

	@echo -e '\n\nC++:'
	./program 14 0 2 4 8 -1 aaaa 3asd 05 0x04 xd
	@echo -ne '\n'
	./program -4 8 2
	@echo -ne '\n'
	./program 0
	@echo -ne '\n'
	./program 0 0 1

all: Test.class program

Test.class: Test.java
	javac Test.java
Test.java: PrimeNumbers.java
	@touch $@

program: PrimeNumbers.cpp
	$(CXX) $(CXXFLAGS) -o $@ $^

clean: 
	rm -f program $(patsubst %.java,%.class,$(JAVANAMES))

PHONY: test all clean
