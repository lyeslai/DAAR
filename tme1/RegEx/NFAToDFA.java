package DAAR.tme1.RegEx;

import java.util.*;




public class NFAToDFA {

    public static DFA convertToDFA(NFA nfa, Set<Character> alphabet) {
        Queue<Set<NFAState>> queue = new LinkedList<>();
        Map<Set<NFAState>, DFAState> nfaToDfaMap = new HashMap<>();
        Set<DFAState> dfaStates = new HashSet<>();

        // Calcul de la clôture epsilon de l'état initial
        Set<NFAState> startNFAStates = epsilonClosure(Collections.singleton(nfa.startState));
        DFAState startDFAState = new DFAState(startNFAStates, isFinalState(startNFAStates));
        dfaStates.add(startDFAState);
        nfaToDfaMap.put(startNFAStates, startDFAState);
        queue.add(startNFAStates);

        while (!queue.isEmpty()) {
            Set<NFAState> currentNFAStates = queue.poll();
            DFAState currentDFAState = nfaToDfaMap.get(currentNFAStates);

            // Pour chaque symbole de l'alphabet
            for (char symbol : alphabet) {
                Set<NFAState> reachableNFAStates = new HashSet<>();
                for (NFAState nfaState : currentNFAStates) {
                    // Si une transition existe pour le symbole
                    if (nfaState.transitions.containsKey(symbol)) {
                        for (NFAState target : nfaState.transitions.get(symbol)) {
                            reachableNFAStates.addAll(epsilonClosure(Collections.singleton(target)));
                        }
                    }
                }

                // Si on trouve de nouveaux états atteignables
                if (!reachableNFAStates.isEmpty()) {
                    if (!nfaToDfaMap.containsKey(reachableNFAStates)) {
                        DFAState newDFAState = new DFAState(reachableNFAStates, isFinalState(reachableNFAStates));
                        nfaToDfaMap.put(reachableNFAStates, newDFAState);
                        dfaStates.add(newDFAState);
                        queue.add(reachableNFAStates);
                    }
                    currentDFAState.transitions.put(symbol, nfaToDfaMap.get(reachableNFAStates));
                }
            }
        }

        return new DFA(startDFAState, dfaStates);
    }

    // Calcul de la clôture epsilon pour un ensemble d'états NFA
    // permet de minimiser le dfa 
    private static Set<NFAState> epsilonClosure(Set<NFAState> nfaStates) {
        Set<NFAState> closure = new HashSet<>(nfaStates);
        Deque<NFAState> stack = new ArrayDeque<>(nfaStates);

        while (!stack.isEmpty()) {
            NFAState state = stack.pop();
            for (NFAState epsilonTarget : state.epsilonTrans) {
                if (!closure.contains(epsilonTarget)) {
                    closure.add(epsilonTarget);
                    stack.add(epsilonTarget);
                }
            }
        }

        return closure;
    }

    // Vérifie si un ensemble d'états contient un état final
    private static boolean isFinalState(Set<NFAState> nfaStates) {
        for (NFAState state : nfaStates) {
            if (state.findarbre) {
                return true;
            }
        }
        return false;
    }
}