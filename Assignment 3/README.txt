The goal of Assignment3 is to implement a basic maximum entropy model tagger that performs stochastic gradient descent in batches to learn a maxent model.

To get the maximum entropy tagger to work, you need to implement the following two methods:

double[] modelExpectations(double[] model, List<TaggedSentence> data)

double[] empiricalCounts(List<TaggedSentence> data)
