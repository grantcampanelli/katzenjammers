package matcher;
import model.*;
/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class PhoneMatcher implements Matcher
{
    private static PhoneMatcher instance = null;

    private PhoneMatcher() {
    }

    public static PhoneMatcher getInstance() {
        if (instance == null) {
            instance = new PhoneMatcher();
        }
        return instance;
    }

    public double match(Source s1, Source s2) {
        return 0.0;
    }
}
