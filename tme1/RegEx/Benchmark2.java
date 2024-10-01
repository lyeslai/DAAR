package DAAR.tme1.RegEx;

import java.util.*;

import DAAR.tme2.Kmp;

import java.io.*;

public class Benchmark2 {

    public static void main(String[] args) throws IOException {
        // Différentes tailles de texte
        int[] textSizes = {1000, 100000, 1000000};
        
        // Motifs à tester
        String[] patterns = {"abc", "a(b|c)*de", "a(b|c)*d(e|f){2,}g"};

        // Stocker les résultats de benchmark pour chaque algorithme
        List<Map<String, Double>> kmpResults = new ArrayList<>();
        List<Map<String, Double>> automateResults = new ArrayList<>();

        for (int size : textSizes) {
            String text = generateText(size);
            System.out.println("Running benchmarks for text size: " + size);
            
            // Stocker les temps pour chaque motif pour KMP
            Map<String, Double> kmpBenchmark = new HashMap<>();
            // Stocker les temps pour chaque motif pour Automates
            Map<String, Double> automateBenchmark = new HashMap<>();

            for (String pattern : patterns) {
                // Temps pour KMP
                double kmpTime = benchmarkKMP(text, pattern);
                kmpBenchmark.put(pattern, kmpTime);

                // Temps pour Automates
                double automateTime = benchmarkAutomate(text, pattern);
                automateBenchmark.put(pattern, automateTime);
            }

            kmpResults.add(kmpBenchmark);
            automateResults.add(automateBenchmark);
        }

        // Sauvegarder les résultats dans un fichier CSV pour les graphiques
        saveResultsToCSV("kmp_results.csv", kmpResults, textSizes, patterns);
        saveResultsToCSV("automate_results.csv", automateResults, textSizes, patterns);
    }

    // Méthode pour générer du texte aléatoire de taille donnée
    public static String generateText(int size) {
        StringBuilder sb = new StringBuilder(size);
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < size; i++) {
            sb.append(alphabet.charAt((int)(Math.random() * alphabet.length())));
        }
        return sb.toString();
    }

    // Méthode pour effectuer le benchmark avec KMP
    public static double benchmarkKMP(String text, String pattern) {
        long startTime = System.nanoTime();
        Kmp.KMPSearch(pattern, text);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1e6; // Convertir en millisecondes
    }

    // Méthode pour effectuer le benchmark avec Automates
    public static double benchmarkAutomate(String text, String pattern) {
        try {
            RegExTree regExTree = RegExParser.parse(pattern);
            NFA nfa = RegExToNFA.convert(regExTree);
            DFA dfa = NFAToDFA.convertToDFA(nfa, collectAlphabet(pattern));

            long startTime = System.nanoTime();
            runAutomateSearch(dfa, text);
            long endTime = System.nanoTime();
            return (endTime - startTime) / 1e6; // Convertir en millisecondes
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Fonction pour exécuter la recherche de motif avec un automate
    public static void runAutomateSearch(DFA dfa, String text) {
        for (int i = 0; i < text.length(); i++) {
            String substring = text.substring(i);
            if (matches(substring, dfa)) {
                // Vous pouvez aussi collecter les positions de correspondance ici
            }
        }
    }

    // Fonction pour vérifier si un mot correspond à l'automate DFA
    public static boolean matches(String input, DFA dfa) {
        DFAState currentState = dfa.startState;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            DFAState nextState = currentState.transitions.get(c);

            if (nextState == null) {
                return false;
            }
            currentState = nextState;
        }

        return currentState.isFinal;
    }

    // Fonction pour collecter l'alphabet à partir du motif
    public static Set<Character> collectAlphabet(String pattern) {
        Set<Character> alphabet = new HashSet<>();
        for (char c : pattern.toCharArray()) {
            alphabet.add(c);
        }
        return alphabet;
    }

    // Sauvegarder les résultats dans un fichier CSV
    public static void saveResultsToCSV(String filename, List<Map<String, Double>> results, int[] sizes, String[] patterns) throws IOException {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Text Size");
            for (String pattern : patterns) {
                sb.append(",").append(pattern);
            }
            sb.append("\n");

            for (int i = 0; i < sizes.length; i++) {
                sb.append(sizes[i]);
                Map<String, Double> benchmark = results.get(i);
                for (String pattern : patterns) {
                    sb.append(",").append(benchmark.get(pattern));
                }
                sb.append("\n");
            }

            writer.write(sb.toString());
        }
    }
}
