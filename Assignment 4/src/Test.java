import java.util.ArrayList;
import java.util.List;


public class Test {

	public static void main(String[] args) {
		
		List document1 = new ArrayList<String>();
		
		List document2 = new ArrayList<String>();
		
		List document3 = new ArrayList<String>();
		
		document1.add("the");
		document1.add("lecturer");
		document1.add("love");
		document1.add("all");
		document1.add("students");
		
		document2.add("Croatia");
		document2.add("will");
		document2.add("win");
		document2.add("the");
		document2.add("soccer");
		document2.add("worldcup");
		
		
		document3.add("students");
		document3.add("and");
		document3.add("lecturer");
		document3.add("love");
		document3.add("watching");
		document3.add("soccer");
		
		
		int no_words = 13;
		
		int[] alpha = {1,1,1,1};
		
		int[] beta = {1,1,1,1,1,1,1,1,1,1,1,1,1};
		
		
		LDA_Computation computation = new LDA_Computation(4,no_words,alpha,beta);
		
		computation.addDocument(document1);
		computation.addDocument(document2);
		computation.addDocument(document3);
		
		
		computation.sample(20);
		
		computation.printPhi();
		computation.printTheta();
		computation.printTopicAssignments();
		
		
		
		

	}

}
