
import java.util.HashSet;
import java.util.Set;

public class TokenFeatures implements BooleanFeature{

	public Set<String> extractFeatures(TaggedSentence tagged_sentence, int i) {
	
		Set<String> features = new HashSet<String>();
		
		String token = tagged_sentence.getToken(i);
		// consider adding only the most frequent tokens
		
		//features.add(token);
		
		 if (i >= 0 && i < tagged_sentence.size()) features.add("w="+tagged_sentence.getToken(i));
			
		 // if (i < tagged_sentence.size()-1) features.add("w+1="+tagged_sentence.getToken(i+1));
		
		return features;
		
	}
	
	public int max()
	{
		return 3;
	}
		
}
