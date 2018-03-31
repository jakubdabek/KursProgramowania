CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z
PROGRAM = program.exe

test: test_cpp
test_printall: test_cpp_printall

test_cpp_printall: PRINTALL = _PRINTALL
test_cpp_printall: test_cpp
test_cpp: $(PROGRAM)
	@echo -e '\n\nC++:'
	./$(PROGRAM) X 1 3 4
	@echo -ne '\n'
	./$(PROGRAM) R M MCD MVIII MMMCCCXXXIII MMMCMXCIX MMMMDXX XXIX XXIXA IX3 MMMCMXCIX XD DX
	@echo -ne '\n'
	./$(PROGRAM) A 1 2 3 4 5 6 7 8 9 10 \
	14 44 52 92 \
	108 146 175 199 200 354 465 532 643 712 823 994 \
	1000 1111 1232
	
	@echo -ne '\n'
	./$(PROGRAM) TEST$(PRINTALL) < tests.csv
	@echo -ne '\n'
	./$(PROGRAM) TEST_WRONG$(PRINTALL) < tests_wrong.csv

debug: DEBUGFLAGS = -g -D_GLIBCXX_DEBUG
debug: $(PROGRAM)
all: $(PROGRAM)

$(PROGRAM): main.cpp RomanArabic.cpp
	$(CXX) $(CXXFLAGS) $(DEBUGFLAGS) -o $@ $^
RomanArabic.cpp main.cpp: RomanArabic.hpp
	@touch $@

clean:
	rm -f $(PROGRAM)

PHONY: test test_cpp debug all clean
