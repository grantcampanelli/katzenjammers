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
    public Master master(Integer masterId, Set<Source> src) {
        Map<String, Integer> prefixMap = new HashMap <String, Integer>();
        Map<String, Integer> suffixMap = new HashMap <String, Integer>();
        Map<String, Integer> credentialMap = new HashMap <String, Integer>();
        Map<String, Integer> phoneMap = new HashMap <String, Integer>();

        String bestFirst, bestMiddle, bestLast;
        int maxFirstLength = 0, maxMiddleLength = 0, maxLastLength = 0;
        String isSoleProprieter = "";

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
                bestLast = names[2]
            }

            if (s.solProp.equals("Y")) {
                isSoleProprieter = "Y";
            }
            else if (s.solProp.equals("N")) {
                isSoleProprieter = "N";
            }

            //if ()

        }
        if (isSoleProprieter.equals("")) {
            isSoleProprieter = "X";
        }


    }

}
