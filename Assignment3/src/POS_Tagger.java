

import java.io.IOException;
import java.util.List;

public interface POS_Tagger {

	public void train(List<TaggedSentence> train, List<TaggedSentence> dev, double alpha, int batch_size, int epochs);
	
	public TaggedSentence predict(Sentence sentence);
	
	public void addExtractor(FeatureExtractor featureExtrator);
		
}
