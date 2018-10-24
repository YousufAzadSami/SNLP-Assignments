import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class LDA_Computation {

	int no_topics;
	
	int no_words;
	
	int[] alpha;
	
	int[] beta;
	
	List<List<String>> words;
	
	List<List<Integer>> topics;
	
	Map<Integer,Map<Integer,Integer>> n_d_k;
	
	Map<Integer,Map<String,Integer>> n_w_k;
	
	Map<Integer,Integer> n_k;

	public LDA_Computation(int no_topics, int no_words, int[] alpha, int[] beta) {
		super();
		this.no_topics = no_topics;
		this.no_words = no_words;
		this.alpha = alpha;
		this.beta = beta;
		
		words = new ArrayList<List<String>>();
		topics = new ArrayList<List<Integer>>();
		
		n_d_k= new HashMap<Integer,Map<Integer,Integer>>();
		n_w_k = new HashMap<Integer,Map<String,Integer>>();
		n_k = new HashMap<Integer,Integer>();
			
	}
	
	public void sample(int T)
	{
		initializeTopics();
		String word;
		Integer topic;
		
		for (int t =0; t < T; t++)
		{
			System.out.print("Iteration: "+(t+1)+"\n");
			
			// printTopicAssignments();
			
			for (int i=0; i < words.size(); i++)
			{
				List<String> doc = words.get(i);
				
				for (int j=0; j < doc.size(); j++)
				{
					word = doc.get(j);
					topic = topics.get(i).get(j);
					
					// decrease counts
					
					for (int k=0; k < no_topics; k++)
					{
						// compute topic distribution
						// sample from topic distribution
						// recompute counts
					}
			
				}
			}
		}
	}
	
	public void addDocument(List<String> document)
	{
		words.add(document);
	}
	
	public void initializeTopics()
	{
		int random;
		
		List<Integer> topic_list = null;
		List<String> word_list;
		
		Random rn = new Random();
				
		for (int i = 0; i < words.size(); i++)
		{
			topic_list = new ArrayList<Integer>();
			word_list = words.get(i);
			
			for (int j=0; j < word_list.size(); j++)
			{
				random = rn.nextInt(no_topics);
				topic_list.add(new Integer(random));
				
				updateTopic(new Integer(random),1);
				updateWordTopic(word_list.get(j),new Integer(random),1);
				updateDocumentTopic(new Integer(i+1),new Integer(random),1);
				
			}
			topics.add(topic_list);	
		}
		
	}
	
	public void printTopicAssignments()
	{
		List<Integer> topic_list;
		List<String> word_list;
		
		for (int i = 0; i < words.size(); i++)
		{
			System.out.print("Document: "+(i+1)+": ");
			
			topic_list = topics.get(i);
			word_list = words.get(i);
			
			for (int j=0; j < word_list.size(); j++)
			{
				System.out.print(word_list.get(j)+"/"+topic_list.get(j)+"\t");
				
			}
			System.out.print("\n");
		}
	}
	
	public void printPhi()
	{
		Map<String,Integer> map;
		double total = 0;
		double value;
		
		System.out.print("Printing Word Topic Distribution Phi\n");
		
		for (int i= 0; i < no_topics; i++)
		{
			total = 0;
			
			System.out.print("Topic "+i+"\n");
			
			map = n_w_k.get(i);
			
			for (String word: map.keySet())
			{
				total += (double) map.get(word);
			}
			
			for (String word: map.keySet())
			{
				value = (double) map.get(word);
				value = value / total;
				System.out.print(word+" -> "+ value+"\t");
			}
			
			System.out.print("\n");
			
		}
	}
	
	public void printTheta()
	{
		Map<Integer,Integer> map;
		double total = 0;
		double value;
		
		System.out.print("Printing Document Topic Distribution Theta\n");
		
		for (int i= 0; i < words.size(); i++)
		{
			total = 0;
			
			System.out.print("Document "+i+"\n");
			
			map = n_d_k.get(i+1);
			
			for (Integer topic: map.keySet())
			{
				total += (double) map.get(topic);
			}
			
			for (Integer topic: map.keySet())
			{
				value = (double) map.get(topic);
				value = value / total;
				System.out.print(topic+" -> "+ value+"\t");
			}
			
			System.out.print("\n");
			
		}
		
		
	}
	
	public void updateTopic(Integer topic, Integer delta)
	{
		Integer value;
		
		if (n_k.containsKey(topic))
		{
			value = n_k.get(topic);
			n_k.put(topic, value+delta);
		}
		else
		{
			n_k.put(topic, delta);
		}
	}
	
	public void updateWordTopic(String word, Integer topic, Integer delta)
	{
		Map<String,Integer> map;
		Integer value;
		
		if (n_w_k.containsKey(topic))
		{
			map = n_w_k.get(topic);
		}
		else
		{
			map = new HashMap<String,Integer>();
			n_w_k.put(topic, map);
		}
		
		if (map.containsKey(word))
		{
			value = map.get(word);
			value = value+delta;
		}
		else
		{
			value = 1;
		}
		map.put(word, value);
		
	}
	
	public void updateDocumentTopic(Integer doc, Integer topic, Integer delta)
	{
		Map<Integer,Integer> map;
		Integer value;
		
		if (n_d_k.containsKey(doc))
		{
			map = n_d_k.get(doc);
		}
		else
		{
			map = new HashMap<Integer,Integer>();
			n_d_k.put(doc, map);
		}
		
		if (map.containsKey(topic))
		{
			value = map.get(topic);
			value = value+delta;
		}
		else
		{
			value = 1;
		}
		map.put(topic, value);
		
		
	}
	
	
}
