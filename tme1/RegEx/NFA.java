package DAAR.tme1.RegEx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class NFAState {
    int id;
    boolean findarbre;
    Map<Character, Set<NFAState>> transitions;
    Set<NFAState> epsilonTrans;


    public NFAState(int id) {
        this.id= id;
        this.findarbre =false;
        this.transitions = new HashMap<>();
        this.epsilonTrans = new HashSet<>();
    }

    public void addtransition(char symbol, NFAState state ) {
        //computeifabsent : Elle sert à insérer une valeur 
        //dans la map pour une clé donnée seulement
        // si cette clé n'est pas déjà associée à une valeur
        transitions.computeIfAbsent(symbol,k-> new HashSet<>()).add(state);   
    }
    public void addepsilontransition(NFAState state) {
        epsilonTrans.add(state);
    }

}
public class NFA {
    NFAState startState;
    Set<NFAState> states;

    public NFA(NFAState startState, Set<NFAState> states) {
        this.startState = startState;
        this.states = states;
    }
    

    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph NFA {\n");
        sb.append("rankdir=LR;\n");
        sb.append("node [shape=circle];\n");

        // Marquer l'état initial
        sb.append("start [shape=point];\n");
        sb.append("start -> ").append(startState.id).append(";\n");

        // Parcourir les états
        for (NFAState state : states) {
            if (state.findarbre) {
                sb.append(state.id).append(" [shape=doublecircle];\n");
            }

            // Transitions par symbole
            for (Map.Entry<Character, Set<NFAState>> entry : state.transitions.entrySet()) {
                char symbol = entry.getKey();
                for (NFAState target : entry.getValue()) {
                    sb.append(state.id).append(" -> ").append(target.id)
                      .append(" [label=\"").append(escapeSymbol(symbol)).append("\"];\n");
                }
            }

            // Transitions epsilon
            for (NFAState target : state.epsilonTrans) {
                sb.append(state.id).append(" -> ").append(target.id)
                  .append(" [label=\"ε\"];\n");
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
