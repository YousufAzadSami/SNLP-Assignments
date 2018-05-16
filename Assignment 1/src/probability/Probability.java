package probability;

import java.math.BigDecimal;

public class Probability {
	
	public BigDecimal computeProbability(int frequency, int totalFrequency) {
		BigDecimal prob = new BigDecimal((double)frequency/(double)totalFrequency);
		return prob;
	}
	
	public static Double computeProbability(double frequency, double totalFrequency) {
		Double prob = frequency/totalFrequency;
		return prob;
	}
	
//	public Map<String, BigDecimal> probAllWords(Map<String, Integer> wordFreq) {
//		Map<String, BigDecimal> allWordsProb = new HashMap<>();
//		for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
//			if(entry.getKey() != "total_freq") {
//				allWordsProb.put(entry.getKey(), probWord(entry.getKey(), wordFreq));
//			}
//		}
//		return allWordsProb;
//	}
}
