

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TigerCorpusReader implements CorpusReader{

	List<TaggedSentence> Corpus;
	
	public TigerCorpusReader()
	{
		Corpus = new ArrayList<TaggedSentence>();
	}
	
	public void read(String file) throws IOException  {
			
		
		TaggedSentence sentence = null;
		
		try{
			  // Open the file that is the first 
			  // command line parameter
			  FileInputStream fstream = new FileInputStream(file);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  String token, tag;
			  //Read File Line By Line
			  
			  Pattern p = Pattern.compile("\\s*<t id=\"(.*?)\" word=\"(.*?)\" lemma=\"(.*?)\" pos=\"(.*?)\".*");
			  Matcher matcher;
			  
			  while ((strLine = br.readLine()) != null)   {
				  // Print the content on the console
				  
				  if (strLine.matches("\\s*<terminals>\\s*"))
				  {
					  sentence = new TaggedSentence();
				  }
				  
				  matcher = p.matcher(strLine);
				  
			      if (matcher.find())
			      {
			    	  token = matcher.group(2);
			    	  tag = matcher.group(4);
			    	  sentence.add(token, tag);
			      }
				  
				  if (strLine.matches("\\s*</terminals>\\s*")) 
				  {	 
					  Corpus.add(sentence);
					  // System.out.println(sentence);
				  }
		
			  }
				
			  //Close the input stream
			  in.close();
			    } catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		}
	

	public Iterator<TaggedSentence> getIterator() {
		return Corpus.iterator();
	}
	
	public int size()
	{
		return Corpus.size();
	}
	
	public List<TaggedSentence> getSentences(int i, int j)
	{
		return Corpus.subList(i, j-1);
	}
	
	public void toFile(BufferedWriter out) throws IOException
	{
		
		for (TaggedSentence tg: Corpus)
		{
			out.write(tg.toString()+"\n");
		}
		
		out.flush();
	}
	
	public String toString()
	{
		String string = "";
		
		for (TaggedSentence tg: Corpus)
		{
			string += tg.toString()+"\n";
		}
		
		return string;
	}

	
	
}
