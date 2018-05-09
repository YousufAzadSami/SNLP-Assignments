package gt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class GenerateText {
	
	//TigerCorpusReader reader = new TigerCorpusReader();
	
	
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub

		GenerateText generateText = new GenerateText();
		
		TigerCorpusReader reader = new TigerCorpusReader();
		
		reader.read("data/tiger_release_dec05.xml");
		
		List<TaggedSentence> corpus = reader.getCorpus();
		Map<String, Integer> wordFreq = generateText.word_freq(corpus);
		generateText.maxProbWord(wordFreq);
		
		System.out.println(generateText.probWord(",",wordFreq));
		
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
//			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		wordFreq.put("total_freq", totalFerq);
		
		return wordFreq;
	}
	
	public double probWord(String word, Map<String, Integer> wordFreq) {
		double prob = wordFreq.get(word)/wordFreq.get("total_freq")*1.00000000;
		//System.out.println(wordFreq.get("total_freq"));
		return prob;
	}
	
	public Map<String, Double> probAllWords(Map<String, Integer> wordFreq) {
		Map<String, Double> allWordsProb = new HashMap<>();
		for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
			allWordsProb.put(entry.getKey(), probWord(entry.getKey(), wordFreq));
		}
		
		for (Map.Entry<String, Double> entry : allWordsProb.entrySet()) {
			//System.out.println(entry.getKey() + ": " + entry.getValue());
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
		
		System.out.println("asdasdasdgjhg "+max);
		return max;
	}
}
