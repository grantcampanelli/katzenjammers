import matcher.NameMatcher;
import matcher.PhoneMatcher;
import model.Score;
import model.Source;

import java.io.File;
import java.util.List;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class MatchMaker
{
    public static void main(String[] args) {
        List<Rule> rules = RuleParser.parseRules("rules.ini");
        //you can see the parsing is working
        for (Rule r : rules) {
            System.out.println(r);
        }

        Score score = new Score();
        MatchKeeper matchKeeper = new MatchKeeper();

        List<Source> sources = Main.readInFromDatabase();
        System.out.println("HI");

        //match pairwise
        for (int curSource = 0; curSource < sources.size(); curSource++) {
            for (int toCompare = 0; toCompare < sources.size(); toCompare++) {
                //create score for this match
                score.nameScore = NameMatcher.getInstance().match(sources.get
                    (curSource), sources.get(toCompare));
                score.phoneScore = PhoneMatcher.getInstance().match(sources.get
                    (curSource), sources.get(toCompare));

                //run each rule
                for (Rule rule : rules) {
                    if (rule.runRule(score)) {
                        matchKeeper.commit(sources.get(curSource), sources.get(toCompare), rule);
                        continue;
                    }
                }
            }
        }
    }
}
