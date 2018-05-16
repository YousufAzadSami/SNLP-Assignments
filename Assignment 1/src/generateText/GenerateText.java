package generateText;

import java.util.List;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import corpus.InfoGenrator;
import corpus.TaggedSentence;
import corpus.TigerCorpusReader;
import smpling.Sampling;

public class GenerateText {
	
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub

		Sampling sampling = new Sampling();
		
		TigerCorpusReader reader = new TigerCorpusReader();
		
		reader.read("../data/tiger_release_dec05.xml");
		
		List<TaggedSentence> corpus = reader.getCorpus();
		
		InfoGenrator infoGenrator = new InfoGenrator(corpus);
		
		System.out.println("Text generated using on the univariate word distribution:");
		int length = 5;
		for (int i=0; i < length; i++)
		{
			String word = sampling.univariateSmple(infoGenrator.getWordsSortedByFerquency(), infoGenrator.getTotalWordFerq());
			System.out.print(word + " ");
		}

		System.out.println();

		System.out.println("Text generated using on the conditional word distribution:");
		for (int i=0; i < length; i++)
		{
			String tag = sampling.conditionalSmple(infoGenrator.getConditionalTagsSortedByProbability(), infoGenrator.getWordTagsSortedByProbability());
			System.out.print(tag + " ");
		}
		
		/*List<TaggedSentence> sample = reader.getSentences(10, 12);
		
		for (TaggedSentence sentence: sample)
		{
			for (int i=0; i < sentence.size(); i++)
			{
				System.out.print(sentence.getToken(i) + "\t"+sentence.getPOS(i)+"\n");
			}
		}*/
	}

}
