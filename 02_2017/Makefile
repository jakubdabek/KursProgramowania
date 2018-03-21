JAVANAMES := $(wildcard Java/*.java)
CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z
CPPFILES := $(wildcard cpp/*.cpp)
OBJECTFILES := $(patsubst %.cpp,%.o,$(CPPFILES))

test: all
	@echo -e '\nJava:'
	cd Shapes && java -cp target/classes com.dabek.jakub.shapes.App	ctt 6 8 4 8 4 90 7 7 7 7 30
	@echo -e '\nc++:'
	cd cpp && ./program ctt 6 8 4 8 4 90 7 7 7 7 30

all: java cpp

java:
	cd Shapes && mvn compile
	#mkdir -p Shapes/target/classes
	#cd Shapes && javac -cp src/main/java/ src/main/java/com/dabek/jakub/shapes/App.java -d target/classes/

cpp: cpp/program

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
	rm -f cpp/program $(OBJECTFILES)
	cd Shapes && mvn clean
	#rm -rf Shapes/target

PHONY: test all java cpp clean