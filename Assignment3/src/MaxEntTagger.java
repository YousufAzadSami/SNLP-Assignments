import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaxEntTagger implements POS_Tagger{

	LoglinearModel model;
	
	double[] empiricalCounts;
	
	// List<Set<String>> featureList;
	List<String> labels;
	
	FeatureExtractor extractor;
	
	
	public MaxEntTagger()
	{	
		model = new LoglinearModel();
		
		// featureList = new ArrayList<Set<String>>();
		labels = new ArrayList<String>();

	}



	public TaggedSentence predict(Sentence sentence) {
		
		TaggedSentence tagged_sentence = new TaggedSentence(sentence);
		
		Set<String> features;
		
		double max = 0.0;
		
		double prob = 0.0;
		
		String max_tag = null;
		
		
		for (int i=0; i < tagged_sentence.size(); i++)
		{
			
			max = 0;
			
			features = extractor.extractFeatures(tagged_sentence, i);
			
		
			for (String tag: this.model.getLabels())
			{
				
				prob = model.computeProb(features,tag);
				
				if (prob > max)
				{
					max = prob;
					max_tag = tag;
				}
				
			}
			tagged_sentence.setTag(i, max_tag);
			
		}
		
		return tagged_sentence;
		
		
	}

	public void addExtractor(FeatureExtractor featureExtractor) {
		extractor = featureExtractor;
		
	}
	

	
	double[] empiricalCounts(List<TaggedSentence> data)
	{
		double[] empiricalCounts = new double[model.size()];
		
		for (TaggedSentence sentence: data)
		{
			for (int i=0; i < sentence.size(); i++)
			{

				Set<String> features = extractor.extractFeatures(sentence, i);
				
				for (String feature: features)
				{
					updateEmpiricalCounts(empiricalCounts, feature, sentence.getPOS(i), new Integer(1));
					
				}
				
			}
		}
		// System.out.print("Empirical Counts:\n");
		// print(empiricalCounts);
		return empiricalCounts;
		
	}
	

	private void print(double[] array) {
		for (int i = 0;  i < array.length; i++)
		{
			System.out.println(this.model.getFeatureName(i)+": "+array[i]);
		}
		
	}


	private void updateEmpiricalCounts(double[] counts, String feature, String label, Integer value) {
		
		if (model.containsFeature(feature,label)) 
		{
			counts[model.getFeatureIndex(feature,label).intValue()] = counts[model.getFeatureIndex(feature,label).intValue()] + value.doubleValue(); 
		}		
	}

	double[] modelExpectations(double[] model, List<TaggedSentence> data)
	{
		// System.out.print("Computing model expectations!\n");
	
		List<Set<String>> featureList = new ArrayList<Set<String>>();
		
		this.model.updateModel(model);
		
		double prob;
	
		double[] expectations = new double[model.length];
		
		Set<String> features;
		
		for (TaggedSentence sentence: data)
		{
			for (int i=0; i < sentence.size(); i ++)
			{
				featureList.add(extractor.extractFeatures(sentence, i));
			}
			
		}
		
		
		for (int i=0; i < featureList.size(); i++)
		{
				features = featureList.get(i);
				
				for (String tag: this.model.getLabels())
				{
					prob = this.model.computeProb(features, tag);
					// System.out.println("Probability: "+prob);
				
					for (String feature: features)
					{
						expectations[this.model.getFeatureIndex(feature, tag)] += prob;
					}
				}
							
		}
		//System.out.print("Expected Counts:\n");
		// print(expectations);
		return expectations;
	}



	public int getModelLength() {
		return model.size();
	}


	public double negLogLikelihood(double[] model, List<TaggedSentence> data) {

		this.model.updateModel(model);
		
		List<Set<String>> featureList = new ArrayList<Set<String>>();
		
		for (TaggedSentence sentence: data)
		{
			for (int i=0; i < sentence.size(); i ++)
			{
				featureList.add(extractor.extractFeatures(sentence, i));
			}
			
		}
		
		return - this.model.logLikelihood(featureList, labels);
		
	}


	public String getFeature(int i) {
		return this.model.getFeatureName(i);
	}


	public void train(List<TaggedSentence> train, List<TaggedSentence> dev,
			double alpha, int batch_size, int epochs) {
		
		int no_examples = 0;
		
		double[] gradient;
		
		List<Set<String>> featureList = new ArrayList<Set<String>>();
		
		for (TaggedSentence sentence: train)
		{
			for (int i=0; i < sentence.size(); i ++)
			{
				featureList.add(extractor.extractFeatures(sentence, i));
				labels.add(sentence.getPOS(i));
				no_examples++;
			}
			
		}
		
		
		// System.out.print("Number of examples:"+no_examples+"\n");
		
		System.out.print("Initializing!\n");
		
		this.model.initialize(featureList,labels);
		
		List<TaggedSentence> data = new ArrayList<TaggedSentence>();
		
		TaggerEvaluator evaluator = new TaggerEvaluator();
		
		Boolean stop = false;
		
		double best_score = 0.0;
		
		double accuracy;
		
		int i = 1;
		
		int no_sentences  = 0;
		
		while (i < epochs && ! stop)
	
		{
			System.out.print("Epoch: "+i+"\n");
			
			for (TaggedSentence sentence: train)
			{
				data.add(sentence);
				
				if (no_sentences % batch_size == 0 || train.size() < batch_size)
				{
					
					gradient = gradientAt(model.model, data);
					
					model.updateModel(gradient,-alpha);

					data = new ArrayList<TaggedSentence>();
				}
				
				no_sentences ++;
			}
			accuracy = evaluator.evaluate(this, dev);
			
			System.out.print("Accuracy on dev: "+accuracy+"\n");
			System.out.print("Log-likelihood: "+this.model.logLikelihood(featureList, this.labels));
			
			if (accuracy > best_score)
			{
				best_score = accuracy;
			
			}
			else
			{
				// early stopping
				System.out.print("Early stopping!!!\n");
				stop = true;
			}
			
			i++;
		}
		
		
		
	}
	
	private double[] gradientAt(double[] x, List<TaggedSentence> data) {
		
		// System.out.println("Calling at gradient");
		
		double[] gradient = new double[this.getModelLength()];
		
		double[] empiricalCounts = this.empiricalCounts(data);
		double[] modelExpectation = this.modelExpectations(x,data);
		
		for (int i = 0; i < empiricalCounts.length; i ++)
		{
			gradient[i] =  (modelExpectation[i] - empiricalCounts[i]);
			// System.out.print("Feature:  "+ this.getFeature(i) +"\t" +modelExpectation[i]+" - "+empiricalCounts[i]+"\n");
		}

			
		return gradient;
	}
	
}
