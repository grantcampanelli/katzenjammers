package matcher;
import model.Source;
/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class NameMatcher implements Matcher
{
    private static NameMatcher instance = null;

    private NameMatcher() {
    }

    public static NameMatcher getInstance() {
        if (instance == null) {
            instance = new NameMatcher();
        }
        return instance;
    }

    public double match(Source s1, Source s2) {
        return 0.0;
    }
}
