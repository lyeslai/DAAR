package DAAR.tme1.RegEx;

import java.util.*;

public class DFAMinimizer {

    public static DFA minimize(DFA dfa) {
        Set<Character> alphabet = getAlphabet(dfa);
        Set<DFAState> allStates = dfa.states;

        // Étape 1 : Créer deux partitions initiales (états finaux et non finaux)
        Set<DFAState> finalStates = new HashSet<>();
        Set<DFAState> nonFinalStates = new HashSet<>();
        for (DFAState state : allStates) {
            if (state.isFinal) {
                finalStates.add(state);
            } else {
                nonFinalStates.add(state);
            }
        }

        List<Set<DFAState>> partitions = new ArrayList<>();
        if (!finalStates.isEmpty()) partitions.add(finalStates);
        if (!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);

        // Étape 2 : Affiner les partitions
        boolean changed;
        do {
            changed = false;
            List<Set<DFAState>> newPartitions = new ArrayList<>();
            for (Set<DFAState> partition : partitions) {
                Map<Map<Character, Integer>, Set<DFAState>> grouped = new HashMap<>();
                for (DFAState state : partition) {
                    Map<Character, Integer> signature = new HashMap<>();
                    for (char symbol : alphabet) {
                        DFAState target = state.transitions.get(symbol);
                        signature.put(symbol, getPartitionIndex(partitions, target));
                    }
                    grouped.computeIfAbsent(signature, k -> new HashSet<>()).add(state);
                }
                if (grouped.size() > 1) {
                    changed = true;
                    newPartitions.addAll(grouped.values());
                } else {
                    newPartitions.add(partition);
                }
            }
            partitions = newPartitions;
        } while (changed);

        // Étape 3 : Créer le DFA minimisé
        Map<Set<DFAState>, DFAState> newStateMap = new HashMap<>();
        Set<DFAState> minimizedStates = new HashSet<>();
        DFAState newStartState = null;

        // Créer les nouveaux états minimisés
        for (Set<DFAState> group : partitions) {
            DFAState newState = new DFAState(null, group.iterator().next().isFinal);
            newStateMap.put(group, newState);
            minimizedStates.add(newState);
            if (group.contains(dfa.startState)) {
                newStartState = newState;
            }
        }

        // Créer les transitions des nouveaux états
        for (Set<DFAState> group : partitions) {
            DFAState newState = newStateMap.get(group);
            DFAState representative = group.iterator().next();
            for (Map.Entry<Character, DFAState> entry : representative.transitions.entrySet()) {
                char symbol = entry.getKey();
                DFAState target = entry.getValue();
                Set<DFAState> targetGroup = findPartitionContainingState(partitions, target);
                newState.transitions.put(symbol, newStateMap.get(targetGroup));
            }
        }

        return new DFA(newStartState, minimizedStates);
    }

    private static int getPartitionIndex(List<Set<DFAState>> partitions, DFAState state) {
        for (int i = 0; i < partitions.size(); i++) {
            if (partitions.get(i).contains(state)) {
                return i;
            }
        }
        return -1; // État mort
    }

    private static Set<DFAState> findPartitionContainingState(List<Set<DFAState>> partitions, DFAState state) {
        for (Set<DFAState> partition : partitions) {
            if (partition.contains(state)) {
                return partition;
            }
        }
        return null;
    }

    private static Set<Character> getAlphabet(DFA dfa) {
        Set<Character> alphabet = new HashSet<>();
        for (DFAState state : dfa.states) {
            alphabet.addAll(state.transitions.keySet());
        }
        return alphabet;
    }
}
