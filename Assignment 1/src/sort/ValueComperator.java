package sort;

import java.util.Comparator;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
class ValueComparator implements Comparator {
	
	
	Map map;
 
	public ValueComparator(Map map) {
		this.map = map;
	}
 
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		if (valueA.compareTo(valueB) == 0) {
			return 1;
		}
		return valueA.compareTo(valueB);
	}
	
}
