

import java.util.Set;

public interface BooleanFeature {

	Set<String> extractFeatures(TaggedSentence tagged_sentence, int i);
	
	public int max();
	
}
