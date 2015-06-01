import model.Source;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class MatchKeeper
{
    Integer curMasterId = 0;
    //maps source ID's to Master Id's
    private Map<Integer, Integer> map = new HashMap<Integer, Integer>();

    public void commit(Source source1, Source source2, Rule why) {
        Integer masterId = curMasterId;

        if (map.get(source1.id) != null) {
            masterId = map.get(source1.id);
            map.put(source2.id, masterId);
        }
        //is this case possible?
        else if (map.get(source2.id) != null) {
            masterId = map.get(source2.id);
            map.put(source1.id, masterId);
        }
        else {
            map.put(source1.id, masterId);
            map.put(source2.id, masterId);
        }

        //save this match as an audit record
    }
}
