package DAAR.tme2;

import java.util.ArrayList;
import java.util.List;

public class Kmp {
  
    // Fills lps[] for given pattern pat
    static void computeLPSArray(String pat, int M, int[] lps)
    {
        // Length of the previous longest prefix suffix
        int len = 0;
      
        // lps[0] is always 0
        lps[0] = 0;

        // Loop calculates lps[i] for i = 1 to M-1
        int i = 1;
        while (i < M) {
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            }
            else {
                if (len != 0) {
                    len = lps[len - 1];
                }
                else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
    }

    // Prints occurrences of pat in txt
    static List<Integer> KMPSearch(String pat, String txt)
    {
        int M = pat.length();
        int N = txt.length();

        // Create lps[] that will hold the longest prefix
        // suffix values for pattern
        int[] lps = new int[M];
        List<Integer> result = new ArrayList<>();

        // Preprocess the pattern (calculate lps[] array)
        computeLPSArray(pat, M, lps);

        int i = 0; // index for txt
        int j = 0; // index for pat
        while ((N - i) >= (M - j)) {
            if (pat.charAt(j) == txt.charAt(i)) {
                j++;
                i++;
            }

            if (j == M) {
                result.add(i - j + 1);
                j = lps[j - 1];
            }
            else if (i < N
                     && pat.charAt(j) != txt.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                }
                else {
                    i = i + 1;
                }
            }
        }
        return result;
    }

    // Driver code
    public static void main(String[] args)
    {
        String txt = "geeksforgeeks";
        String pat = "geeks";
      
        List<Integer> result = KMPSearch(pat, txt);

        // Print all the occurrences (1-based indices)
        for (int index : result) {
            System.out.print(index + " ");
        }
    }
}

