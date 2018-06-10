import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException{
			
		TigerCorpusReader reader = new TigerCorpusReader();
		
		reader.read("/Users/cimiano/data/tigercorpus2/corpus/tiger_release_dec05.xml");
		
		BufferedWriter output = new BufferedWriter(new FileWriter("/Users/cimiano/data/tigercorpus2/corpus/tiger_release_dec05.xml.csv"));
        
		reader.toFile(output);
        
        output.close();
       
		TaggerEvaluator evaluator = new TaggerEvaluator();
			
		TaggedSentence sentence = new TaggedSentence();
		
		sentence.add("Der", "ART");
		sentence.add("Mann", "NN");
		sentence.add("ist","VBZ");
		sentence.add("toll","ADJ");
		
	    List<TaggedSentence> train = new ArrayList<TaggedSentence>();
	    
	    train.add(sentence);
	
	    train = reader.getSentences(0, 45000);
	     
	    POS_Tagger tagger = new MaxEntTagger();
	    
	    FeatureExtractor extractor = new FeatureExtractor();
	    
	    extractor.addFeature(new TokenFeatures());
	    extractor.addFeature(new PreviousTagFeature());
	    // extractor.addFeature(new SubstringFeatures());
	    
 
	    tagger.addExtractor(extractor);
	    
	   
	    List<TaggedSentence> development = new ArrayList<TaggedSentence>();
	    
		sentence = new TaggedSentence();
		
		sentence.add("Die", "ART");
		sentence.add("Frau", "NN");
		sentence.add("ist","VBZ");
		sentence.add("entzückend","ADJ");
	    
		development.add(sentence);
		
		development = reader.getSentences(45001, 50000);
		
	    tagger.train(train,development,0.05,100,10);
	       
	    List<TaggedSentence> test = new ArrayList<TaggedSentence>();
	    
	    sentence = new TaggedSentence();
		
		sentence.add("Der", "ART");
		sentence.add("Mann", "NN");
		sentence.add("ist","VBZ");
		sentence.add("entzückend","ADJ");
		
		test.add(sentence);
	
		test = reader.getSentences(50001, reader.size());
		
	    evaluator.evaluate(tagger, test);
	    
	    System.out.print(evaluator.getStatistics());
	    
	 
	}

}
