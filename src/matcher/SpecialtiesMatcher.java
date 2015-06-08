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
            //System.out.println("Same Primary Specialty");
            score = 1;
        }

        if(sec1 == sec2) {
            //System.out.println("Same Secondary Specialty");
            score = 1;
        }

        if(prim1 == sec2 || prim2 == sec1) {
            //System.out.println("Same Primary Specialty as Other Secondary Specialty");
            score = 0.5;
        }

        return score;
    }

}

