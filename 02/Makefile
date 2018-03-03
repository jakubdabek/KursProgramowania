CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z
JAVANAMES := $(wildcard *.java)

test: all
	@echo -e '\nJava:'
	java Test X 1 3 4
	@echo -ne '\n'
	java Test R M MCD MVIII MMMCCCXXXIII MMMCMXCIX
	@echo -ne '\n'
	java Test A 1 2 3 4 5 6 7 8 9 10 \
	14 44 52 92 \
	108 146 175 199 200 354 465 532 643 712 823 994 \
	1000 1111 1232

	@echo -e '\n\nC++:'
	./program X 1 3 4
	@echo -ne '\n'
	./program R M MCD MVIII MMMCCCXXXIII MMMCMXCIX MMMMDXX XXIX XXIXA IX3 MMMCMXCIX XD DX
	@echo -ne '\n'
	@./program A 1 2 3 4 5 6 7 8 9 10 \
	14 44 52 92 \
	108 146 175 199 200 354 465 532 643 712 823 994 \
	1000 1111 1232 >/dev/null

debug: DEBUGFLAGS = -g -D_GLIBCXX_DEBUG
debug: all
all: Test.class program

Test.class: Test.java
	javac Test.java
Test.java: RomanArabic.java RomanArabicException.java
	@touch $@

program: main.cpp RomanArabic.cpp
	$(CXX) $(CXXFLAGS) $(DEBUGFLAGS) -o $@ $^
RomanArabic.cpp main.cpp: RomanArabic.hpp
	@touch $@

clean:
	rm -f program $(patsubst %.java,%.class,$(JAVANAMES))

PHONY: test all clean
