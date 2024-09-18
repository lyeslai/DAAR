package DAAR.tme1.RegEx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NFA {
    int id;
    boolean findarbre;
    Map<Character, Set<NFA>> transitions;
    Set<NFA> epsilonTrans;


    public NFA(int id) {
        this.id= id;
        this.findarbre =false;
        this.transitions = new HashMap<>();
        this.epsilonTrans = new HashSet<>();
    }
}
