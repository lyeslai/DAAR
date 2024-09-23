package DAAR.tme1.RegEx;

import java.util.*;
public class RegExToNFA {


    private static int stateID = 0 ;

    public static NFA convert (RegExTree arbre) throws Exception {
        return buildNFA (arbre);
    }

    public static NFA buildNFA (RegExTree arbre) throws Exception {
        if (arbre == null) 
            throw new Exception ("Arbre nul");
        switch (arbre.root) {
            case RegEx.CONCAT:
                return concat(buildNFA(arbre.subTrees.get(0)), buildNFA(arbre.subTrees.get(1)));
            case RegEx.ALTERN:
                return altern(buildNFA(arbre.subTrees.get(0)), buildNFA(arbre.subTrees.get(1)));
            case RegEx.ETOILE:
                return etoile(buildNFA(arbre.subTrees.get(0)));
            case RegEx.DOT:
                return dot(); 
            default:
            if (arbre.root >= 0 && arbre.root <= 255) {
                return symbol((char) arbre.root);
            } else {
                throw new Exception("root: " + arbre.root);
            }

        } 

    }

    private static NFA symbol(char c) {
        NFAState start = new NFAState(stateID++);
        NFAState end = new NFAState(stateID++);
        end.findarbre =true;
        start.addtransition(c, end);
        Set<NFAState> states = new HashSet<>();
        states.add(start);
        states.add(end);
        return new NFA(start, states);
        }


//ANCIENNE FONCTION

    // private static NFA concat(NFA nfa, NFA nfa2) {
    //     nfa.states.forEach(s -> {
    //         if (s.findarbre) {
    //             s.findarbre =false;
    //             s.addepsilontransition(nfa2.startState);                
    //         }
    //     });
    //     nfa.states.addAll(nfa2.states);
    //     return new NFA(nfa.startState, nfa2.states);
    // }

//FONCTION GRACE A IA

    private static NFA concat(NFA nfa1, NFA nfa2) {
        Map<NFAState, NFAState> stateMap = new HashMap<>();
        Set<NFAState> newStates = new HashSet<>();
    
        // Cloner les états de nfa1
        for (NFAState state : nfa1.states) {
            NFAState clonedState = new NFAState(stateID++);
            clonedState.findarbre = state.findarbre;
            stateMap.put(state, clonedState);
            newStates.add(clonedState);
        }
    
        // Cloner les états de nfa2
        for (NFAState state : nfa2.states) {
            NFAState clonedState = new NFAState(stateID++);
            clonedState.findarbre = state.findarbre;
            stateMap.put(state, clonedState);
            newStates.add(clonedState);
        }
    
        // Recréer les transitions de nfa1
        for (NFAState state : nfa1.states) {
            NFAState clonedState = stateMap.get(state);
            for (Map.Entry<Character, Set<NFAState>> entry : state.transitions.entrySet()) {
                char symbol = entry.getKey();
                for (NFAState target : entry.getValue()) {
                    clonedState.addtransition(symbol, stateMap.get(target));
                }
            }
            for (NFAState target : state.epsilonTrans) {
                clonedState.addepsilontransition(stateMap.get(target));
            }
        }
    
        // Recréer les transitions de nfa2
        for (NFAState state : nfa2.states) {
            NFAState clonedState = stateMap.get(state);
            for (Map.Entry<Character, Set<NFAState>> entry : state.transitions.entrySet()) {
                char symbol = entry.getKey();
                for (NFAState target : entry.getValue()) {
                    clonedState.addtransition(symbol, stateMap.get(target));
                }
            }
            for (NFAState target : state.epsilonTrans) {
                clonedState.addepsilontransition(stateMap.get(target));
            }
        }
    
        // Ajouter les transitions ε entre les états finaux de nfa1 et l'état initial de nfa2
        for (NFAState state : nfa1.states) {
            if (state.findarbre) {
                NFAState clonedState = stateMap.get(state);
                clonedState.findarbre = false;
                clonedState.addepsilontransition(stateMap.get(nfa2.startState));
            }
        }
    
        NFAState newStartState = stateMap.get(nfa1.startState);
    
        return new NFA(newStartState, newStates);
    }




    private static NFA altern(NFA nfa, NFA nfa2) {
        NFAState start = new NFAState(stateID++);
        NFAState end = new NFAState(stateID++);
        end.findarbre=true;
        
        start.addepsilontransition(nfa.startState);
        start.addepsilontransition(nfa2.startState);

        nfa.states.forEach(s-> {
            if (s.findarbre) {
                s.findarbre =false;
                s.addepsilontransition(end);
            }
        });

        nfa2.states.forEach(s-> {
            if (s.findarbre) {
                s.findarbre =false;
                s.addepsilontransition(end);
            }
        });

        Set<NFAState> states = new HashSet<>();
        states.add(start);
        states.add(end);
        states.addAll(nfa.states);
        states.addAll(nfa2.states);
        return new NFA(start,states);
    }




    private static NFA dot() {
        NFAState start = new NFAState(stateID++);
        NFAState end = new NFAState(stateID++);
        end.findarbre=true;
        for (char c = 0 ; c < 256; c++ ){
            start.addtransition(c, start);
        }
        Set<NFAState> states = new HashSet<>();
        states.add(start);
        states.add(end);
        return new NFA(start, states);
    }




    private static NFA etoile(NFA nfa) {
        NFAState start = new NFAState(stateID++);
        NFAState end = new NFAState(stateID++);
        end.findarbre=true;

        start.addepsilontransition(nfa.startState);
        start.addepsilontransition(end);

        nfa.states.forEach(s -> {
            if (s.findarbre) {
                s.findarbre = false;
                s.addepsilontransition(nfa.startState);
                s.addepsilontransition(end);
            }
        });

        Set<NFAState> states = new HashSet<>();
        states.add(start);
        states.add(end);
        states.addAll(nfa.states);
        return new NFA(start, states);
    }



   

}
 

