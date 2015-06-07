import model.Source;

import java.util.*;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class MatchKeeper
{
    Integer availableMasterId = 0;
    //maps source ID's to Master Id's
    private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    private Map<Integer, Set<Source>> listMap = new HashMap<Integer, Set<Source>>();

    public void commit(Source source1, Source source2, Rule why) {
        Integer masterId;
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
            masterId = availableMasterId;
            map.put(source1.id, availableMasterId);
            map.put(source2.id, availableMasterId);
            availableMasterId++;
        }

        if (listMap.get(masterId) == null) {
            listMap.put(masterId, new HashSet<Source>());
        }
        listMap.get(masterId).add(source1);
        listMap.get(masterId).add(source2);
    }
}
