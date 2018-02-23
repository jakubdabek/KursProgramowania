JAVANAMES := $(wildcard Java/*.java)
CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z
CPPFILES := $(wildcard cpp/*.cpp)
OBJECTFILES := $(patsubst %.cpp,%.o,$(CPPFILES))

test: all
	@echo -e '\nJava:'
	cd Java && java Test 	ctt 6 8 4 8 4 90 7 7 7 7 30
	@echo -e '\nc++:'
	cd cpp && ./program 	ctt 6 8 4 8 4 90 7 7 7 7 30

all: Java/Test.class cpp/program

Java/Test.class: $(JAVANAMES)
	cd Java && javac Test.java

cpp/program: $(OBJECTFILES)
	$(CXX) $(CXXFLAGS) -o $@ $^
$(addprefix cpp/,Tetragon.hpp Pentagon.hpp Hexagon.hpp Circle.hpp): cpp/Shape.hpp
	@touch $@
$(addprefix cpp/,Tetragon.cpp Pentagon.cpp Hexagon.cpp Circle.cpp): %.cpp : %.hpp
	@touch $@
$(addprefix cpp/,Rectangle.hpp Square.hpp Rhombus.hpp): cpp/Tetragon.hpp
	@touch $@
$(addprefix cpp/,Rectangle.cpp Square.cpp Rhombus.cpp): %.cpp : %.hpp
	@touch $@
$(OBJECTFILES): %.o : %.cpp


clean: 
	rm -f cpp/program $(patsubst %.java,%.class,$(JAVANAMES)) $(OBJECTFILES)

PHONY: test all clean