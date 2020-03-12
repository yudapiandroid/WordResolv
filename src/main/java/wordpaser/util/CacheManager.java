package wordpaser.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    private static ConcurrentHashMap<String, Map<String, Object>> data
            = new ConcurrentHashMap<String, Map<String, Object>>();


    private static final String TIME_KEY = "__Time";


    private static void refreshData() {
        Iterator<Map.Entry<String, Map<String, Object>>> it
                = data.entrySet().iterator();
        List<String> keys = new ArrayList<String>();
        while (it.hasNext()) {
            Map.Entry<String, Map<String, Object>> next = it.next();
            Map<String, Object> map = next.getValue();
            if(map.get(TIME_KEY) instanceof Long){
                Long l = (Long) map.get(TIME_KEY);
                if(System.currentTimeMillis() - l > 24 * 60 * 60 * 1000){
                    keys.add(next.getKey());
                }
            }
        }
        for (String k : keys) {
            data.remove(k);
        }
    }

    public synchronized static void putCache(String key, Map<String, Object> d) {
        d.put(TIME_KEY, new Long(System.currentTimeMillis()));
        data.put(key, d);
        refreshData();
    }

    public synchronized static Map<String, Object> getCache(String key) {
        refreshData();
        return data.get(key);
    }

}
