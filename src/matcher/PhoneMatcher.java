package matcher;
import model.*;



/**
 * Write a description of class PhoneMatcher here.
 *
 * @author Vincent Escoto
 * @version
 */
public class PhoneMatcher implements Matcher
{
    private static PhoneMatcher instance = null;
    private static final int PhoneLength = 10;

    private PhoneMatcher() {
    }

    public static PhoneMatcher getInstance() {
        if (instance == null) {
            instance = new PhoneMatcher();
        }
        return instance;
    }

    public double match(Source s1, Source s2)
    {
        double score = 0.0;
        String p1 = s1.phone, p2 = s2.phone;
        int diff = 0;
        if(p1.length() != PhoneLength || p2.length() != PhoneLength)
        {
            return score;
        }
        for(int index = 0; index < PhoneLength; index++)
        {
            if(p1.charAt(index) != p2.charAt(index))
            {
                diff++;
            }
        }
        System.out.println("Difference: " + diff);
        if(diff <= 3)
        {
            score = (double)(PhoneLength - diff) / PhoneLength;
        }

        return score;
    }
}


/**
 * Created by Mitch Fierro on 5/31/2015.
 */
//public class PhoneMatcher implements Matcher
//{
//    private static PhoneMatcher instance = null;
//
//    private PhoneMatcher() {
//    }
//
//    public static PhoneMatcher getInstance() {
//        if (instance == null) {
//            instance = new PhoneMatcher();
//        }
//        return instance;
//    }
//
//    public double match(Source s1, Source s2) {
//        return 0.0;
//    }
//}
