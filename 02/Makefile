CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z
JAVANAMES := $(wildcard *.java)

test: test_java test_cpp

test_java: Program02.class
	@echo -e '\nJava:'
	java Program02 X 1 3 4
	@echo -ne '\n'
	java Program02 R M MCD MVIII MMMCCCXXXIII MMMCMXCIX MMMMDXX XXIX XXIXA IX3 MMMCMXCIX XD DX
	@echo -ne '\n'
	java Program02 A 1 2 3 4 5 6 7 8 9 10 \
	14 44 52 92 \
	108 146 175 199 200 354 465 532 643 712 823 994 \
	1000 1111 1232

	@echo -ne '\n'
	java Program02 TEST_PRINTALL < tests.csv
	@echo -ne '\n'
	java Program02 TEST_WRONG_PRINTALL < tests_wrong.csv

test_cpp: program
	@echo -e '\n\nC++:'
	./program X 1 3 4
	@echo -ne '\n'
	./program R M MCD MVIII MMMCCCXXXIII MMMCMXCIX MMMMDXX XXIX XXIXA IX3 MMMCMXCIX XD DX
	@echo -ne '\n'
	@./program A 1 2 3 4 5 6 7 8 9 10 \
	14 44 52 92 \
	108 146 175 199 200 354 465 532 643 712 823 994 \
	1000 1111 1232 2>/dev/null
	
	@echo -ne '\n'
	./program TEST_PRINTALL < tests.csv
	@echo -ne '\n'
	./program TEST_WRONG_PRINTALL < tests_wrong.csv

debug: DEBUGFLAGS = -g -D_GLIBCXX_DEBUG
debug: program
all: Program02.class program

Program02.class: Program02.java
	javac Program02.java
Program02.java: RomanArabic.java RomanArabicException.java
	@touch $@

program: main.cpp RomanArabic.cpp
	$(CXX) $(CXXFLAGS) $(DEBUGFLAGS) -o $@ $^
RomanArabic.cpp main.cpp: RomanArabic.hpp
	@touch $@

clean:
	rm -f program $(patsubst %.java,%.class,$(JAVANAMES))

PHONY: test test_java test_cpp debug all clean
