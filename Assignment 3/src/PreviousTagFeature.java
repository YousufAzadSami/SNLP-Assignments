

import java.util.HashSet;
import java.util.Set;

public class PreviousTagFeature implements BooleanFeature{

	public Set<String> extractFeatures(TaggedSentence tagged_sentence, int i) {
		
		Set<String> features = new HashSet<String>();
		
		if (i > 0)
		{
			features.add("POS-1="+tagged_sentence.getPOS(i-1));
			
		}
		
		if (i==0) features.add("POS-1=start");
		
		return features; 
	}
	
	public int max()
	{
		return 1;
	}
	
	
}
