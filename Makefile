JAVAC ?= javac
JAVA ?= java

SRC_DIR := src
BUILD_DIR := build/classes
MAIN_CLASS := main.Main
SOURCES := $(shell find $(SRC_DIR) -type f -name '*.java' -print)

.PHONY: all compile run clean

all: compile

compile:
	@mkdir -p $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $(SOURCES)

run: compile
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS)

clean:
	rm -rf build
	find $(SRC_DIR) -name '*.class' -delete