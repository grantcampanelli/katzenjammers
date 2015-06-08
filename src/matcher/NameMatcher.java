package matcher;
import model.Source;
import java.io.*;
import java.util.Scanner;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class NameMatcher implements Matcher
{
    private static NameMatcher instance = null;
    public static final double MATCH = 1.0;
    public static final double STRONG_MATCH = 0.75;
    public static final double MAYBE_MATCH = 0.5;
    public static final double WEAK_MATCH = 0.25;
    public static final double NO_MATCH = 0.0;


    private NameMatcher() {
    }

    public static NameMatcher getInstance() {
        if (instance == null) {
            instance = new NameMatcher();
        }
        return instance;
    }

    public double match(Source ss1, Source ss2) {
        double ret = 0.0;
        double jwret = 0.0;
        double lev = 0.0;
        String temp;
        String s1 = ss1.name;
        String s2 = ss2.name;

        if(s1.length() > s2.length()) {
            temp = s1;
            s1 = s2;
            s2 = temp;
        }
        JaroWinkler jw = new JaroWinkler(s1, s2);
        jwret = jw.getSimilarity(s1, s2);
        lev = LevenshteinDistance.distance(s1, s2);
        if(lev == 0) {
            return MATCH;
        }
        //if the edit distance is signifigant but jw value is high, it still might be a match
        if(lev > 3 && jwret > 90) {
            return MAYBE_MATCH;
        }

        if(lev == 1) {
            return STRONG_MATCH;
        }
        if(jwret > 92) {
            return WEAK_MATCH;
        }
        return NO_MATCH;
    }

    public static class LevenshteinDistance {
 
        public static int distance(String a, String b) {
            a = a.toLowerCase();
            b = b.toLowerCase();
            // i == 0
            int [] costs = new int [b.length() + 1];
            for (int j = 0; j < costs.length; j++)
                costs[j] = j;
            for (int i = 1; i <= a.length(); i++) {
                // j == 0; nw = lev(i - 1, j)
                costs[0] = i;
                int nw = i - 1;
                for (int j = 1; j <= b.length(); j++) {
                    int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                    nw = costs[j];
                    costs[j] = cj;
                }
            }
            return costs[b.length()];
        }    
    }

    //JaroWinkler
//CREDITS: http://blogs.ucl.ac.uk/chime/2010/06/28/java-example-code-of-common-similarity-algorithms-used-in-data-mining/
//Made less broken by Cameron
//THIS CLASS DOESN"T LIKE WHEN the second argument is shorter or when it gets empty strings so the order of arguments
    //is swaped if necessary before it is called.
    public class JaroWinkler
    {
        private String compOne;
        private String compTwo;
    
        private String theMatchA = "";
        private String theMatchB = "";
        private int mRange = -1;
    
        public JaroWinkler()
        {
        }
    
        public JaroWinkler(String s1, String s2)
        {
            compOne = s1;
            compTwo = s2;
        }
    
        public double getSimilarity(String s1, String s2)
        {
            compOne = s1;
            compTwo = s2;
    
            mRange = Math.max(compOne.length(), compTwo.length()) / 2 - 1;
    
            double res = -1;
    
            int m = cameronsGetMatch();
            if(m == 0)
            {
                return 0;
            }
            int t = 0;
            if (getMissMatch(compTwo,compOne) > 0)
            {
                t = (getMissMatch(compOne,compTwo) / getMissMatch(compTwo,compOne));
            }
            int l1 = compOne.length();
            int l2 = compTwo.length();
    
            double f = 0.3333;
            double mt = (double)(m-t)/m;
            double jw = f * ((double)m/l1+(double)m/l2+(double)mt);
            res = jw + getCommonPrefix(compOne,compTwo) * (0.1*(1.0 - jw));
    
            return res;
        }
        private int cameronsGetMatch() {
            int matches = 0;
            char bad = '@';
            StringBuilder check = new StringBuilder(compOne);
            for (int i = 0; i < compOne.length(); i++) {
                for (int j = 0; j < compTwo.length(); j++) {
                    if (compOne.charAt(i) == compTwo.charAt(j)
                            && j-i <= mRange
                            && check.charAt(i) != bad) {
                        check.setCharAt(i, bad);
                        matches++;
                    }
                }
            }
            return matches;
        }

        private int getMissMatch(String s1, String s2)
        {
            int transPositions = 0;

            for (int i = 0; i < theMatchA.length(); i++)
            {
                //Look Backward
                int counter = 0;
                while(counter <= mRange && i >= 0 && counter <= i)
                {
                    if (theMatchA.charAt(i) == theMatchB.charAt(i - counter) && counter > 0)
                    {
                        transPositions++;
                    }
                    counter++;
                }
    
                //Look forward
                counter = 1;
                while(counter <= mRange && i < theMatchB.length() && (counter + i) < theMatchB.length())
                {
                    if (theMatchA.charAt(i) == theMatchB.charAt(i + counter) && counter > 0)
                    {
                        transPositions++;
                    }
                counter++;
                }
            }
            return transPositions;
        }
    
        private int getCommonPrefix(String compOne, String compTwo)
        {
            int cp = 0;
            for (int i = 0; i < 4 && i < compOne.length()-1 && i < compTwo.length()-1; i++)
            {
                if (compOne.charAt(i) == compTwo.charAt(i)) cp++;
            }
            return cp;
        }
    }


}
