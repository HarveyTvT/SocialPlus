package utils;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by lizhuoli on 15/9/17.
 */
public class ValueComparator implements Comparator<String> {
    private Map<String,Integer> map;
    public ValueComparator(Map<String, Integer> map){
        this.map = map;
    }
    @Override
    public int compare(String o1, String o2) {
        return map.get(o1).compareTo(map.get(o2));
    }
}
