import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HMM_Tagger implements POS_Tagger {

    HashMap<String, HashMap<String, Integer>> state_transition_counts;
    HashMap<String, HashMap<String, Integer>> state_emmission_counts;

    HashMap<String, Integer> state_transition_total;
    HashMap<String, Integer> state_emmission_total;

    HashMap<String, HashMap<String, Double>> state_transitions;
    HashMap<String, HashMap<String, Double>> state_emmissions;

    HashMap<String, Integer> pos_index;
    HashMap<Integer, String> inv_pos_index;

    HashMap<String, Integer> token_freq;

    public void train(List<TaggedSentence> tagged_sentences) {

        state_transition_counts = new HashMap<String, HashMap<String, Integer>>();
        state_emmission_counts = new HashMap<String, HashMap<String, Integer>>();

        state_transition_total = new HashMap<String, Integer>();
        state_emmission_total = new HashMap<String, Integer>();

        state_transitions = new HashMap<String, HashMap<String, Double>>();
        state_emmissions = new HashMap<String, HashMap<String, Double>>();

        token_freq = new HashMap<String, Integer>();

        pos_index = new HashMap<String, Integer>();

        inv_pos_index = new HashMap<Integer, String>();

        TaggedSentence tagged_sentence;

        int no_sentences = 0;

        for (int i = 0; i < tagged_sentences.size(); i++) {
            no_sentences++;
            tagged_sentence = tagged_sentences.get(i);
            // System.out.println(tagged_sentence);
            update(tagged_sentence);
        }

        computeModel();

        System.out.println("Trained on " + no_sentences + "sentences\n");

    }

    private void computeModel() {

        Integer count;
        Integer total;
        Double value;
        HashMap<String, Integer> counts;
        HashMap<String, Double> map;

        for (String prevTag : state_transition_counts.keySet()) {

            counts = state_transition_counts.get(prevTag);
            total = state_transition_total.get(prevTag);

            map = new HashMap<String, Double>();

            for (String tag : counts.keySet()) {
                count = counts.get(tag);

                value = new Double((double) count / (double) total);

                map.put(tag, value);
            }

            state_transitions.put(prevTag, map);
        }

        for (String tag : state_transitions.keySet()) {
            if (!pos_index.containsKey(tag)) {
                pos_index.put(tag, new Integer(pos_index.keySet().size()));

                inv_pos_index.put(new Integer(pos_index.keySet().size()), tag);
            }
        }

        for (String tag : state_emmission_counts.keySet()) {

            counts = state_emmission_counts.get(tag);
            total = state_emmission_total.get(tag);

            map = new HashMap<String, Double>();

            for (String token : counts.keySet()) {
                count = counts.get(token);

                value = new Double((double) (count + 1) / ((double) total + (double) (token_freq.keySet().size() + 1)));

                map.put(token, value);
            }

            // smoothing

            value = new Double((double) 1 / ((double) total + (double) (token_freq.keySet().size() + 1)));

            map.put("unkwn", value);

            state_emmissions.put(tag, map);

        }

    }

    private void update(TaggedSentence tagged_sentence) {

        String prevTag;
        String token;
        String tag;

        for (int i = 0; i < tagged_sentence.size(); i++) {
            token = tagged_sentence.getToken(i);

            if (i > 0)
                prevTag = tagged_sentence.getPOS(i - 1);
            else
                prevTag = "start";

            tag = tagged_sentence.getPOS(i);

            updateStateTransitions(prevTag, tag);
            updateEmmissions(tag, token);
            updateTokenFreq(token);

        }
    }

    private void updateTokenFreq(String token) {

        Integer freq;

        if (token_freq.containsKey(token)) {
            freq = token_freq.get(token);
            token_freq.put(token, new Integer(freq + 1));

        } else {
            token_freq.put(token, new Integer(1));
        }

    }

    private void updateEmmissions(String tag, String token) {
        HashMap<String, Integer> map;
        Integer intValue;

        if (state_emmission_counts.containsKey(tag)) {

            map = state_emmission_counts.get(tag);

            if (map.containsKey(token)) {
                intValue = map.get(token);

                map.put(token, ++intValue);

            } else {
                map.put(token, new Integer(1));
            }

            if (state_emmission_total.containsKey(tag)) {

                intValue = state_emmission_total.get(tag);
                {
                    state_emmission_total.put(tag, ++intValue);
                }
            } else {
                state_emmission_total.put(tag, new Integer(1));
            }

        } else {
            map = new HashMap<String, Integer>();
            map.put(token, new Integer(1));

            state_emmission_counts.put(tag, map);

            state_emmission_total.put(tag, new Integer(1));

        }

    }

    private void updateStateTransitions(String prevTag, String tag) {

        HashMap<String, Integer> map;
        Integer intValue;

        if (state_transition_counts.containsKey(prevTag)) {

            map = state_transition_counts.get(prevTag);

            if (map.containsKey(tag)) {
                intValue = map.get(tag);

                map.put(tag, ++intValue);

            } else {
                map.put(tag, new Integer(1));
            }

            if (state_transition_total.containsKey(prevTag)) {

                intValue = state_transition_total.get(prevTag);
                {
                    state_transition_total.put(prevTag, ++intValue);
                }
            } else {
                state_transition_total.put(prevTag, new Integer(1));
            }

        } else {
            map = new HashMap<String, Integer>();
            map.put(tag, new Integer(1));

            state_transition_counts.put(prevTag, map);

            state_transition_total.put(prevTag, new Integer(1));

        }

    }

    public TaggedSentence predict(Sentence sentence) {

        return viterbi(sentence);
    }

    private TaggedSentence viterbi(Sentence sentence) {

        int k = state_transitions.keySet().size();

        double delta[][] = new double[sentence.size()][k]; // possibility

        int gamma[][] = new int[sentence.size()][k]; // index for tags

        TaggedSentence tagged_sentence = new TaggedSentence(sentence);

        // Implement Viterbi
        double max_start = 0.0;
        for (int i = 0; i < k; i++) {
            double a = a("start", inv_pos_index.get(i + 1));
            if (max_start < a) {
                max_start = a;
            }
        }

        for (int j = 0; j < sentence.size(); j++) {
            for (int i = 0; i < k; i++) {
                double max = 0.0;
                for (Map.Entry<Integer, String> entry : inv_pos_index.entrySet()) {
                    if (j == 0) {

                        max = max_start * a(entry.getValue(), inv_pos_index.get(i + 1))
                                * b(inv_pos_index.get(i + 1), sentence.getToken(j));
                    } else {
                        max = delta[j - 1][entry.getKey()-1] * a(entry.getValue(), inv_pos_index.get(i + 1))
                                * b(inv_pos_index.get(i + 1), sentence.getToken(j));

                    }

                    if (delta[j][i] < max) {
                        delta[j][i] = max;
                    }
                }
            }
        }

        for (int j = sentence.size() - 1; j >= 0; j--) {
            double max_prob = 0.0;
            int max_ind = 0;
            for (int i = 0; i < k; i++) {
                if (max_prob < delta[j][i]) {
                    max_prob = delta[j][i];
                    max_ind = i + 1;
                }
            }
            tagged_sentence.setTag(j, inv_pos_index.get(max_ind));
        }

        return tagged_sentence;
    }

    private void printArray(double[][] delta, int i) {

        System.out.print(i + ": ");
        for (int k = 0; k < delta.length; k++) {
            if (delta[i][k] > Double.NEGATIVE_INFINITY) {
                System.out.print(this.inv_pos_index.get(new Integer(k)) + ":" + delta[i][k] + " ");
            }
        }

        System.out.print("\n");

    }

    private double delta(String i, int j, HashMap<Integer, HashMap<String, Double>> delta) {

        HashMap<String, Double> map;

        if (delta.containsKey(new Integer(j))) {
            map = delta.get(j);

            if (map.containsKey(i))

                return ((Double) map.get(i)).doubleValue();

        }

        return 0.0;
    }

    private double b(String tag, String token) {
        // implement b method. emission
        if (state_emmissions.containsKey(tag)) {
            if (state_emmissions.get(tag).containsKey(token)) {
                return state_emmissions.get(tag).get(token);
            } else {
                return state_emmissions.get(tag).get("unkwn");
            }
        }

        return 0;
    }

    private double a(String tag, String nextTag) {
        // implement a method. Transition
        if (state_transitions.containsKey(tag)) {
            if (state_transitions.get(tag).containsKey(nextTag)) {
                return state_transitions.get(tag).get(nextTag);
            }
        }
        return 0;
    }

    public String toString() {
        String string = "";

        String prevTag, tag, token;

        HashMap<String, Double> map;

        string += "===================================================\n";

        string += "State transition probabilities for " + state_transitions.keySet().size() + " tags:\n";

        for (Iterator it = state_transitions.keySet().iterator(); it.hasNext(); ) {

            prevTag = (String) it.next();

            string += prevTag + ": ";

            map = state_transitions.get(prevTag);

            for (Iterator it2 = map.keySet().iterator(); it2.hasNext(); ) {
                tag = (String) it2.next();

                string += tag + "(" + map.get(tag) + ")";
            }

            string += "\n";

        }

        string += "===================================================";

        string += "State emmission probabilities for " + state_emmissions.keySet().size() + " tokens:\n";

        for (Iterator it = state_emmissions.keySet().iterator(); it.hasNext(); ) {

            tag = (String) it.next();

            string += tag + ": ";

            map = state_emmissions.get(tag);

            for (Iterator it2 = map.keySet().iterator(); it2.hasNext(); ) {
                token = (String) it2.next();

                string += token + "(" + map.get(token) + ")";
            }

            string += "\n";

        }

        return string;
    }

}
