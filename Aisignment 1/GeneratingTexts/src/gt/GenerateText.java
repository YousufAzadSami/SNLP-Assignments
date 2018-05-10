package gt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.lang.Math;
import java.math.BigDecimal;

import javax.crypto.Mac;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class GenerateText {
	
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub

		GenerateText generateText = new GenerateText();
		
		TigerCorpusReader reader = new TigerCorpusReader();
		
		reader.read("data/tiger_release_dec05.xml");
		
		List<TaggedSentence> corpus = reader.getCorpus();
		Map<String, Integer> wordFreq = generateText.word_freq(corpus);
		
		Map<String, BigDecimal> allWordsProb = generateText.probAllWords(wordFreq);
		TreeMap<String, BigDecimal> sortedAllWordsProb = generateText.sortMapByValue(allWordsProb);
		
		int length = 5;
		for (int i=0; i < length; i++)
		{
			String word = generateText.smple(sortedAllWordsProb);
			System.out.print(word + " ");
		}
		
		//generateText.probAllWords(wordFreq);
		
		/*List<TaggedSentence> sample = reader.getSentences(10, 12);
		
		for (TaggedSentence sentence: sample)
		{
			for (int i=0; i < sentence.size(); i++)
			{
				System.out.print(sentence.getToken(i) + "\t"+sentence.getPOS(i)+"\n");
			}
		}*/
	}
	
	public Map<String, Integer> word_freq(List<TaggedSentence> corpus){
		Map<String, Integer> wordFreq = new HashMap<>();
		for(TaggedSentence c : corpus) {
			List<String> tokens = c.tokens;
			for(String t : tokens) {
				if (wordFreq.containsKey(t)) {
					wordFreq.put(t, wordFreq.get(t)+1); 
				} else {
					wordFreq.put(t, 1);
				}				
			}
		}
		int totalFerq = 0;
		for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
			totalFerq += entry.getValue();
		}
		wordFreq.put("total_freq", totalFerq);
		
		return wordFreq;
	}
	
	public BigDecimal probWord(String word, Map<String, Integer> wordFreq) {
		BigDecimal prob = new BigDecimal((double)wordFreq.get(word)/(double)wordFreq.get("total_freq"));
		return prob;
	}
	
	public Map<String, BigDecimal> probAllWords(Map<String, Integer> wordFreq) {
		Map<String, BigDecimal> allWordsProb = new HashMap<>();
		for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
			if(entry.getKey() != "total_freq") {
				allWordsProb.put(entry.getKey(), probWord(entry.getKey(), wordFreq));
			}
		}
		return allWordsProb;
	}
	
	public int maxProbWord(Map<String, Integer> wordFreq) {
		int max = 0;
		for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
			if (max < entry.getValue() && entry.getKey() != "total_freq") {
				max = entry.getValue();
			}
		}
		
		for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
			if (entry.getValue() == 43682) {
				System.out.println(entry.getKey());
			}
		}
		return max;
	}
	
	public String smple(Map<String, BigDecimal> allWordsProb) {
		Random rand = new Random();
		BigDecimal random = new BigDecimal((double)rand.nextFloat());
		BigDecimal sum = new BigDecimal(0);
		
		for(Map.Entry<String, BigDecimal> entry : allWordsProb.entrySet()) {
			sum = sum.add(entry.getValue());
			if(sum.subtract(random).compareTo(new BigDecimal(0)) >= 0) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public static TreeMap<String, BigDecimal> sortMapByValue(Map<String, BigDecimal> map){
		Comparator<String> comparator = new ValueComparator(map);
		//TreeMap is a map sorted by its keys. 
		//The comparator is used to sort the TreeMap by keys. 
		TreeMap<String, BigDecimal> result = new TreeMap<String, BigDecimal>(comparator);
		result.putAll(map);
		return result;
	}
	
}
