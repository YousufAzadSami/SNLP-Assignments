package smpling;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import probability.Probability;

public class Sampling {

    public String univariateSmple(Map<String, Integer> wordsSortedByFerquency, Integer totalWordFrequency) {
        Random rand = new Random();
        double random = (double) rand.nextFloat();
        double sum = 0.0;
        for (Map.Entry<String, Integer> entry : wordsSortedByFerquency.entrySet()) {
            sum += Probability.computeProbability(entry.getValue().doubleValue(), totalWordFrequency.doubleValue());
            if (sum - random >= 0.0) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String conditionalSmple(Map<String, Double> conditionalTagsSortedByProbability,
                                   Map<String, Map<String, Double>> wordTagsSortedByProbability) {
        Random rand = new Random();
        double random = (double) rand.nextFloat();
        double sum = 0.0;
        for (Map.Entry<String, Double> entry : conditionalTagsSortedByProbability.entrySet()) {
            for (Map.Entry<String, Double> en : wordTagsSortedByProbability.get(entry.getKey().split(" ")[1])
                    .entrySet()) {
                sum += (entry.getValue()*en.getValue());
                if (sum-random >= 0) {
                    return en.getKey();
                }
            }
            return null;// entry.getKey().split(" ")[1];

        }
        return null;
    }

}
