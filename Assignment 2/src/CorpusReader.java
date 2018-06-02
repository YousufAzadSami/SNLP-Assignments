

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public interface CorpusReader {
	
	public void read(String file) throws IOException;
	
	public Iterator<TaggedSentence>  getIterator();
	
	public List<TaggedSentence> getSentences(int i, int j);
	
	public int size();
	
	public String toString();
	
}
