package DAAR.tme1.RegEx;

import java.util.*;

class DFAState {
    Set<NFAState> nfaStates; // Ensemble des états NFA contenus dans cet état DFA
    Map<Character, DFAState> transitions; // Transitions DFA
    boolean isFinal; // Est-ce un état final ?

    public DFAState(Set<NFAState> nfaStates, boolean isFinal) {
        this.nfaStates = nfaStates;
        this.transitions = new HashMap<>();
        this.isFinal = isFinal;
    }
}

public class DFA {
    DFAState startState;
    Set<DFAState> states;

    public DFA(DFAState startState, Set<DFAState> states) {
        this.startState = startState;
        this.states = states;
    }

    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph DFA {\n");
        sb.append("rankdir=LR;\n");
        sb.append("node [shape=circle];\n");

        // Marquer l'état initial
        sb.append("start [shape=point];\n");
        sb.append("start -> ").append(System.identityHashCode(startState)).append(";\n");

        // Parcourir les états
        for (DFAState state : states) {
            if (state.isFinal) {
                sb.append(System.identityHashCode(state)).append(" [shape=doublecircle];\n");
            }

            // Transitions par symbole
            for (Map.Entry<Character, DFAState> entry : state.transitions.entrySet()) {
                char symbol = entry.getKey();
                DFAState target = entry.getValue();
                sb.append(System.identityHashCode(state)).append(" -> ")
                  .append(System.identityHashCode(target)).append(" [label=\"")
                  .append(escapeSymbol(symbol)).append("\"];\n");
            }
        }

        sb.append("}\n");
        return sb.toString();
    }

    private String escapeSymbol(char symbol) {
        if (symbol == '"') {
            return "\\\"";
        } else if (symbol == '\\') {
            return "\\\\";
        } else if (symbol == '\n') {
            return "\\n";
        } else if (symbol == '\r') {
            return "\\r";
        } else if (symbol == '\t') {
            return "\\t";
        } else if (Character.isISOControl(symbol)) {
            return String.format("\\x%02X", (int) symbol);
        } else {
            return Character.toString(symbol);
        }
    }
}
