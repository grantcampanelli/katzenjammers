package matcher;
import model.*;
/**
 * Created by grantcampanelli on 6/7/15.
 */
public class SpecialtiesMatcher implements Matcher{

    private static SpecialtiesMatcher instance = null;

    private SpecialtiesMatcher() {
    }

    public static SpecialtiesMatcher getInstance() {
        if (instance == null) {
            instance = new SpecialtiesMatcher();
        }
        return instance;
    }

    public double match(Source s1, Source s2)
    {
        double score = 0.0;
        Integer prim1 = s1.primarySpecialty, prim2 = s2.primarySpecialty,
                sec1 = s1.secondarySpecialty, sec2 = s2.secondarySpecialty;

        if(prim1 == prim2) {
            System.out.println("Same Primary Specialty");
            score = 1;
        }

        if(sec1 == sec2) {
            System.out.println("Same Secondary Specialty");
            score = 1;
        }

        return score;

//        String p1 = s1.phone, p2 = s2.phone;
//        int diff = 0;
//        if(p1.length() != PhoneLength || p2.length() != PhoneLength)
//        {
//            return score;
//        }
//        for(int index = 0; index < PhoneLength; index++)
//        {
//            if(p1.charAt(index) != p2.charAt(index))
//            {
//                diff++;
//            }
//        }
//        System.out.println("Difference: " + diff);
//        if(diff <= 3)
//        {
//            score = (double)(PhoneLength - diff) / PhoneLength;
//        }
//
//        return score;
    }
}

