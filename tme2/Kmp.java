package DAAR.tme2;

public class Kmp {

    public int[] computeTabLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0; //longueur prefixe 
        int i = 0;


        lps[0] = 0;
        while (i<m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len-1];
                } else {
                    lps[i] =0;
                    i++;
                }
            }
        }
    
        return lps;
    }

    public void KMPSearch (String pattern , String text) {
        int m = pattern.length();
        int n = text.length();
        int[] lps = computeTabLPS(pattern);
        int i=0;
        int j=0;

        while (i<n) {
            if (pattern.charAt(j)==text.charAt(i)) {
                i++;
                j++;
            }
            if (j==m) {
                System.out.println("motif trouvé a l'index " + (i-j));
                j= lps[j-1];
            } else if(i<n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j= lps[j-1];
                } else {
                    i++;
                }

            }
        }
    }

    public static void main(String[] args) {
        Kmp kmp = new Kmp();
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        kmp.KMPSearch(pattern, text);
    }
    
}
