package DAAR.tme1.RegEx;

import java.util.*;

class RegExTree {
    protected int root;
    protected ArrayList<RegExTree> subTrees;



    public RegExTree(int root, ArrayList<RegExTree> subTrees) {
      this.root = root;
      this.subTrees = subTrees;
    }



    public void collectAlphabet(Set<Character> alphabet) {
      // Si le nœud est un opérateur (CONCAT, ETOILE, ALTERN), on ne l'ajoute pas
      if (root == RegEx.CONCAT || root == RegEx.ETOILE || root == RegEx.ALTERN || root == RegEx.DOT) {
          // Parcourir les sous-arbres
          for (RegExTree subtree : subTrees) {
              subtree.collectAlphabet(alphabet);  // Récursivement collecter l'alphabet
          }
      } else {
          // Sinon, c'est un caractère littéral (feuille), on l'ajoute à l'alphabet
          alphabet.add((char) root);
      }
  }
    //FROM TREE TO PARENTHESIS
    public String toString() {
      if (subTrees.isEmpty()) return rootToString();
      String result = rootToString()+"("+subTrees.get(0).toString();
      for (int i=1;i<subTrees.size();i++) result+=","+subTrees.get(i).toString();
      return result+")";
    }

    
    private String rootToString() {
      if (root==RegEx.CONCAT) return ".";
      if (root==RegEx.ETOILE) return "*";
      if (root==RegEx.ALTERN) return "|";
      if (root==RegEx.DOT) return ".";
      return Character.toString((char)root);
    }
}