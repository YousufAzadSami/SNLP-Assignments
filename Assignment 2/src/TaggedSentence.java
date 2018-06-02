

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaggedSentence {

	List<String> tokens;
	List<String> tags;
	
	double prob =0;
	
	public TaggedSentence()
	{
		tokens = new ArrayList<String>();
		tags = new ArrayList<String>();
	}
	
	public TaggedSentence(Sentence sentence) {
		
		tokens = new ArrayList<String>();
		tags = new ArrayList<String>();
		
		for (int i = 0; i < sentence.size(); i++)
		{
			tokens.add(sentence.getToken(i));
			tags.add("unkwn");
		}
		
	}

	public TaggedSentence(TaggedSentence tagged_sentence) {
		
		tokens = new ArrayList<String>();
		tags = new ArrayList<String>();
		
		for (int i = 0; i < tagged_sentence.size(); i++)
		{
			tokens.add(tagged_sentence.getToken(i));		
		}
		
		int i=0;
		
		String tag = tagged_sentence.getPOS(i);
		
		while (tag != null)
		{
			tags.add(tag);
			
			tag = tagged_sentence.getPOS(++i);
		}
			
		
		
		
	}
	
	public double getProb()
	{
	   return prob;
	}
	
	public void setProb(double prob)
	{
	   this.prob = prob;
	}
	   

	public void add(String token, String tag)
	{
		tokens.add(token);
		tags.add(tag);
	}
	
	String getToken(int i)
	{
		return tokens.get(i);
	}
	
	String getPOS(int i)
	{
		if (i < tags.size())
		
		return tags.get(i);
		
		else return null;
	}
	
	int size()
	{
		return tokens.size();
	}
	
	
	public String toString()
	{
		String string = "";
		for (int i=0; i < tokens.size(); i++)
		{
			if (i < tags.size())
			
			string += tokens.get(i) +"["+tags.get(i)+"] ";
			
			else
				
			string += tokens.get(i) +"[?] ";	
			
		}
		return string; // + "(" + prob +")";
	}

	public int evaluate(TaggedSentence tagged_sentence) {
		
		int correct=0;
		
		for (int i=0; i < tags.size(); i++)
		{
			if (tags.get(i).equals(tagged_sentence.getPOS(i)))
			{
				correct++;
			}
				
		}
		
		return correct;
	}

	
	public void reverse()
	{
		Collections.reverse(tags);
		Collections.reverse(tokens);
		
	}

	public void addTag(String tag) {
		tags.add(tag);
		
	}

	public void setTag(int i, String tag) {
		tags.set(i, tag);
	}

	public boolean greater(TaggedSentence left) {
		
		if (this.getProb() > left.getProb()) return true;

		return false;
	}

	public int noPos() {
		return tags.size();
	}
	
}
