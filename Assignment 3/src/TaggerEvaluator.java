

import java.util.HashMap;
import java.util.List;

public class TaggerEvaluator {

		
	HashMap<String,Integer> tag_gold_freq;
	HashMap<String,Integer> tag_pred_freq;
	
	HashMap<String,Integer> tag_gold_correct;
	HashMap<String,Integer> tag_pred_correct;
	
	double accuracy;
	
	
	public TaggerEvaluator()
	{
	}
	
	public String getStatistics()
	{
		String string = "";
		
		string += "Accuracy: "+ accuracy +"\n";
		
		string+= "#tags: " + tag_gold_freq.size()+"\n";
		
		for (String tag: tag_pred_freq.keySet())
		{
			if (tag_pred_correct.containsKey(tag))
			
			string += "Precision("+tag+"): "+tag_pred_correct.get(tag).doubleValue() +"/"+ tag_pred_freq.get(tag).doubleValue()+"\n";
			
			else string+= "Precision("+tag+"):"+"0" + "/" + tag_pred_freq.get(tag)+"\n";
			
		}
		
		for (String tag: tag_gold_freq.keySet())
		{
			
			if (tag_gold_correct.containsKey(tag))
			
			string += "Recall("+tag+"): "+tag_gold_correct.get(tag).doubleValue() +"/"+ tag_gold_freq.get(tag).doubleValue()+"\n";
		
			else string+= "Recall("+tag+"): 0"+"/"+tag_gold_freq.get(tag)+"\n";
		
		}
		
		
		return string;
	}
	
	
	public double evaluate(POS_Tagger tagger, List<TaggedSentence> tagged_sentences)
	{
		
		int correct = 0, total = 0;
		
		tag_gold_freq = new HashMap<String,Integer>();
		tag_pred_freq = new HashMap<String,Integer>();
		tag_gold_correct = new HashMap<String,Integer>();
		tag_pred_correct = new HashMap<String,Integer>();
	
		
		TaggedSentence tagged_sentence;
		
		System.out.print("Evaluating on "+tagged_sentences.size()+" sentences!\n");
		
		for (int i=0; i < tagged_sentences.size(); i++)
		{
			tagged_sentence = tagged_sentences.get(i);
			
			// System.out.println("Evaluating sentence: "+tagged_sentence);
		
			TaggedSentence prediction = tagger.predict(new Sentence(tagged_sentence)); 
		
			// System.out.println("Prediction: "+prediction);
			
			for (int j=0; j < tagged_sentence.size(); j++)
			{
				total++;
				
				if (tagged_sentence.getPOS(j).equals(prediction.getPOS(j)))
				{
					correct ++;
					
					update(tag_gold_correct,tagged_sentence.getPOS(j));
					update(tag_pred_correct,prediction.getPOS(j));
					
					
				}
				else
				{
					if (tagged_sentence.getPOS(j).equals("NE") )
					{
						//System.out.println(tagged_sentence.getToken(j)+"["+tagged_sentence.getPOS(j)+"]");
						//System.out.println(prediction.getToken(j)+"["+prediction.getPOS(j)+"]");
						//System.out.print(extractor.extractFeatures(tagged_sentence,j));
					}
					
				}
				
				
				
				update(tag_gold_freq,tagged_sentence.getPOS(j));
				update(tag_pred_freq,prediction.getPOS(j));
				
			}
			
		}
		
		accuracy = (double) correct / (double) total;
				
		return accuracy;
		
	}


	private void update(HashMap<String, Integer> map, String tag) {
		
		Integer value;
		
		if (map.containsKey(tag))
		{
			value = map.get(tag);
			
			map.put(tag, value+1);
			
		}
		else
		{
			map.put(tag, 1);
		}
		
		
	}
	
}
