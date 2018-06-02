

import java.io.IOException;
import java.util.List;

public interface POS_Tagger {

	public void train(List<TaggedSentence> sentences) throws IOException, InterruptedException;
	
	public TaggedSentence predict(Sentence sentence);
		
}
