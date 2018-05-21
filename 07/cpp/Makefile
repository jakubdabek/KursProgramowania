CXX = clang++
CXXFLAGS = -Wall -pedantic -std=c++17
CPPFILES := $(wildcard *.cpp)
FILENAMES := $(patsubst %.cpp, %, $(CPPFILES))

all: program

program: program.cpp

program.cpp: BTree.hpp
	@touch $@
BTree.hpp: BTree.tpp BTree_Node.tpp
	@touch $@