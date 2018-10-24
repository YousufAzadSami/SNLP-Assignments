import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class LoglinearModel {

	double[] model;
	
	Map<String,Map<String,Integer>> featureIndex;
	
	Map<Integer,String> int2Feature;
	
	Set<String> labels;
	
	int no_features = 0;
	
	
	public LoglinearModel()
	{
		no_features = 0;
		
		featureIndex = new HashMap<String,Map<String,Integer>>();
		int2Feature = new HashMap<Integer,String>();
		labels = new HashSet<String>();
		
	}
	
	public void initialize(List<Set<String>> data, List<String> labels)
	{
		for (String label: labels)
		{
			this.labels.add(label);
		}
		
		for (Set<String> features: data)
		{
			for (String feature: features)
			{
				addFeature(feature);
			}
		}
		
		model = new double[no_features];
		

	}
	
	public void addFeature(String feature)
	{
		Map<String,Integer> map;
	
		for (String label: labels)
		{
			if (featureIndex.containsKey(label))
			{
				map = featureIndex.get(label);
			}
			else
			{
				map = new HashMap<String,Integer>();
				featureIndex.put(label, map);
			}	
		
			if (!map.containsKey(feature))
			{
				map.put(feature, no_features);
				int2Feature.put(no_features, feature+" tag="+label);
				no_features++;
				// System.out.print("Adding feature: "+feature+":"+label+no_features+"\n");
			}
		}
		
	}
	
	public double[] getModel()
	{
		return model;
		
	}
	
	public void updateModel(double[] model)
	{
		this.model = model;
	}
	
	public double computeUnnormalizedProb(Set<String> features, String label)
	{
		Map<String,Integer> map = featureIndex.get(label);
		
		double prob = 0;
		
		for (String feature: features)
		{
			if (map.containsKey(feature))
			{
				prob += model[map.get(feature)];
			}
		}
		
		return Math.exp(prob);
	}
	
	public double computeSumProb(Set<String> features)
	{
		double sum = 0;
		
		for (String label: labels)
		{
			sum += computeUnnormalizedProb(features,label); 
		}
		
		return sum;
		
	}
	
	public double computeProb(Set<String> features, String label)
	{
		return computeUnnormalizedProb(features, label) / computeSumProb(features);
	}
	
	public double logLikelihood(List<Set<String>> data, List<String> labels)
	{
		double probSum = 0;
		
		for (int i=0; i < data.size(); i++)
		{
			probSum += Math.log(computeProb(data.get(i),labels.get(i)));
		}

		return probSum;
	}

	public int size() {
		return model.length;
	}


	public boolean containsFeature(String feature, String label) {
		if (featureIndex.containsKey(label))
		{
			return featureIndex.get(label).containsKey(feature);
		}
		else
		{
			return false;
		}
	}

	public Integer getFeatureIndex(String feature, String label) {
		
		Map<String,Integer>map;
		
		if (featureIndex.containsKey(label))
		{
			map = featureIndex.get(label);
			if (map.containsKey(feature))
			{
				return map.get(feature);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
		
	}

	public String getFeatureName(int i) {
		return int2Feature.get(i);
	}

	public Set<String> getLabels() {
		return labels;
	}

	public void updateModel(double[] gradient, double alpha) {
		
		for (int i=0; i < gradient.length; i++)
		{
			model[i] = model[i] + alpha * gradient[i];
		}
		
	}
	
	
}
