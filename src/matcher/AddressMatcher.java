package matcher;
import model.*;

import java.util.*;

/**
 * Created by Vincent Escoto on 6/02/2015.
 */
public class AddressMatcher implements Matcher
{
    private static AddressMatcher instance = null;
    private String[] IgnoreTable = {"STE","SUITE","FLOOR","FL","#","UNIT","TH","APT","ST","RM","MAIL","CODE","BLDG"};
    private String[] StreetTable = {"NORTH","SOUTH","EAST","WEST","AVENUE","AVE","BOULEVARD","BLVD","NE","SE","SW","STREET","ST","ROAD",
            "RD","CULDESAC","CT","WAY","PARKWAY","PKWY","LANE","LN","DRIVE","DR","HIGHWAY","HWY","PIKE","CENTER","CTR",
            "PARK","LOOP","N","S","E","W"};
    private AddressMatcher() {
    }

    public static AddressMatcher getInstance() {
        if (instance == null) {
            instance = new AddressMatcher();
        }
        return instance;
    }

    public double match(Source s1, Source s2)
    {
        double mailScore = match(s1.mailingAddress,s2.mailingAddress),
                practiceScore = match(s1.practiceAddress,s2.practiceAddress);
        if(mailScore < 0 && practiceScore < 0)
        {
            return 0.0;
        }
        else if(mailScore >= 0 && practiceScore >= 0)
        {
            return (mailScore + practiceScore) / 2;
        }
        else if(mailScore >= 0)
        {
            return mailScore;
        }
        else
        {
            return practiceScore;
        }
    }

    private double match(Address a1, Address a2)
    {
        String[] u = new String[2], s = new String[2];
        double score = 0.0;

        if(a1.country.trim().toUpperCase().equals("NULL") &&
                a1.county.trim().toUpperCase().equals("NULL") &&
                a1.zip_code.trim().toUpperCase().equals("NULL") &&
                a1.region.trim().toUpperCase().equals("NULL") &&
                a1.city.trim().toUpperCase().equals("NULL") &&
                a1.unit.trim().toUpperCase().equals("NULL") &&
                a1.street.trim().toUpperCase().equals("NULL"))
        {
            score = -1.0;
        }
        else if (a1.country.trim().toUpperCase().equals("NULL") &&
                a1.county.trim().toUpperCase().equals("NULL") &&
                a1.zip_code.trim().toUpperCase().equals("NULL") &&
                a1.region.trim().toUpperCase().equals("NULL") &&
                a1.city.trim().toUpperCase().equals("NULL") &&
                a1.unit.trim().toUpperCase().equals("NULL") &&
                a1.street.trim().toUpperCase().equals("NULL"))
        {
            score = -1.0;
        }
        else if(a1.country.trim().toUpperCase().equals("NULL") || a2.country.trim().toUpperCase().equals("NULL") ||
                a1.country.trim().toUpperCase().equals(a2.country.trim().toUpperCase()))
        {
            if((a1.county.trim().toUpperCase().equals("NULL") || a2.county.trim().toUpperCase().equals("NULL") ||
                    a1.county.trim().toUpperCase().equals(a2.county.trim().toUpperCase())))
            {
                if((a1.zip_code.trim().toUpperCase().equals("NULL") || a2.zip_code.trim().toUpperCase().equals("NULL") ||
                        a1.zip_code.trim().toUpperCase().equals(a2.zip_code.trim().toUpperCase())))
                {
                    if((a1.zip_code.trim().toUpperCase().equals("NULL") || a2.zip_code.trim().toUpperCase().equals("NULL") ||
                            a1.zip_code.trim().toUpperCase().equals(a2.zip_code.trim().toUpperCase())))
                    {
                        if((a1.region.trim().toUpperCase().equals("NULL") || a2.region.trim().toUpperCase().equals("NULL") ||
                                a1.region.trim().toUpperCase().equals(a2.region.trim().toUpperCase())))
                        {
                            if((a1.city.trim().toUpperCase().equals("NULL") || a2.city.trim().toUpperCase().equals("NULL") ||
                                    a1.city.trim().toUpperCase().equals(a2.city.trim().toUpperCase())))
                            {
                                u[0] = a1.unit.trim().toUpperCase();
                                u[1] = a2.unit.trim().toUpperCase();
                                for(int i = 0; i < 2; i++)
                                {
                                    for(int j = 0; j < IgnoreTable.length; j++)
                                    {
                                        if(u[i].contains(IgnoreTable[j]))
                                        {
                                            u[i] = u[i].replace(IgnoreTable[j],"");
                                        }
                                    }
                                }
                                // System.out.println("Source 1: " + u[0] + " Source 2: " + u[1]);
                                if(u[0].trim().toUpperCase().equals(u[1].trim().toUpperCase()))
                                {
                                    if(!a1.street.trim().toUpperCase().equals("NULL") && !a2.street.trim().toUpperCase().equals("NULL"))
                                    {
                                        s[0] = a1.street.trim().toUpperCase();
                                        s[1] = a2.street.trim().toUpperCase();
                                        for(int i = 0; i < 2; i++)
                                        {
                                            for(int j = 0; j < StreetTable.length; j++)
                                            {
                                                if(s[i].contains(StreetTable[j]))
                                                {
                                                    s[i] = s[i].replace(StreetTable[j],"");
                                                }
                                            }
                                        }
                                        if(s[0].trim().toUpperCase().equals(s[1].trim().toUpperCase()))
                                        {
                                            score = 1.0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return score;
    }
}