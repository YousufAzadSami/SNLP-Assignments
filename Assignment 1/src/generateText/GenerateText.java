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

        reader.read("../data/tiger_release_dec05-utf8.xml");

        List<TaggedSentence> corpus = reader.getCorpus();

        InfoGenrator infoGenrator = new InfoGenrator(corpus);

        System.out.println("Text generated using on the univariate word distribution:");
        int length = 5;
        for (int i = 0; i < length; i++) {
            String word = sampling.univariateSmple(infoGenrator.getWordsSortedByFerquency(), infoGenrator.getTotalWordFerq());
            System.out.print(word + " ");
        }

        System.out.println();
        System.out.println();

        System.out.println("Text generated using on the conditional word distribution:");
        for (int i = 0; i < length; i++) {
            String word;
            if (i == 0) {
                word = sampling.firstWord(infoGenrator.getConditionalTagsSortedByProbability(), infoGenrator.getWordTagsSortedByProbability());
            } else {
                word = sampling.conditionalSmple(infoGenrator.getConditionalTagsSortedByProbability(), infoGenrator.getWordTagsSortedByProbability());
            }
            System.out.print(word + " ");
        }

        System.out.println();
        System.out.println();

        System.out.println("2. Text generated using on the conditional word distribution:");
        for (int i = 0; i < length; i++) {
            String word;
            if (i == 0) {
                word = sampling.firstWord(infoGenrator.getConditionalTagsSortedByProbability(), infoGenrator.getWordTagsSortedByProbability());
            } else {
                word = sampling.conditionalSmple2(infoGenrator.getConditionalTagsSortedByProbability(), infoGenrator.getWordTagsSortedByProbability());
            }
            System.out.print(word + " ");
        }
    }

}
