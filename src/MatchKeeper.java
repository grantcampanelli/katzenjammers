import model.Master;
import model.Source;

import javax.print.attribute.IntegerSyntax;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Mitch Fierro on 5/31/2015.
 */
public class MatchKeeper
{
    Integer availableMasterId = 0;
    //maps source ID's to Master Id's
    private Map<Integer, Integer> map = new HashMap<Integer, Integer>();   // cross walk - source to master
    private Map<Integer, Set<Source>> listMap = new HashMap<Integer, Set<Source>>(); // maps master ID to set of sources
    private Map<Map.Entry<Integer, Integer>, String> auditMap = new HashMap<Map
        .Entry<Integer, Integer>, String>();
    private static MatchKeeper instance = null;

    private MatchKeeper() {

    }
    public static MatchKeeper getInstance() {
        if (instance == null)
            instance = new MatchKeeper();
        return instance;
    }
//    for(Entry<String, HashMap> entry : selects.entrySet()) {
//    String key = entry.getKey();
//    HashMap value = entry.getValue();
//
//    // do what you have to do here
//    // In your case, an other loop.
//}
    /**
     * Saves a match.  Associates each source with a master ID,
     * and also associates each masterId with a set of sources (same data,
     * different views on it)
     * @param source1
     * @param source2
     * @param why
     */
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
        auditMap.put(new AbstractMap.SimpleEntry<Integer, Integer>(source1.id,
            source2.id), why.description);
    }

    /**
     * Grant needs to call this method, then create insert table statements and shit.
     * @return
     */
    public List<Master> getMasters() {
        // build list of masters
        ArrayList<Master> masterList = new ArrayList<Master>();
        for (Map.Entry<Integer, Set<Source>> entry : listMap.entrySet()) {
            masterList.add(MasterDecider.master(entry.getKey(), entry.getValue()));
        }

        return masterList;
    }

    public Map<Integer, Integer> getCrossWalkMap() {
        return map;
    }
}
