package DAAR.tme1.RegEx;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class RegEx {
    // MACROS
    static final int CONCAT = 0xC04CA7;
    static final int ETOILE = 0xE7011E;
    static final int ALTERN = 0xA17E54;
    static final int PROTECTION = 0xBADDAD;
    static final int PARENTHESEOUVRANT = 0x16641664;
    static final int PARENTHESEFERMANT = 0x51515151;
    static final int DOT = 0xD07;

    // REGEX
    private static String regEx;
    public static int nbLigne = 0; 
    public RegEx() {}

    public static void main(String[] args) {
        // Utiliser un scanner pour obtenir les entrées utilisateur
        Scanner scanner = new Scanner(System.in);

        // Demander l'expression régulière
        System.out.print(">> Veuillez entrer une expression régulière: ");
        regEx = scanner.nextLine();

        // Demander le chemin du fichier texte
        System.out.print(">> Veuillez entrer le chemin vers le fichier texte: ");
        String filePath = scanner.nextLine();

        Set<Character> alphabet = new HashSet<>();

        System.out.println(">> Expression régulière : \"" + regEx + "\".");
        System.out.println(">> ...");

        if (regEx.length() < 1) {
            System.err.println(">> ERREUR: expression régulière vide.");
        } else {
            try {
                // Étape 1: Analyse syntaxique
                RegExTree regExTree = RegExParser.parse(regEx);
                System.out.println(">> Arbre syntaxique construit: " + regExTree.toString() + ".");

                // Étape 2: Conversion en NFA
                NFA nfa = RegExToNFA.convert(regExTree);
                System.out.println(">> NFA construit.");

                // Générer le fichier DOT pour le NFA
                String nfaDot = nfa.toDot();
                Files.write(Paths.get("nfa.dot"), nfaDot.getBytes());
                System.out.println(">> Fichier 'nfa.dot' généré pour le NFA.");

                // Étape 3: Conversion en DFA
                regExTree.collectAlphabet(alphabet);
                DFA dfa = NFAToDFA.convertToDFA(nfa, alphabet);
                System.out.println(">> DFA construit.");

                // Générer le fichier DOT pour le DFA
                String dfaDot = dfa.toDot();
                Files.write(Paths.get("dfa.dot"), dfaDot.getBytes());
                System.out.println(">> Fichier 'dfa.dot' généré pour le DFA.");

                // Étape 4: Minimisation du DFA
                DFA minimizedDFA = DFAMinimizer.minimize(dfa);
                System.out.println(">> DFA minimisé.");

                // Générer le fichier DOT pour le DFA minimisé
                String minimizedDfaDot = minimizedDFA.toDot();
                Files.write(Paths.get("minimized_dfa.dot"), minimizedDfaDot.getBytes());
                System.out.println(">> Fichier 'minimized_dfa.dot' généré pour le DFA minimisé.");

                // Exécution des commandes dot pour générer les images (optionnel)
                try {
                    runDotCommand("nfa.dot", "nfa.png");
                    System.out.println(">> Image 'nfa.png' générée pour le NFA.");

                    runDotCommand("dfa.dot", "dfa.png");
                    System.out.println(">> Image 'dfa.png' générée pour le DFA.");

                    runDotCommand("minimized_dfa.dot", "minimized_dfa.png");
                    System.out.println(">> Image 'minimized_dfa.png' générée pour le DFA minimisé.");

                } catch (IOException | InterruptedException e) {
                    System.err.println("Erreur lors de la génération des images avec dot: " + e.getMessage());
                    e.printStackTrace();
                }

                // Étape 5: Lire le fichier texte et afficher les lignes correspondantes
                matchLinesInFile(filePath, minimizedDFA);

            } catch (Exception e) {
                System.err.println(">> ERREUR: erreur de syntaxe pour l'expression régulière \"" + regEx + "\".");
                e.printStackTrace();
            }
        }

        System.out.println(">> Analyse terminée.");
        System.out.println("Au revoir.");
    }

// Fonction pour lire un fichier ligne par ligne et vérifier si une ligne contient des mots qui correspondent à l'expression régulière
// Fonction pour lire un fichier ligne par ligne et vérifier si une sous-chaîne correspond à l'expression régulière
private static void matchLinesInFile(String filePath, DFA dfa) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            
            // Trouver toutes les sous-chaînes correspondantes dans la ligne
            boolean foundMatch = false;
            StringBuilder matchedSubstrings = new StringBuilder();

            for (int i = 0; i < line.length(); i++) {
                for (int j = i + 1; j <= line.length(); j++) {
                    String substring = line.substring(i, j);
                    if (matches(substring, dfa)) {
                        matchedSubstrings.append(substring).append(" ");
                        foundMatch = true;
                    }
                }
            }

            // Si des sous-chaînes correspondent, afficher la ligne avec les sous-chaînes correspondantes
            if (foundMatch) {
                System.out.println("Ligne " + lineNumber + " : " + line);
                System.out.println("Sous-chaînes correspondantes : " + matchedSubstrings.toString());
            }
        }
    } catch (Error e) {
        System.out.println("message d'erreur : " + e.getMessage());
    }
}

// Fonction pour vérifier si une sous-chaîne correspond au DFA
private static boolean matches(String input, DFA dfa) {
    DFAState currentState = dfa.startState;

    // Parcourir chaque caractère de la sous-chaîne
    for (int i = 0; i < input.length(); i++) {
        char c = input.charAt(i);

        // Transition vers l'état suivant
        DFAState nextState = currentState.transitions.get(c);

        if (nextState == null) {
            return false; // Si aucune transition trouvée, retourner faux
        }

        currentState = nextState;
    }

    // Retourner vrai si on est dans un état final à la fin de la sous-chaîne
    return currentState.isFinal;
}


    // Fonction pour exécuter la commande dot et générer l'image correspondante
    private static void runDotCommand(String inputDot, String outputPng) throws IOException, InterruptedException {
        @SuppressWarnings("deprecation")
        Process process = Runtime.getRuntime().exec("dot -Tpng " + inputDot + " -o " + outputPng);
        process.waitFor();
    }
}
