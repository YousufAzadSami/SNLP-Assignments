package corpus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import probability.Probability;
import sort.SortMap;

public class InfoGenrator {

	private Map<String, Integer> wordsFerquency = new HashMap<>();
	int totalWordFerq = 0;
	private Map<String, Integer> tagsFerquency = new HashMap<>();
	int totalTagFerqency = 0;

	private Map<String, Integer> wordsSortedByFerquency = new HashMap<>();
	private Map<String, Integer> tagsSortedByFerquency = new HashMap<>();

	private Map<String, Map<String, Integer>> wordTagsFerquency = new HashMap<>();

	private Map<String, Integer> biTagsFerquency = new HashMap<>();
	
	private Map<String, Double> conditionalTagsprobability = new HashMap<>();
	private Map<String, Double> conditionalTagsSortedByProbability = new HashMap<>();
	
	private Map<String, Map<String, Double>> wordTagsProbability = new HashMap<>();
	private Map<String, Map<String, Double>> wordTagsSortedByProbability = new HashMap<>();

	public Map<String, Integer> getWordsFerquency() {
		return wordsFerquency;
	}

	public void setWordsFerquency(Map<String, Integer> wordsFerquency) {
		this.wordsFerquency = wordsFerquency;
	}

	public void setWordsFerquency(String key, Integer value) {
		this.wordsFerquency.put(key, value);
	}

	public int getTotalWordFerq() {
		return totalWordFerq;
	}

	public void setTotalWordFerq(int totalWordFerq) {
		this.totalWordFerq = totalWordFerq;
	}

	public Map<String, Integer> getTagsFerquency() {
		return tagsFerquency;
	}

	public void setTagsFerquency(Map<String, Integer> tagsFerquency) {
		this.tagsFerquency = tagsFerquency;
	}

	public void setTagsFerquency(String key, Integer value) {
		this.tagsFerquency.put(key, value);
	}

	public int getTotalTagFerqency() {
		return totalTagFerqency;
	}

	public void setTotalTagFerqency(int totalTagFerqency) {
		this.totalTagFerqency = totalTagFerqency;
	}

	public Map<String, Integer> getWordsSortedByFerquency() {
		return wordsSortedByFerquency;
	}

	public void setWordsSortedByFerquency(Map<String, Integer> wordsSortedByFerquency) {
		this.wordsSortedByFerquency = wordsSortedByFerquency;
	}

	public Map<String, Integer> getTagsSortedByFerquency() {
		return tagsSortedByFerquency;
	}

	public void setTagsSortedByFerquency(Map<String, Integer> tagsSortedByFerquency) {
		this.tagsSortedByFerquency = tagsSortedByFerquency;
	}

	public Map<String, Map<String, Integer>> getWordTagsFerquency() {
		return wordTagsFerquency;
	}

	public void setWordTagsFerquency(Map<String, Map<String, Integer>> wordTagsFerquency) {
		this.wordTagsFerquency = wordTagsFerquency;
	}

	public void setWordTagsFerquency(String pk, String vk, int value) {
		if (this.wordTagsFerquency.containsKey(pk)) {
			this.wordTagsFerquency.get(pk).put(vk, value);
		} else {
			this.wordTagsFerquency.put(pk, new HashMap<String, Integer>());
			this.wordTagsFerquency.get(pk).put(vk, value);
		}
	}

	public Map<String, Integer> getBiTagsFerquency() {
		return biTagsFerquency;
	}

	public void setBiTagsFerquency(Map<String, Integer> biTagsFerquency) {
		this.biTagsFerquency = biTagsFerquency;
	}
	
	public void setBiTagsFerquency(String key, Integer value) {
		this.biTagsFerquency.put(key, value);
	}

	public Map<String, Double> getConditionalTagsprobability() {
		return conditionalTagsprobability;
	}

	public void setConditionalTagsprobability(Map<String, Double> conditionalTagsprobability) {
		this.conditionalTagsprobability = conditionalTagsprobability;
	}
	
	public void setConditionalTagsprobability(String key, Double value) {
		this.conditionalTagsprobability.put(key, value);
	}

	public Map<String, Double> getConditionalTagsSortedByProbability() {
		return conditionalTagsSortedByProbability;
	}

	public void setConditionalTagsSortedByProbability(Map<String, Double> conditionalTagsSortedByProbability) {
		this.conditionalTagsSortedByProbability = conditionalTagsSortedByProbability;
	}

	public Map<String, Map<String, Double>> getWordTagsProbability() {
		return wordTagsProbability;
	}

	public void setWordTagsProbability(Map<String, Map<String, Double>> wordTagsProbability) {
		this.wordTagsProbability = wordTagsProbability;
	}
	
	public void setWordTagsProbability(String pk, String vk, double value) {
		if (this.wordTagsProbability.containsKey(pk)) {
			this.wordTagsProbability.get(pk).put(vk, value);
		} else {
			this.wordTagsProbability.put(pk, new HashMap<String, Double>());
			this.wordTagsProbability.get(pk).put(vk, value);
		}
	}

	public Map<String, Map<String, Double>> getWordTagsSortedByProbability() {
		return wordTagsSortedByProbability;
	}

	@SuppressWarnings("unchecked")
	public void setWordTagsSortedByProbability(Map<String, Map<String, Double>> wordTagsSortedByProbability) {
		for(Map.Entry<String, Map<String, Double>> en : wordTagsSortedByProbability.entrySet()) {
			this.wordTagsSortedByProbability.put(en.getKey(), SortMap.sortByValueAsc(en.getValue()));
		}
		
//		this.wordTagsSortedByProbability = wordTagsSortedByProbability;
	}

	@SuppressWarnings("unchecked")
	public InfoGenrator(List<TaggedSentence> corpus) {
		super();
		computeWordsFrequency(corpus);
		computeTotalWordFrequency();
		computeTagsFrequency(corpus);
		computeTotalTagFrequency();

		setWordsSortedByFerquency(SortMap.sortByValueAsc(getWordsFerquency()));
		setTagsSortedByFerquency(SortMap.sortByValueAsc(getTagsFerquency()));

		computeWordTagsFrequency(corpus);
		
		computeBiTagsFrequency(corpus);
		
		computeConditionalTagsProbability();
		setConditionalTagsSortedByProbability(getConditionalTagsprobability());
		
		computeWordTagProbability();
		setWordTagsSortedByProbability(getWordTagsProbability());
	}

	@Override
	public String toString() {
		
		String wordsFerquencyStr = "\t[\n";
		for(Map.Entry<String, Integer> entry : wordsFerquency.entrySet()) {
			wordsFerquencyStr += "\t\t" + entry.getKey() + "=" + entry.getValue() + "\n";
		}
		wordsFerquencyStr += "\n\t],\n";
		
		String tagsFerquencyStr = "\t[\n";
		for(Map.Entry<String, Integer> entry : tagsFerquency.entrySet()) {
			tagsFerquencyStr += "\t\t" + entry.getKey() + "=" + entry.getValue() + "\n";
		}
		tagsFerquencyStr += "\n\t],\n";
		
		String wordsSortedByFerquencyStr = "\t[\n";
		for(Map.Entry<String, Integer> entry : wordsSortedByFerquency.entrySet()) {
			wordsSortedByFerquencyStr += "\t\t" + entry.getKey() + "=" + entry.getValue() + "\n";
		}
		wordsSortedByFerquencyStr += "\n\t],\n";
		
		String tagsSortedByFerquencyStr = "\t[\n";
		for(Map.Entry<String, Integer> entry : tagsSortedByFerquency.entrySet()) {
			tagsSortedByFerquencyStr += "\t\t" + entry.getKey() + "=" + entry.getValue() + "\n";
		}
		tagsSortedByFerquencyStr += "\n\t],\n";
		
		String wordsTagsFerquencyStr = "\t[\n";
		for(Map.Entry<String, Map<String, Integer>> entry : wordTagsFerquency.entrySet()) {
			wordsTagsFerquencyStr += "\t\t" + entry.getKey() + "=[\n";
			for(Map.Entry<String, Integer> ent: entry.getValue().entrySet()) {
				wordsTagsFerquencyStr += "\t\t\t" + ent.getKey() + "=" + ent.getValue() + "\n";
			}
			wordsTagsFerquencyStr += "\n\t\t],\n";
		}
		wordsTagsFerquencyStr += "\n\t],\n";
		
		String biTagsFerquencyStr = "\t[\n";
		for(Map.Entry<String, Integer> entry : biTagsFerquency.entrySet()) {
			biTagsFerquencyStr += "\t\t" + entry.getKey() + "=" + entry.getValue() + "\n";
		}
		biTagsFerquencyStr += "\n\t],\n";		
		
		return "InfoGenrator [\n\twordsFerquency=" + wordsFerquencyStr + "\ttotalWordFerq=" + totalWordFerq
				+ ", tagsFerquency=" + tagsFerquencyStr + "\ttotalTagFerqency=" + totalTagFerqency
				+ ", wordsSortedByFerquency=" + wordsSortedByFerquencyStr + "\ttagsSortedByFerquency="
				+ tagsSortedByFerquencyStr + "\twordTagsFerquency=" + wordsTagsFerquencyStr + "\tbiTagsFerquency="
				+ biTagsFerquencyStr + "]";

		
//		return "InfoGenrator [wordsFerquency=" + wordsFerquency + ", totalWordFerq=" + totalWordFerq
//				+ ", tagsFerquency=" + tagsFerquency + ", totalTagFerqency=" + totalTagFerqency
//				+ ", wordsSortedByFerquency=" + wordsSortedByFerquency + ", tagsSortedByFerquency="
//				+ tagsSortedByFerquency + ", wordTagsFerquency=" + wordTagsFerquency + ", biTagsFerquency="
//				+ biTagsFerquency + ", conditionalTagsprobability=" + conditionalTagsprobability
//				+ ", conditionalTagsSortedByProbability=" + conditionalTagsSortedByProbability
//				+ ", wordTagsProbability=" + wordTagsProbability + ", wordTagsSortedByProbability="
//				+ wordTagsSortedByProbability + "]";
	}

	private void computeWordsFrequency(List<TaggedSentence> corpus) {
		for (TaggedSentence c : corpus) {
			List<String> tokens = c.tokens;
			for (String t : tokens) {
				if (getWordsFerquency().containsKey(t)) {
					setWordsFerquency(t, getWordsFerquency().get(t) + 1);
				} else {
					setWordsFerquency(t, 1);
				}
			}
		}
	}

	private void computeTotalWordFrequency() {
		for (Map.Entry<String, Integer> entry : getWordsFerquency().entrySet()) {
			setTotalWordFerq(getTotalWordFerq() + entry.getValue());
		}
	}

	private void computeTagsFrequency(List<TaggedSentence> corpus) {
		for (TaggedSentence c : corpus) {
			List<String> tags = c.tags;
			for (String t : tags) {
				if (getTagsFerquency().containsKey(t)) {
					setTagsFerquency(t, getTagsFerquency().get(t) + 1);
				} else {
					setTagsFerquency(t, 1);
				}
			}
		}
	}

	private void computeTotalTagFrequency() {
		for (Map.Entry<String, Integer> entry : getTagsFerquency().entrySet()) {
			setTotalTagFerqency(getTotalTagFerqency() + entry.getValue());
		}
	}

	private void computeWordTagsFrequency(List<TaggedSentence> corpus) {
		for (TaggedSentence sentence : corpus) {
			for (int i = 0; i < sentence.size(); i++) {
				if (getWordTagsFerquency().containsKey(sentence.tokens.get(i))) {
					if (getWordTagsFerquency().get(sentence.tokens.get(i)).containsKey(sentence.tags.get(i))) {
						setWordTagsFerquency(sentence.tokens.get(i), sentence.tags.get(i),
								getWordTagsFerquency().get(sentence.tokens.get(i)).get(sentence.tags.get(i)) + 1);
					} else {
						setWordTagsFerquency(sentence.tokens.get(i), sentence.tags.get(i), 1);
					}
				} else {
					setWordTagsFerquency(sentence.tokens.get(i), sentence.tags.get(i), 1);
				}
			}

		}
	}
	
	private void computeBiTagsFrequency(List<TaggedSentence> corpus) {
		for (TaggedSentence sentence : corpus) {
			for (int i = 0; i < sentence.size(); i++) {
				String k = "";
				if(i == 0) {
					k = "-1 " + sentence.tags.get(0);
					if (getBiTagsFerquency().containsKey(k)) {
						setBiTagsFerquency(k, getBiTagsFerquency().get(k) + 1);
					}
					else {
						setBiTagsFerquency(k, 1);
					}
				}
				else {
					k = sentence.tags.get(i - 1) + " " + sentence.tags.get(i);
					if (getBiTagsFerquency().containsKey(k)) {
						setBiTagsFerquency(k, getBiTagsFerquency().get(k) + 1);
					}
					else {
						setBiTagsFerquency(k, 1);
					}
				}
			}
		}
	}
	
	private void computeConditionalTagsProbability() {
		Map<String, Integer> btf = getBiTagsFerquency();
		Map<String, Integer> tf = getTagsFerquency();
		for (Map.Entry<String, Integer> entry : btf.entrySet()){
			double prob = Probability.computeProbability((double)entry.getValue(), (double)tf.get(entry.getKey().split(" ")[1]));
			setConditionalTagsprobability(entry.getKey(),prob);
		}
	}
	
	private void computeWordTagProbability() {
		Map<String, Map<String, Integer>> m = getWordTagsFerquency();
		Map<String, Integer> tf = getTagsFerquency();
		for(Map.Entry<String, Map<String, Integer>> en : m.entrySet()) {
			for(Map.Entry<String, Integer> ent : en.getValue().entrySet()) {
				double prob = Probability.computeProbability((double)ent.getValue(), (double)tf.get(ent.getKey()));
				setWordTagsProbability(ent.getKey(), en.getKey(), prob);
			}
		}
	}

}
