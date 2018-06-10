

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeatureExtractor {

	List<BooleanFeature> extractors;
	
	public FeatureExtractor()
	{
		extractors = new ArrayList<BooleanFeature>();	
	}
	
	
	public void addFeature(BooleanFeature extractor) {
		this.extractors.add(extractor);
		
	}

	
	public Set<String> extractFeatures(TaggedSentence sentence, int i) {
		
		Set<String> feats = new HashSet<String>();
		
		for (BooleanFeature extractor: extractors)
		{
			for (String feat: extractor.extractFeatures(sentence,i))
			{
				feats.add(feat);
			}
			
		}
			
		return feats;
		
	}	
}
	
	
