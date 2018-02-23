test: all
	java Program -24 ggh \"Hello world\" 56

all: Program.class

Program.class: Program.java
	javac Program.java

clean:
	rm -f Program.class

PHONY: all clean