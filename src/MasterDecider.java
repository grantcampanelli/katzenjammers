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
        Map<Integer, Integer> primarySpecMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> secondarySpecMap = new HashMap<Integer, Integer>();

        String bestFirst = "", bestMiddle = "", bestLast = "";
        int maxFirstLength = 0, maxMiddleLength = 0, maxLastLength = 0;
        String isSoleProprieter = "X", bestPhoneNumber = null, bestGender = null,
                bestType = null, bestPrefix = null, bestSuffix = null,
                bestCredentials = null;
        Integer bestPrimarySpecialty = null, bestSecondarySpecialty = null;

        Integer maxPrimarySpec = 0, maxSecondarySpec = 0, maxSuffix = 0, maxPrefix = 0,
            maxCred = 0, maxPhone = 0;

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
            //master phones
            if (s.phone != null && phoneMap.get(s.phone) == null) {
                phoneMap.put(s.phone, 0);
            }
            if (s.phone != null) {
                phoneMap.put(s.phone, phoneMap.get(s.phone) + 1);
                if (phoneMap.get(s.phone) > maxPhone)
                    bestPhoneNumber = s.phone;
            }
            //pick last gender lol
            if(s.gender != null) {
                bestGender = s.gender;
            }
            //all types guaranteed to be the same
            if(s.type != null) {
                bestType = s.type;
            }
            //master primary specs
            if (s.primarySpecialty != null && primarySpecMap.get(s.primarySpecialty) == null) {
                primarySpecMap.put(s.primarySpecialty, 0);
            }
            if (s.primarySpecialty != null)
            {
                primarySpecMap.put(s.primarySpecialty, primarySpecMap.get(s.primarySpecialty) + 1);
                if (primarySpecMap.get(s.primarySpecialty) > maxPrimarySpec)
                    bestPrimarySpecialty = s.primarySpecialty;

            }
            //amster secondary specs
            if (s.secondarySpecialty != null && secondarySpecMap.get(s.secondarySpecialty) == null) {
                secondarySpecMap.put(s.secondarySpecialty, 0);
            }
            if (s.secondarySpecialty != null)
            {
                secondarySpecMap.put(s.secondarySpecialty, secondarySpecMap.get(s.secondarySpecialty) + 1);
                if (secondarySpecMap.get(s.secondarySpecialty) > maxSecondarySpec)
                    bestSecondarySpecialty = s.secondarySpecialty;
            }

            //master prefixs
            if (s.prefix != null && prefixMap.get(s.prefix) == null) {
                prefixMap.put(s.prefix, 0);
            }
            if (s.prefix != null) {
                prefixMap.put(s.prefix, prefixMap.get(s.prefix) + 1);
                if (prefixMap.get(s.prefix) > maxPrefix)
                    bestPrefix = s.prefix;
            }
            //master suffixs
            if (s.suffix != null && suffixMap.get(s.suffix) == null) {
                suffixMap.put(s.suffix, 0);
            }
            if (s.suffix != null)
            {
                suffixMap.put(s.suffix, suffixMap.get(s.suffix) + 1);
                if (suffixMap.get(s.suffix) > maxSuffix)
                    bestSuffix = s.suffix;
            }

            //master medccreds
            if (s.medCredential != null && credentialMap.get(s.medCredential) == null) {
                credentialMap.put(s.medCredential, 0);
            }
            if (s.medCredential != null)
            {
                credentialMap.put(s.medCredential, credentialMap.get(s.medCredential) + 1);
                if (credentialMap.get(s.medCredential) > maxCred)
                    bestCredentials = s.medCredential;
            }

        }
        if (isSoleProprieter == "") {
            isSoleProprieter = "X";
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
