package smpling;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import probability.Probability;

public class Sampling {
	
	public String smple(Map<String, Integer> wordsSortedByFerquency, int totalWordFrequency) {
		Random rand = new Random();
		BigDecimal random = new BigDecimal((double)rand.nextFloat());
		BigDecimal sum = new BigDecimal(0);
		Probability probability = new Probability();
		for(Map.Entry<String, Integer> entry : wordsSortedByFerquency.entrySet()) {
			sum = sum.add(probability.computeIndividualWordProbability(entry.getKey(), entry.getValue(), totalWordFrequency));
			if(sum.subtract(random).compareTo(new BigDecimal(0)) >= 0) {
				return entry.getKey();
			}
		}
		return null;
	}

}
