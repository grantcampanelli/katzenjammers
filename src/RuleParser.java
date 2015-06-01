import org.ini4j.Ini;
import org.ini4j.Profile;
import org.ini4j.Profile.Section;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class RuleParser
{
    public static List<Rule> parseRules(String fileName)
    {

        List<Rule> rules = new ArrayList<Rule>();
        try
        {
            Ini ini = new Ini(new File(fileName));
            Section numRules = ini.get("NumRules");
            Integer ruleCount = Integer.parseInt(numRules.get("num"));
            for (int i = 0; i < ruleCount; i++)
            {
                Section curRule = ini.get("Rule" + i);
                double nameWeight = Double.parseDouble(curRule.get("nameWeight"));
                double phoneWeight = Double.parseDouble(curRule.get("phoneWeight"));
                double addressWeight = Double.parseDouble(curRule.get("addressWeight"));
                double specialtiesWeight = Double.parseDouble(curRule.get("specialtiesWeight"));
                double threshold = Double.parseDouble(curRule.get("threshold"));
                rules.add(new Rule(nameWeight, phoneWeight, addressWeight,
                    specialtiesWeight, threshold));
            }
            return rules;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Failed parsing rules");
            return null;
        }
    }
}
