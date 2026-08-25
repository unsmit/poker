JAVAC = javac
JAVA = java
SRC = src
MODEL = $(SRC)/model/*.java
EVAL = $(SRC)/eval/*.java
MAIN = $(SRC)/main/Main.java

all:
	$(JAVAC) $(MODEL) $(EVAL) $(MAIN)

run: all
	$(JAVA) -cp $(SRC) main.Main

clean:
	find . -name "*.class" -delete