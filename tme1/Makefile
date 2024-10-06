# Variables
JAVAC = javac
JAVA = java
SRC_DIR = RegEx
BIN_DIR = bin

# Liste des fichiers sources
SOURCES := $(wildcard $(SRC_DIR)/*.java)
# Liste des fichiers objets
CLASSES := $(patsubst $(SRC_DIR)/%.java, $(BIN_DIR)/%.class, $(SOURCES))

# Par défaut, compile tout le projet
all: $(CLASSES)

# Règle pour compiler les fichiers .java en fichiers .class
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $<

# Exécuter une classe principale spécifique
run: MAIN_CLASS=RegEx
run: all
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

# Exécuter Main1
run1: MAIN_CLASS=Benchmark1
run1: run

# Exécuter Main2
run2: MAIN_CLASS=Benchmark1
run2: run

# Nettoyer les fichiers compilés
clean:
	rm -rf $(BIN_DIR)/*.class

# Indiquer que ces cibles ne sont pas des fichiers
.PHONY: all run run1 run2 clean
