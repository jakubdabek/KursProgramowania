JAVANAMES := $(wildcard Java/*.java)
CXX = clang++
CXXFLAGS = -Wall --pedantic -std=c++1z
#CPPFILES := $(wildcard cpp/*.cpp)
#OBJECTFILES := $(patsubst %.cpp,%.o,$(CPPFILES))

print_command: all
	@echo java -cp target/classes com.dabek.jakub.ezgui.App

run: all
	java -cp "target/classes:/mnt/c/Program Files/Java/lib" com.dabek.jakub.ezgui.App

all: java cpp

java:
	mkdir -p target/classes
	javac -cp "src/:/mnt/c/Program Files/Java/lib" src/com/dabek/jakub/ezgui/App.java -d target/classes/

cpp:
	cd src/RomanArabic && make program.exe

clean:
	rm -rf target
	cd src/RomanArabic && make clean

PHONY: test all java clean