

import java.util.ArrayList;
import java.util.List;

public class Sentence {

	List<String> tokens;
	
	public Sentence()
	{
		tokens = new ArrayList<String>();
	}
	
	public Sentence(TaggedSentence tagged_sentence) {
	
		tokens = new ArrayList<String>();
		
		for (int i = 0 ; i < tagged_sentence.size(); i++)
		{
			tokens.add(tagged_sentence.getToken(i));
		}
		
	}

	public void add(String token)
	{
		tokens.add(token);
	}
	
	String getToken(int i)
	{
		return tokens.get(i);
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
			string += tokens.get(i) +" ";
		}
		return string;
	}
	
}
