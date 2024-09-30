package DAAR.tme1.RegEx;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import DAAR.tme2.Kmp;

public class RegExKMP {
    private static String pat;
    
    private static  void matchLinesInFile(String filePath, String pat) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                int lineNumber = 0;
        
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    
                    // Trouver toutes les sous-chaînes correspondantes dans la ligne
                    boolean foundMatch = false;
                    StringBuilder matchedSubstrings = new StringBuilder();
        
                    for (int i = 0; i < line.length(); i++) {
                        for (int j = i + 1; j <= line.length(); j++) {
                            String substring = line.substring(i, j);
                            List<Integer> resul= Kmp.KMPSearch(pat,substring);
                            if (matched(resul)) {
                                matchedSubstrings.append(substring).append(" ");
                                foundMatch = true;
                            }
                        }
                    }
        
                    // Si des sous-chaînes correspondent, afficher la ligne avec les sous-chaînes correspondantes
                    if (foundMatch) {
                        System.out.println("Ligne " + lineNumber + " : " + line);

                    }
                }
            } catch (Error e) {
                System.out.println("message d'erreur : " + e.getMessage());
            }
        }
    
        public static boolean matched(List<Integer> result){
            if (result.size()==0) {
                return false;
            }
            return true;
        }
    
    
        public static void main(String[] args) {

            try {
            Scanner scanner = new Scanner(System.in);
            System.out.print(">> Veuillez entrer un pattern : ");
            pat = scanner.nextLine();
    
            // Demander le chemin du fichier texte
            System.out.print(">> Veuillez entrer le chemin vers le fichier texte: ");
            String filePath = scanner.nextLine();
             
    
            
            matchLinesInFile(filePath,pat);
        } catch (Exception e) {

        }
        

        

    }
    
}
