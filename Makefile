JAVAC = javac
JAVA = java
SRC = src
MODEL = $(SRC)/model/*.java
EVAL = $(SRC)/eval/*.java
SIM = $(SRC)/sim/*.java
MAIN = $(SRC)/main/Main.java

all:
	javac --module-path /Applications/javafx-sdk-26.0.2/lib --add-modules javafx.controls src/model/*.java src/eval/*.java src/sim/*.java src/gui/*.java src/main/Main.java
	java --module-path /Applications/javafx-sdk-26.0.2/lib --add-modules javafx.controls -cp src gui.Gui

run: all
	$(JAVA) -cp $(SRC) main.Main
	$(JAVA) -cp $(SRC) gui.GUI

clean:
	find . -name "*.class" -delete