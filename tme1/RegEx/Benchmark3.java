package DAAR.tme1.RegEx;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import DAAR.tme2.Kmp;

public class Benchmark3 {

    public static void main(String[] args) throws IOException {
        // Répertoire contenant les fichiers du projet Gutenberg
        String directoryPath = "/home/lyeslai/Documents/DAAR/tme1/Books/";

        // Liste des mots les plus utilisés en anglais
        List<String> commonWords = Arrays.asList(
            "the", "of", "and", "to", "a", "in", "is", "it", "you", "that", "he", "was", "for", 
            "on", "are", "with", "as", "I", "his", "they", "be"
        );

        // Récupérer tous les fichiers du répertoire
        List<Path> allFiles = new ArrayList<>(Files.list(Paths.get(directoryPath))
                                    .filter(Files::isRegularFile)
                                    .toList());  // Convertir en ArrayList modifiable

        // Limiter à un échantillon de 30 fichiers
        Collections.shuffle(allFiles); // Mélanger aléatoirement
        List<Path> selectedFiles = allFiles.subList(0, Math.min(1, allFiles.size()));

        // Stocker les résultats du benchmark pour chaque algorithme
        List<Map<String, Double>> kmpResults = new ArrayList<>();
        List<Map<String, Double>> automateResults = new ArrayList<>();

        // Parcourir les fichiers sélectionnés
        for (Path filePath : selectedFiles) {
            String text = new String(Files.readAllBytes(filePath));
            System.out.println("Processing file: " + filePath.getFileName());

            // Stocker les temps pour chaque mot courant pour KMP
            Map<String, Double> kmpBenchmark = new HashMap<>();
            // Stocker les temps pour chaque mot courant pour Automates
            Map<String, Double> automateBenchmark = new HashMap<>();

            // Parcourir chaque mot de la liste des mots courants
            for (String word : commonWords) {
                // Benchmark KMP
                double kmpTime = benchmarkKMP(text, word);
                kmpBenchmark.put(word, kmpTime);

                // // Benchmark Automate
                // double automateTime = benchmarkAutomate(text, word);
                // automateBenchmark.put(word, automateTime);
            }

            kmpResults.add(kmpBenchmark);
            // automateResults.add(automateBenchmark);
        }

        // Sauvegarder les résultats dans des fichiers CSV pour analyse
        saveResultsToCSV("kmp_results.csv", kmpResults, commonWords, selectedFiles);
        // saveResultsToCSV("automate_results.csv", automateResults, commonWords, selectedFiles);
    }

    // Méthode pour effectuer le benchmark avec KMP
    public static double benchmarkKMP(String text, String pattern) {
        long startTime = System.nanoTime();
        Kmp.KMPSearch(pattern, text); // Appel à l'algorithme KMP
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1e6; // Convertir en millisecondes
    }

    // Méthode pour effectuer le benchmark avec Automates
    // public static double benchmarkAutomate(String text, String pattern) {
    //     try {
    //         RegExTree regExTree = RegExParser.parse(pattern); // Construire l'arbre RegEx
    //         NFA nfa = RegExToNFA.convert(regExTree); // Convertir en NFA
    //         DFA dfa = NFAToDFA.convertToDFA(nfa, collectAlphabet(pattern)); // Convertir en DFA

    //         long startTime = System.nanoTime();
    //         runAutomateSearch(dfa, text); // Effectuer la recherche avec l'automate
    //         long endTime = System.nanoTime();
    //         return (endTime - startTime) / 1e6; // Convertir en millisecondes
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return 0;
    // }

    // Fonction pour exécuter la recherche de motif avec un automate
    // public static void runAutomateSearch(DFA dfa, String text) {
    //     for (int i = 0; i < text.length(); i++) {
    //         String substring = text.substring(i);
    //         if (matches(substring, dfa)) {
    //             // Vous pouvez aussi collecter les positions de correspondance ici
    //         }
    //     }
    // }

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
    public static void saveResultsToCSV(String filename, List<Map<String, Double>> results, List<String> patterns, List<Path> files) throws IOException {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            StringBuilder sb = new StringBuilder();
            sb.append("File,Pattern");
            for (String pattern : patterns) {
                sb.append(",").append(pattern);
            }
            sb.append("\n");

            for (int i = 0; i < files.size(); i++) {
                Path filePath = files.get(i);
                Map<String, Double> benchmark = results.get(i);

                sb.append(filePath.getFileName());
                for (String pattern : patterns) {
                    sb.append(",").append(benchmark.get(pattern));
                }
                sb.append("\n");
            }

            writer.write(sb.toString());
        }
    }
}
