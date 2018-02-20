CC = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z -g

all: program
	./program 4 0 1 8 -1 aaaaa
program: PascalTriangleRow.cpp
	$(CC) $(CXXFLAGS) -o $@ $^
clean: 
	rm -f program