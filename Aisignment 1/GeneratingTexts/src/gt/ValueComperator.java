package gt;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

class ValueComparator implements Comparator {
	Map map;
 
	public ValueComparator(Map map) {
		this.map = map;
	}
 
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		if (valueA.compareTo(valueB) == 0 && valueA.compareTo(new BigDecimal(0)) >0) {
//			System.out.println(valueA.compareTo(valueB));
			return valueA.compareTo(new BigDecimal(0));
		}
		else if (valueA.compareTo(valueB) == 0 && valueA.compareTo(new BigDecimal(0)) == 0) {
			return valueA.compareTo(new BigDecimal(-1));
		}
		return valueA.compareTo(valueB);
	}
}
