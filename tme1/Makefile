# Variables
SRC_DIR = 
BIN_DIR = bin
CLASSES = RegEx/*.java 
MAIN_CLASS = DAAR.tme1.RegEx.RegEx
BOOKS_DIR = Books
JAVA = java
JAVAC = javac

# Compilation
all: $(BIN_DIR)/classes

$(BIN_DIR)/classes: $(CLASSES)
	mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(CLASSES)

# tester l'implementation automate 
run_automate:
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

#tester l'implementation KMP
run_kmp:
	$(JAVA) -cp $(BIN_DIR) DAAR.tme1.RegEx.RegExKMP

# Exécution du benchmark3
run_benchmark3:
	$(JAVA) -cp $(BIN_DIR) DAAR.tme1.RegEx.Benchmark3

# Exécution du benchmark2
run_benchmark2:
	$(JAVA) -cp $(BIN_DIR) DAAR.tme1.RegEx.Benchmark2

# Exécution du benchmark1
run_benchmark1:
	$(JAVA) -cp $(BIN_DIR) DAAR.tme1.RegEx.Benchmark1

# Nettoyage
clean:
	rm -rf $(BIN_DIR)

# Nettoyage des fichiers .class et .dot
clean_all:
	rm -rf $(BIN_DIR)/*.class *.dot *.png