//  package DAAR.tme1.RegEx;

// import java.util.Scanner;
// import java.util.ArrayList;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.io.IOException;
// import java.lang.Exception;

// public class RegEx {
//   //MACROS
//   static final int CONCAT = 0xC04CA7;
//   static final int ETOILE = 0xE7011E;
//   static final int ALTERN = 0xA17E54;
//   static final int PROTECTION = 0xBADDAD;

//   static final int PARENTHESEOUVRANT = 0x16641664;
//   static final int PARENTHESEFERMANT = 0x51515151;
//   static final int DOT = 0xD07;
  
//   //REGEX
//   private static String regEx;
  
//   //CONSTRUCTOR
//   public RegEx(){}

//   //MAIN
//   public static void main(String arg[], RegExTree arbre) {
//     System.out.println("Bienvenue dans le nouveau egrep");
//     if (arg.length!=0) {
//       regEx = arg[0];
//     } else {
//       Scanner scanner = new Scanner(System.in);
//       System.out.print("  >> Please enter a regEx: ");
//       regEx = scanner.next();
//     }

    
//     if (regEx.length()<1) {
//       System.err.println("  >> ERROR: empty regEx.");
//     } else {
//       try {
//         RegExTree ret = RegExParser.parse(regEx);
//         System.out.println("  >> Tree result: "+ret.toString()+".");

//         NFA nfa = RegExToNFA.convert(arbre);
//         System.out.println(">> NFA construit.");

//         String nfaDot = nfa.toDot();
//         Files.write(Paths.get("nfa.dot"), nfaDot.getBytes());
//         System.out.println(">> Fichier 'nfa.dot' généré pour le NFA.");
        
//         try {
//           // Générer l'image du NFA
//           @SuppressWarnings("deprecation")
//           Process p1 = Runtime.getRuntime().exec("dot -Tpng nfa.dot -o nfa.png");
//           p1.waitFor();
//           System.out.println(">> Image 'nfa.png' générée pour le NFA.");
//         } catch (IOException | InterruptedException e) {
//           e.printStackTrace();
//       }
        
//       } catch (Exception e) {
//         System.err.println("  >> ERROR: syntax error for regEx \""+regEx+"\".");
//       }
//     }

//     System.out.println("  >> ...");
//     System.out.println("  >> Parsing completed.");
//     System.out.println("Goodbye Mr. Anderson.");
//   }

  
// }

package DAAR.tme1.RegEx;

import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class RegEx {
    // MACROS
    static final int CONCAT = 0xC04CA7;
    static final int ETOILE = 0xE7011E;
    static final int ALTERN = 0xA17E54;
    static final int PROTECTION = 0xBADDAD;

    static final int PARENTHESEOUVRANT = 0x16641664;
    static final int PARENTHESEFERMANT = 0x51515151;
    static final int DOT = 0xD07;

    // REGEX
    private static String regEx;

    public RegEx() {}

    public static void main(String[] args) {
        System.out.println("Bienvenue dans le test du parser d'expressions régulières.");

        if (args.length >= 1) {
            regEx = args[0];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print(">> Veuillez entrer une expression régulière: ");
            regEx = scanner.nextLine();
        }

        System.out.println(">> Expression régulière : \"" + regEx + "\".");
        System.out.println(">> ...");

        if (regEx.length() < 1) {
            System.err.println(">> ERREUR: expression régulière vide.");
        } else {
            try {
                // Étape 1: Analyse syntaxique
                RegExTree regExTree = RegExParser.parse(regEx);
                System.out.println(">> Arbre syntaxique construit: " + regExTree.toString() + ".");

                // Étape 2: Conversion en NFA
                NFA nfa = RegExToNFA.convert(regExTree);
                System.out.println(">> NFA construit.");

                // Générer le fichier DOT pour le NFA
                String nfaDot = nfa.toDot();
                Files.write(Paths.get("nfa.dot"), nfaDot.getBytes());
                System.out.println(">> Fichier 'nfa.dot' généré pour le NFA.");



                // Exécution des commandes dot pour générer les images (optionnel)
                try {
                    // Générer l'image du NFA
                    @SuppressWarnings("deprecation")
                    Process p1 = Runtime.getRuntime().exec("dot -Tpng nfa.dot -o nfa.png");
                    p1.waitFor();
                    System.out.println(">> Image 'nfa.png' générée pour le NFA.");

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.err.println(">> ERREUR: erreur de syntaxe pour l'expression régulière \"" + regEx + "\".");
                e.printStackTrace();
            }
        }

        System.out.println(">> ...");
        System.out.println(">> Analyse terminée.");
        System.out.println("Au revoir.");
    }
}


