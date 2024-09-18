 package DAAR.tme1.RegEx;

import java.util.Scanner;
import java.util.ArrayList;

import java.lang.Exception;

public class RegEx {
  //MACROS
  static final int CONCAT = 0xC04CA7;
  static final int ETOILE = 0xE7011E;
  static final int ALTERN = 0xA17E54;
  static final int PROTECTION = 0xBADDAD;

  static final int PARENTHESEOUVRANT = 0x16641664;
  static final int PARENTHESEFERMANT = 0x51515151;
  static final int DOT = 0xD07;
  
  //REGEX
  private static String regEx;
  
  //CONSTRUCTOR
  public RegEx(){}

  //MAIN
  public static void main(String arg[]) {
    System.out.println("Bienvenue dans le nouveau egrep");
    if (arg.length!=0) {
      regEx = arg[0];
    } else {
      Scanner scanner = new Scanner(System.in);
      System.out.print("  >> Please enter a regEx: ");
      regEx = scanner.next();
    }
    System.out.println("  >> Parsing regEx \""+regEx+"\".");
    System.out.println("  >> ...");
    
    if (regEx.length()<1) {
      System.err.println("  >> ERROR: empty regEx.");
    } else {
      System.out.print("  >> ASCII codes: ["+(int)regEx.charAt(0));
      for (int i=1;i<regEx.length();i++) System.out.print(","+(int)regEx.charAt(i));
      System.out.println("].");
      try {
        RegExTree ret = RegExParser.parse(regEx);
        System.out.println("  >> Tree result: "+ret.toString()+".");

      } catch (Exception e) {
        System.err.println("  >> ERROR: syntax error for regEx \""+regEx+"\".");
      }
    }

    System.out.println("  >> ...");
    System.out.println("  >> Parsing completed.");
    System.out.println("Goodbye Mr. Anderson.");
  }

  
}
