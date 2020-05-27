.PHONY: all clean

all: pom.xml src/main/java/*.java
	mvn -q clean javafx:run
