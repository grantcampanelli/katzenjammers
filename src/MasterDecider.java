import model.Master;
import model.Source;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mitch Fierro on 6/6/2015.
 */
//TODO this class is not done yet.
public class MasterDecider
{
    public static Master master(Integer masterId, Set<Source> src) {
        Map<String, Integer> prefixMap = new HashMap <String, Integer>();
        Map<String, Integer> suffixMap = new HashMap <String, Integer>();
        Map<String, Integer> credentialMap = new HashMap <String, Integer>();
        Map<String, Integer> phoneMap = new HashMap <String, Integer>();

        String bestFirst = "", bestMiddle = "", bestLast = "";
        int maxFirstLength = 0, maxMiddleLength = 0, maxLastLength = 0;
        String isSoleProprieter = "X", bestPhoneNumber = null, bestGender = null,
                bestType = null, bestPrefix = null, bestSuffix = null,
                bestCredentials = null;
        Integer bestPrimarySpecialty = null, bestSecondarySpecialty = null;

        for (Source s : src) {
            String[] names = s.name.split("\"\\\\s+\"");
            if (names.length >0 && names[0] != null && names[0].length() >
                maxFirstLength) {
                maxFirstLength = names[0].length();
                bestFirst = names[0];
            }
            if (names.length > 1 && names[1] != null && names[1].length() >
                maxMiddleLength) {
                maxMiddleLength = names[1].length();
                bestMiddle = names[1];
            }
            if (names.length > 2 && names[2] != null && names[2].length() >
                maxFirstLength) {
                maxLastLength = names[2].length();
                bestLast = names[2];
            }

            if(s.solProp != null) {
                if (s.solProp.equals("Y")) {
                    isSoleProprieter = "Y";
                } else if (s.solProp.equals("N")) {
                    isSoleProprieter = "N";
                }
            }

            if(s.phone != null){
                if(bestPhoneNumber == null)
                    bestPhoneNumber = s.phone;
            }

            if(s.gender != null) {
                bestGender = s.gender;
            }

            if(s.type != null) {
                bestType = s.type;
            }

            if(s.primarySpecialty != null)
                bestPrimarySpecialty = s.primarySpecialty;

            if(s.secondarySpecialty != null)
                bestSecondarySpecialty = s.secondarySpecialty;

            if(s.prefix != null)
                bestPrefix = s.prefix;

            if(s.suffix != null)
                bestSuffix = s.suffix;

            if(s.medCredential != null)
                bestCredentials = s.medCredential;

        }

        Master m = new Master();
        //currently returning a dummy master (partially correct).  Will need to fix this
        //these are the only fields correct for now
        m.dob = null;
        m.id = masterId;
        m.isSole = isSoleProprieter;
        m.firstName = bestFirst;
        m.middleName = bestMiddle;
        m.lastName = bestLast;

        m.gender = bestGender; //all male for now
        m.credential = bestCredentials; ///all md for now
        m.phone = bestPhoneNumber;

        m.prefix = bestPrefix;
        m.suffix = bestSuffix;

        if(bestPrimarySpecialty != null)
            m.primarySpec = Integer.toString(bestPrimarySpecialty);
        else
            m.primarySpec = null;

        if(bestSecondarySpecialty != null)
            m.secondarySpec = Integer.toString(bestSecondarySpecialty);
        else
            m.secondarySpec = null;
        m.type = bestType; //all ind for now. obviously need to change

        return m;
    }
}
