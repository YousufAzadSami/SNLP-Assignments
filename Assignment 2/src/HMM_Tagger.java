
import java.util.*;

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

        boolean isStart = true;

        //int k = state_transitions.keySet().size();

        //double delta[][] = new double[sentence.size()][k];

        //int gamma[][] = new int[sentence.size()][k];

        TaggedSentence tagged_sentence = new TaggedSentence();

        // Implement Viterbi

        Map<String, List<Map<String, String>>> viterbiMatrix = new HashMap<String, List<Map<String, String>>>();

        for (int i = 0; i < sentence.size(); i++) {
            Map<String, Double> tagsOfToken = b(tagged_sentence.getPOS(i), sentence.getToken(i));
            for (Map.Entry<String, Double> entry : tagsOfToken.entrySet()) {
                List<Map<String, String>> tmpList = new ArrayList<>();
                Map<String, String> tmpMap = new HashMap<>();
                if (isStart) {
                    double transition_prob = a("start", entry.getKey());
                    double v_prob = entry.getValue() * transition_prob;
                    tmpMap.put("prob", Double.toString(v_prob));
                    tmpMap.put("prev_tag", "start");
                    tmpList.add(tmpMap);
                    viterbiMatrix.put(entry.getKey(), tmpList);

                } else {
                    double max_v_prob = 0;
                    String max_prev_tag = "";

                    for (Map.Entry<String, List<Map<String, String>>> vEntry : viterbiMatrix.entrySet()) {
                        tmpList = vEntry.getValue();
                        //System.out.println("sentence: " + sentence.size() + " - list: " + tmpList.size());
                        double transition_prob = a(tmpList.get(i - 2).get("prev_tag"), entry.getKey());
                        double v_prob = entry.getValue() * transition_prob * Double.parseDouble(tmpList.get(i - 2).get("prob"));

                        if (max_v_prob < v_prob) {
                            max_v_prob = v_prob;
                            max_prev_tag = vEntry.getKey();
                        }
                    }

                    tmpMap.put("prob", Double.toString(max_v_prob));
                    tmpMap.put("prev_tag", max_prev_tag);
                    tmpList.add(tmpMap);
                    viterbiMatrix.put(entry.getKey(), tmpList);
                }
            }
            isStart = false;
        }


        double max_v_prob = 0;
        String max_prev_tag = "";
        for (Map.Entry<String, List<Map<String, String>>> vEntry : viterbiMatrix.entrySet()) {
            System.out.print("sentence: " + sentence.size() + " - " + "list: " + vEntry.getValue().size());
            double v_prob = Double.parseDouble(vEntry.getValue().get(sentence.size() - 1).get("prob"));

            if (max_v_prob < v_prob) {
                max_v_prob = v_prob;
                max_prev_tag = vEntry.getValue().get(sentence.size() - 1).get("prev_tag");
            }
        }

        List<String> sentence_tag = new ArrayList<>();
        sentence_tag.add(max_prev_tag);


        for (int i = sentence.size() - 2, j = 0; i >= 0; i--, j++) {
            sentence_tag.add(viterbiMatrix.get(sentence_tag.get(j)).get(i).get("prev_tag"));
        }

        for (int i = 0, j = sentence.size() - 1; i < sentence.size(); i++, j--) {
            tagged_sentence.add(sentence.getToken(i), sentence_tag.get(j));
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

    public Map<String, Double>/*double*/ b(String tag, String token) {

        // implement b method. Emission
        Map<String, Double> tagOfToken = new HashMap<>();
        //if (!tag.equalsIgnoreCase("unkwn")) {
        for (Map.Entry<String, HashMap<String, Double>> entry : state_emmissions.entrySet()) {
            if (entry.getValue().containsKey(token)) {
                tagOfToken.put(entry.getKey(), entry.getValue().get(token));
            }
        }
        //}

        //state_emmissions.get(tag).get(token)
        return tagOfToken;

    }

    private double a(String tag, String nextTag) {

        // implement a method. Transition

        return state_transitions.get(tag).get(nextTag);
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
