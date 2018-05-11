package corpus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sort.SortMap;

public class InfoGenrator {

	private Map<String, Integer> wordsFerquency = new HashMap<>();
	int totalWordFerq = 0;
	private Map<String, Integer> tagsFerquency = new HashMap<>();
	int totalTagFerqency = 0;

	private Map<String, Integer> wordsSortedByFerquency = new HashMap<>();
	private Map<String, Integer> tagsSortedByFerquency = new HashMap<>();

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

	@SuppressWarnings("unchecked")
	public InfoGenrator(List<TaggedSentence> corpus) {
		super();
		computeWordsFrequency(corpus);
		computeTotalWordFrequency();
		computeTagsFrequency(corpus);
		computeTotalTagFrequency();
		setWordsSortedByFerquency(SortMap.sortByValueAsc(getWordsFerquency()));
		setTagsSortedByFerquency(SortMap.sortByValueAsc(getTagsFerquency()));
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

}
