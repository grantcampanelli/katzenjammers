import matcher.NameMatcher;
import matcher.PhoneMatcher;
import model.Master;
import model.Score;
import model.Source;
import model.Specialties;


import java.io.File;
import java.util.*;

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
        MatchKeeper matchKeeper = MatchKeeper.getInstance();

        List<Source> sources = JDBCDeserialize.readInFromDatabase();
        System.out.println("HI");

        //match pairwise
        for (int curSource = 0; curSource < sources.size(); curSource++) {
            for (int toCompare = 0; toCompare < sources.size(); toCompare++) {
                if (sources.get(curSource).type.equals(sources.get(toCompare).type))
                {
                    //create score for this match
                    score.nameScore = NameMatcher.getInstance().match(sources.get
                        (curSource), sources.get(toCompare));
                    score.phoneScore = PhoneMatcher.getInstance().match(sources.get
                        (curSource), sources.get(toCompare));
                    

                    //run each rule
                    for (Rule rule : rules)
                    {
                        if (rule.runRule(score))
                        {
                            matchKeeper.commit(sources.get(curSource), sources.get(toCompare), rule);

                            continue;
                        }
                    }
                }
            }
        }


        Map<Integer, Integer> crosswalk = new HashMap<Integer, Integer>();
        crosswalk = MatchKeeper.getInstance().getCrossWalkMap();


        List<Master> masterList = new ArrayList<Master>();
        System.out.println("Starting to print masters");
        masterList = MatchKeeper.getInstance().getMasters();
        System.out.println("Master List length: "+masterList.size());

        for(Master m : masterList) {
            System.out.println("Master: ID:"+m.id);
//        public String type;
//        public String prefix;
//        public String firstName;
//        public String middleName;
//        public String lastName;
//        public String suffix;
//        public String credential;
//        public String gender;
//        public String dob;
//        public String isSole;
//        public String phone;
//        public String primarySpec;
//        public String secondarySpec;
        }

        System.out.println("Done with printing masters");
    }
}
