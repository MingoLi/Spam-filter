import java.util.HashMap;

public class Main {
    
    public static void main(String[] args) {
	
        HashMap<String, Word> freqTable = new HashMap<>();
        
        // call trainning and testing method
        Util.train(freqTable);
        Util.test(freqTable);

        
        // for debugging usage
//        System.out.println(freqTable.size());

//        for (Map.Entry<String, Word> item : freqTable.entrySet()) {
//            System.out.println(item.getKey() + "  " + item.getValue());
//        }

    } // end Main()
}



