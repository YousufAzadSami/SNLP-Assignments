package smpling;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import probability.Probability;

public class Sampling {

	public String univariateSmple(Map<String, Integer> wordsSortedByFerquency, int totalWordFrequency) {
		Random rand = new Random();
		BigDecimal random = new BigDecimal((double) rand.nextFloat());
		BigDecimal sum = new BigDecimal(0);
		Probability probability = new Probability();
		for (Map.Entry<String, Integer> entry : wordsSortedByFerquency.entrySet()) {
			sum = sum.add(probability.computeProbability(entry.getValue(), totalWordFrequency));
			if (sum.subtract(random).compareTo(new BigDecimal(0)) >= 0) {
				return entry.getKey();
			}
		}
		return null;
	}

	public String conditionalSmple(Map<String, Double> conditionalTagsSortedByProbability,
			Map<String, Map<String, Double>> wordTagsSortedByProbability) {
		Random rand = new Random();
		BigDecimal random = new BigDecimal((double) rand.nextFloat());
		BigDecimal sum = new BigDecimal(0);
		for (Map.Entry<String, Double> entry : conditionalTagsSortedByProbability.entrySet()) {
			sum = sum.add(new BigDecimal(entry.getValue()));
			BigDecimal tpSum = new BigDecimal(0);
			if (sum.subtract(random).compareTo(new BigDecimal(0)) >= 0) {
				BigDecimal tp = new BigDecimal(entry.getValue());
				for (Map.Entry<String, Double> en : wordTagsSortedByProbability.get(entry.getKey().split(" ")[1])
						.entrySet()) {
					tpSum = tpSum.add(new BigDecimal(en.getValue()));
					if (tpSum.subtract(tp).compareTo(new BigDecimal(0)) >= 0) {
						return en.getKey();
					}
				}
				return null;// entry.getKey().split(" ")[1];
			}
		}
		return null;
	}

}
