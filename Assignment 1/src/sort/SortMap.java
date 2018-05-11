package sort;

import java.util.Map;
import java.util.TreeMap;

public class SortMap {
	
//	public static TreeMap<String, BigDecimal> sortMapByValueAsc(Map<String, BigDecimal> map){
//		Comparator<String> comparator = new ValueComparator(map);
//		//TreeMap is a map sorted by its keys. 
//		//The comparator is used to sort the TreeMap by keys.
//		TreeMap<String, BigDecimal> result = new TreeMap<String, BigDecimal>(comparator);
//		result.putAll(map);
//		return result;
//	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map sortByValueAsc(Map unsortedMap) {
		Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}
	
}
