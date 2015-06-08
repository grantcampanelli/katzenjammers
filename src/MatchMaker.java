import matcher.AddressMatcher;
import matcher.NameMatcher;
import matcher.PhoneMatcher;
import model.*;


import java.io.*;
import java.util.*;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class MatchMaker
{
    public static String OutputDirectory;

    public static void main(String[] args) {
        JDBCDeserialize.ReadNumSources();
        OutputDirectory = JDBCDeserialize.getOutputDirectory();

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
                    score.addressScore = AddressMatcher.getInstance().match(sources.get
                        (curSource), sources.get(toCompare));
                    score.specialtiesScore = AddressMatcher.getInstance().match(sources.get
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



        File MastersFile = new File(OutputDirectory+"Masters_Katzenjammers.txt");
        File CrosswalkFile = new File(OutputDirectory+"Crosswalk_Katzenjammers.txt");
        FileWriter mastersWriter, crosswalkWriter;
        try {
            mastersWriter = new FileWriter(MastersFile, false);
            PrintWriter pw = new PrintWriter(mastersWriter);

            printMastersColumns(pw);

            for(Master m : masterList) {
                printIntegerItem(pw, m.id, "\t");
                printStringItem(pw, m.type, "\t");
                printStringItem(pw, m.prefix, "\t");
                printStringItem(pw, m.firstName, "\t");
                printStringItem(pw, m.middleName, "\t");
                printStringItem(pw, m.lastName, "\t");
                printStringItem(pw, m.suffix, "\t");
                printStringItem(pw, m.credential, "\t");
                printStringItem(pw, m.gender, "\t");
                printStringItem(pw, m.dob, "\t");
                printStringItem(pw, m.isSole, "\t");
                printStringItem(pw, m.phone, "\t");
                printStringItem(pw, m.primarySpec, "\t");
                printStringItem(pw, m.secondarySpec, "\n");

                printIntegerItemToConsole(m.id, "\t");
                printStringItemToConsole(m.type, "\t");
                printStringItemToConsole(m.prefix, "\t");
                printStringItemToConsole(m.firstName, "\t");
                printStringItemToConsole(m.middleName, "\t");
                printStringItemToConsole(m.lastName, "\t");
                printStringItemToConsole(m.suffix, "\t");
                printStringItemToConsole(m.credential, "\t");
                printStringItemToConsole(m.gender, "\t");
                printStringItemToConsole(m.dob, "\t");
                printStringItemToConsole(m.isSole, "\t");
                printStringItemToConsole(m.phone, "\t");
                printStringItemToConsole(m.primarySpec, "\t");
                printStringItemToConsole(m.secondarySpec, "\n");
            }
            pw.close();

            crosswalkWriter = new FileWriter(CrosswalkFile, false);
            pw = new PrintWriter(crosswalkWriter);

            printCrosswalkColumns(pw);

            Integer source_id, master_id;

            for (Map.Entry<Integer, Integer> entry : crosswalk.entrySet()) {
                source_id = entry.getKey();
                master_id = entry.getValue();
                printCrosswalkLine(pw, master_id, source_id);
                // use key and value
            }

            pw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }



        System.out.println("Done with printing masters");
        for (String s : MatchKeeper.getInstance().getAllRuleDescriptions()) {
            System.out.println(s);
        }
    }

    public static void printMastersColumns(PrintWriter pw) {
        pw.print("Master Id\t");
        pw.print("Provider Type\t");
        pw.print("Name Prefix\t");
        pw.print("First Name\t");
        pw.print("Middle Name\t");
        pw.print("Last Name\t");
        pw.print("Name Suffix\t");
        pw.print("Medical Credential\t");
        pw.print("Gender\t");
        pw.print("Date of Birth\t");
        pw.print("Is Sole Proprietor\t");
        pw.print("Primary Phone\t");
        pw.print("Primary Specialty\t");
        pw.print("Secondary Specialty\n");
    }


    public static void printCrosswalkColumns(PrintWriter pw) {
        pw.print("Master Id\t");
        pw.print("Source Identifier\n");
    }


    public static void printIntegerItem(PrintWriter pw, Integer item, String delimiter) {
        if(item == null)
            pw.print("NULL"+delimiter);
        else
            pw.print(item + delimiter);
    }

    public static void printStringItem(PrintWriter pw, String item, String delimiter) {
        if(item == null)
            pw.print("NULL"+delimiter);
        else
            pw.print(item + delimiter);
    }


    public static void printIntegerItemToConsole(Integer item, String delimiter) {
        if(item == null)
            System.out.print("NULL"+delimiter);
        else
            System.out.print(item + delimiter);
    }

    public static void printStringItemToConsole(String item, String delimiter) {
        if(item == null)
            System.out.print("NULL"+delimiter);
        else
            System.out.print(item + delimiter);
    }

    public static void printCrosswalkLine(PrintWriter pw, Integer master_id, Integer source_id) {
        if(master_id != null && source_id != null)
            pw.print(master_id + "\t" + source_id + "\n");
    }



}
