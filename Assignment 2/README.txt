Assigment 2 consists in implementing a Viterbi-algorithm for finding the most likely state sequence (tag sequence) given an observation.

The task consists in implementing the following methods in HMM_Tagger.java:


        private double b(String tag, String token) {
                
                // implement b method
                
                return 0;
                
        }

        private double a(String tag, String nextTag) {
                
                // implement a method
                
                return 0;
        } 

  private TaggedSentence viterbi(Sentence sentence) {
                                
            int k = state_transitions.keySet().size();
                
                double delta[][] = new double[sentence.size()][k];
                        
                int gamma[][] = new int[sentence.size()][k];
                
                TaggedSentence tagged_sentence = new TaggedSentence(sentence);
                
                
                // Implement Viterbi
                
                        
                return tagged_sentence;
                
        }
