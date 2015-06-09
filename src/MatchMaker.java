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
        if(args.length != 0)
            JDBCDeserialize.ReadNumSources(0);
            //System.out.println(args.length);
        else
            JDBCDeserialize.ReadNumSources(1);

        OutputDirectory = JDBCDeserialize.getOutputDirectory();

        List<Rule> rules = RuleParser.parseRules("rules.ini");
        //you can see the parsing is working
        for (Rule r : rules) {
            System.out.println(r);
        }

        Score score = new Score();
        MatchKeeper matchKeeper = MatchKeeper.getInstance();

        List<Source> sources = JDBCDeserialize.readInFromDatabase();
        System.out.println("Start to Match");

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
        File InsertMastersFile = new File(OutputDirectory+"InsertMasters.sql");
        File CrosswalkFile = new File(OutputDirectory+"Crosswalk_Katzenjammers.txt");
        File InsertCrosswalkFile = new File(OutputDirectory+"InsertCrosswalk.sql");
        File AuditFile = new File(OutputDirectory+"AuditTrail.txt");
        File AuditFileSQL = new File(OutputDirectory+"InsertAuditTrail.sql");
        FileWriter mastersWriter, crosswalkWriter, insertMasterWriter, insertCrosswalkWriter;
        try {
            mastersWriter = new FileWriter(MastersFile, false);
            PrintWriter pw = new PrintWriter(mastersWriter);

            insertMasterWriter = new FileWriter(InsertMastersFile, false);
            PrintWriter sqlInsert = new PrintWriter(insertMasterWriter);

            printMastersColumns(pw);

            String[] names;

            for(Master m : masterList) {

                names = m.firstName.split("\\s+");
                if(names.length == 2) {
                    if(names[0] != null && names[1] != null) {
                        m.firstName = names[0];
                        m.lastName = names[1];
                    }

                }
                else {
                    if (names.length > 0 && names[0] != null) {
                        m.firstName = names[0];
                    }
                    if (names.length > 1 && names[1] != null) {
                        m.middleName = names[1];
                    }
                    if (names.length > 2 && names[2] != null) {
                        m.lastName = names[2];
                    }
                }

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



                sqlInsert.print("INSERT INTO Master VALUES(");
                printIntegerToSql(sqlInsert, m.id, ",");
                printStringToSql(sqlInsert, m.type, ",");
                printStringToSql(sqlInsert, m.prefix, ",");
                printStringToSql(sqlInsert, m.firstName, ",");
                printStringToSql(sqlInsert, m.middleName, ",");
                printStringToSql(sqlInsert, m.lastName, ",");
                printStringToSql(sqlInsert, m.suffix, ",");
                printStringToSql(sqlInsert, m.credential, ",");
                printStringToSql(sqlInsert, m.gender, ",");
                printStringToSql(sqlInsert, m.dob, ",");
                printStringToSql(sqlInsert, m.isSole, ",");
                printStringToSql(sqlInsert, m.phone, ",");

                if(m.primarySpec != null)
                    printIntegerToSql(sqlInsert, Integer.parseInt(m.primarySpec), ",");
                else
                    printStringToSql(sqlInsert, null, ",");

                if(m.secondarySpec != null)
                    printIntegerToSql(sqlInsert, Integer.parseInt(m.secondarySpec), "");
                else
                    printStringToSql(sqlInsert, null, "");

                sqlInsert.print(");\n");

                //INSERT INTO Source VALUES(24614,"IND",NULL,"Christina L Grant",NULL,NULL,"F",NULL,"N",NULL,7558,NULL);
                //INSER
//                printIntegerItemToConsole(m.id, "\t");
//                printStringItemToConsole(m.type, "\t");
//                printStringItemToConsole(m.prefix, "\t");
//                printStringItemToConsole(m.firstName, "\t");
//                printStringItemToConsole(m.middleName, "\t");
//                printStringItemToConsole(m.lastName, "\t");
//                printStringItemToConsole(m.suffix, "\t");
//                printStringItemToConsole(m.credential, "\t");
//                printStringItemToConsole(m.gender, "\t");
//                printStringItemToConsole(m.dob, "\t");
//                printStringItemToConsole(m.isSole, "\t");
//                printStringItemToConsole(m.phone, "\t");
//                printStringItemToConsole(m.primarySpec, "\t");
//                printStringItemToConsole(m.secondarySpec, "\n");
            }
            pw.close();

            sqlInsert.close();

            crosswalkWriter = new FileWriter(CrosswalkFile, false);
            pw = new PrintWriter(crosswalkWriter);

            printCrosswalkColumns(pw);

            insertCrosswalkWriter = new FileWriter(InsertCrosswalkFile, false);
            sqlInsert = new PrintWriter(insertCrosswalkWriter);

            Integer source_id, master_id;

            for (Map.Entry<Integer, Integer> entry : crosswalk.entrySet()) {
                source_id = entry.getKey();
                master_id = entry.getValue();
                printCrosswalkLine(pw, master_id, source_id);
                sqlInsert.print("INSERT INTO Crosswalk VALUES("+master_id+","+source_id+");\n");

                // use key and value
            }
            sqlInsert.close();
            pw.close();

            PrintWriter auditWriter = new PrintWriter(new FileWriter(AuditFile, false));
            printAudit(auditWriter);
            auditWriter.close();

            auditWriter = new PrintWriter(new FileWriter(AuditFileSQL, false));
            printAuditToSQL(auditWriter);
            auditWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



        System.out.println("Done with printing masters");
        System.out.println("These are the rules that fired, causing the mappings above");
        /*for (String s : MatchKeeper.getInstance().getAllRuleDescriptions()) {
            System.out.println(s);
        }*/

        JDBCDeserialize.ClearAndInsertIntoDBs();
    }

    public static void printAudit(PrintWriter out) {
        out.print("Source1\tSource2\tRuleID\n");
        Map<Map.Entry<Integer, Integer>, Integer> audit = MatchKeeper.getInstance()
            .getAudit();
        for(Map.Entry<Map.Entry<Integer, Integer>, Integer> entry : audit.entrySet()) {
            out.print(entry.getKey().getKey()+"\t");
            out.print(entry.getKey().getValue()+"\t");
            out.print(entry.getValue()+"\n");
        }
    }

    public static void printAuditToSQL(PrintWriter out) {
        //out.print("Source1\tSource2\tRuleID\n");
        Map<Map.Entry<Integer, Integer>, Integer> audit = MatchKeeper.getInstance()
                .getAudit();
        for(Map.Entry<Map.Entry<Integer, Integer>, Integer> entry : audit.entrySet()) {
            out.print("INSERT INTO Audit VALUES(");
            out.print(entry.getKey().getKey()+",");
            out.print(entry.getKey().getValue()+",");
            out.print(entry.getValue()+");\n");
        }
    }



    public static void printIntegerToSql(PrintWriter sqlInsert, Integer item, String delimiter) {
        if(item != null)
            sqlInsert.print(item+delimiter);
        else
            sqlInsert.print("NULL"+delimiter);

    }

    public static void printStringToSql(PrintWriter sqlInsert, String item, String delimiter) {
        if(item == null)
            sqlInsert.print("NULL"+delimiter);
        else if(item.length() == 0)
            sqlInsert.print("NULL"+delimiter);
        else
            sqlInsert.print('"'+item+'"'+delimiter);
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
