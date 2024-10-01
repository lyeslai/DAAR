package DAAR.tme1.RegEx;

import java.util.HashSet;
import java.util.Set;

import DAAR.tme2.Kmp;

public class Benchmark1 {

    public static void main(String[] args) {
        // Textes de différentes tailles
        String smallText = generateText(1000);  // 1000 caractères
        String mediumText = generateText(100000);  // 100 000 caractères
        String largeText = generateText(1000000);  // 1 million de caractères
        
        // Motif à rechercher
        String pattern = "abc";

        // KMP Benchmark
        System.out.println("KMP Algorithm:");
        benchmarkKMP(smallText, mediumText, largeText, pattern);

        // Automate Benchmark
        System.out.println("Automate Algorithm:");
        benchmarkAutomate(smallText, mediumText, largeText, pattern);
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

    // Benchmark pour l'algorithme KMP
    public static void benchmarkKMP(String smallText, String mediumText, String largeText, String pattern) {
        long startTime, endTime;

        // Petit texte
        startTime = System.nanoTime();
        Kmp.KMPSearch(pattern, smallText);
        endTime = System.nanoTime();
        System.out.println("Small Text: " + (endTime - startTime) / 1000000.0 + " ms");

        // Texte moyen
        startTime = System.nanoTime();
        Kmp.KMPSearch(pattern, mediumText);
        endTime = System.nanoTime();
        System.out.println("Medium Text: " + (endTime - startTime) / 1000000.0 + " ms");

        // Grand texte
        startTime = System.nanoTime();
        Kmp.KMPSearch(pattern, largeText);
        endTime = System.nanoTime();
        System.out.println("Large Text: " + (endTime - startTime) / 1000000.0 + " ms");
    }

    // Benchmark pour l'algorithme basé sur les automates
    public static void benchmarkAutomate(String smallText, String mediumText, String largeText, String pattern) {
        long startTime, endTime;

        // Créer un automate pour le motif
        RegExTree regExTree;
        try {
            regExTree = RegExParser.parse(pattern);
            NFA nfa = RegExToNFA.convert(regExTree);
            DFA dfa = NFAToDFA.convertToDFA(nfa, collectAlphabet(pattern));

            // Petit texte
            startTime = System.nanoTime();
            runAutomateSearch(dfa, smallText);
            endTime = System.nanoTime();
            System.out.println("Small Text: " + (endTime - startTime) / 1000000.0 + " ms");

            // Texte moyen
            startTime = System.nanoTime();
            runAutomateSearch(dfa, mediumText);
            endTime = System.nanoTime();
            System.out.println("Medium Text: " + (endTime - startTime) / 1000000.0 + " ms");

            // Grand texte
            startTime = System.nanoTime();
            runAutomateSearch(dfa, largeText);
            endTime = System.nanoTime();
            System.out.println("Large Text: " + (endTime - startTime) / 1000000.0 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
