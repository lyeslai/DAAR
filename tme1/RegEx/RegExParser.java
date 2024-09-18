package DAAR.tme1.RegEx;

import java.util.*;
public class RegExParser {
    static final int CONCAT = RegEx.CONCAT;
    static final int ETOILE = RegEx.ETOILE;
    static final int ALTERN = RegEx.ALTERN;
    static final int PROTECTION = RegEx.PROTECTION;

    static final int PARENTHESEFERMANT = RegEx.PARENTHESEFERMANT;
    static final int PARENTHESEOUVRANT = RegEx.PARENTHESEOUVRANT;
    static final int DOT = RegEx.DOT;

        
//FROM REGEX TO SYNTAX TREE 

    public static RegExTree parse(String regEx) throws Exception {

    //BEGIN DEBUG: set conditionnal to true for debug example
    // if (false) throw new Exception();
    // RegExTree example = regEx.exampleAhoUllman();
    // if (false) return example;
    //END DEBUG

    ArrayList<RegExTree> result = new ArrayList<RegExTree>();
    for (int i=0;i<regEx.length();i++) result.add(new RegExTree(charToRoot(regEx.charAt(i)),new ArrayList<RegExTree>()));
    
    return parse(result);
  }


  private static int charToRoot(char c) {
    if (c=='.') return DOT;
    if (c=='*') return ETOILE;
    if (c=='|') return ALTERN;
    if (c=='(') return PARENTHESEOUVRANT;
    if (c==')') return PARENTHESEFERMANT;
    return (int) c;
  }

    private static RegExTree parse(ArrayList<RegExTree> result) throws Exception {
        while (containParenthese(result))
            result = processParenthese(result);
        while (containEtoile(result))
            result = processEtoile(result);
        while (containConcat(result))
            result = processConcat(result);
        while (containAltern(result))
            result = processAltern(result);

        if (result.size() > 1)
            throw new Exception();

        return removeProtection(result.get(0));
    }

    private static boolean containParenthese(ArrayList<RegExTree> trees) {
        for (RegExTree t : trees)
            if (t.root == PARENTHESEFERMANT || t.root == PARENTHESEOUVRANT)
                return true;
        return false;
    }

    private static ArrayList<RegExTree> processParenthese(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;

        for (RegExTree t : trees) {

            if (!found && t.root == PARENTHESEFERMANT) {
                boolean done = false;
                ArrayList<RegExTree> content = new ArrayList<RegExTree>();

                while (!done && !result.isEmpty())

                    if (result.get(result.size() - 1).root == PARENTHESEOUVRANT) {
                        done = true;
                        result.remove(result.size() - 1);
                    } else
                        content.add(0, result.remove(result.size() - 1));

                if (!done)
                    throw new Exception();

                found = true;

                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(parse(content));
                result.add(new RegExTree(PROTECTION, subTrees));
            } else {
                result.add(t);
            }
        }

        if (!found)
            throw new Exception();
        return result;
    }

    private static boolean containEtoile(ArrayList<RegExTree> trees) {
        for (RegExTree t : trees)

            if (t.root == ETOILE && t.subTrees.isEmpty())
                return true;

        return false;
    }

    private static ArrayList<RegExTree> processEtoile(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;

        for (RegExTree t : trees) {
            if (!found && t.root == ETOILE && t.subTrees.isEmpty()) {
                if (result.isEmpty())
                    throw new Exception();
                found = true;
                RegExTree last = result.remove(result.size() - 1);
                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(last);
                result.add(new RegExTree(ETOILE, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }

    private static boolean containConcat(ArrayList<RegExTree> trees) {
        boolean firstFound = false;

        for (RegExTree t : trees) {
            if (!firstFound && t.root != ALTERN && t.root != PARENTHESEFERMANT && t.root != PARENTHESEOUVRANT) {
                firstFound = true;
                continue;
            }

            if (firstFound)
                if (t.root != ALTERN && t.root != PARENTHESEFERMANT && t.root != PARENTHESEOUVRANT)
                    return true;
                else
                    firstFound = false;
        }
        return false;
    }

    private static ArrayList<RegExTree> processConcat(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;
        boolean firstFound = false;
        for (RegExTree t : trees) {
            if (!found && !firstFound && t.root != ALTERN) {
                firstFound = true;
                result.add(t);
                continue;
            }
            if (!found && firstFound && t.root == ALTERN) {
                firstFound = false;
                result.add(t);
                continue;
            }
            if (!found && firstFound && t.root != ALTERN) {
                found = true;
                RegExTree last = result.remove(result.size() - 1);
                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(last);
                subTrees.add(t);
                result.add(new RegExTree(CONCAT, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }

    private static boolean containAltern(ArrayList<RegExTree> trees) {
        for (RegExTree t : trees)
            if (t.root == ALTERN && t.subTrees.isEmpty())
                return true;
        return false;
    }

    private static ArrayList<RegExTree> processAltern(ArrayList<RegExTree> trees) throws Exception {
        ArrayList<RegExTree> result = new ArrayList<RegExTree>();
        boolean found = false;
        RegExTree gauche = null;
        boolean done = false;
        for (RegExTree t : trees) {
            if (!found && t.root == ALTERN && t.subTrees.isEmpty()) {
                if (result.isEmpty())
                    throw new Exception();
                found = true;
                gauche = result.remove(result.size() - 1);
                continue;
            }
            if (found && !done) {
                if (gauche == null)
                    throw new Exception();
                done = true;
                ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
                subTrees.add(gauche);
                subTrees.add(t);
                result.add(new RegExTree(ALTERN, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }

    private static RegExTree removeProtection(RegExTree tree) throws Exception {
        if (tree.root == PROTECTION && tree.subTrees.size() != 1)
            throw new Exception();
        if (tree.subTrees.isEmpty())
            return tree;
        if (tree.root == PROTECTION)
            return removeProtection(tree.subTrees.get(0));

        ArrayList<RegExTree> subTrees = new ArrayList<RegExTree>();
        for (RegExTree t : tree.subTrees)
            subTrees.add(removeProtection(t));
        return new RegExTree(tree.root, subTrees);
    }
}
    
