import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Util {

    static double probHam;      // p(Ham)
    static double probSpam;     // p(Spam)

    public static void train(HashMap<String, Word> freqTable) {
        BufferedReader reader;
        String line;
        String[] tokens;
        double spamCount = 0;
        double hamCount = 0;
        double wordsCount = 0;     // number of words in training msg
        int uniqueWordsCount;

        String word;

        File folder = new File("A4PartA/training/ham");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                try {
                    reader = new BufferedReader(new FileReader(listOfFiles[i]));
                    while((line = reader.readLine()) != null) {
                        if ( line.trim().isEmpty() )
                            break;
                    }

                    while((line=reader.readLine()) != null) {
                        tokens = splitLine(line);

                        for(String s : tokens) {
                            hamCount++;
                            wordsCount++;
                            
                            word = stripWords(s);
                            if(!freqTable.containsKey(word)) {
                                freqTable.put(word, new Word(1, 0));
                            }
                            else {
                                freqTable.get(word).hamCount++;
                            }
                        } // end for
                    } // end while

                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        } // end for ham

        folder = new File("A4PartA/training/spam");
        listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                try {
                    reader = new BufferedReader(new FileReader(listOfFiles[i]));
                    while((line = reader.readLine()) != null) {
                        if ( line.trim().isEmpty() )
                            break;
                    }
                    while((line=reader.readLine()) != null) {
                        tokens = splitLine(line);

                        for(String s : tokens) {
                            spamCount++;
                            wordsCount++;

                            word = stripWords(s);
                            if(!freqTable.containsKey(word)) {
                                freqTable.put(word, new Word(0, 1));
                            }
                            else {
                                freqTable.get(word).spamCount++;
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }

            }

        } // end for spam

        // calculate the probability P(Spam) & P(Ham)

        probHam = hamCount / wordsCount;     // P(Ham)
        probSpam = spamCount / wordsCount;   // P(Spam)

        String key;
        Word value;
        uniqueWordsCount = freqTable.size();
        // calculate P(W|C)
        for (Map.Entry<String, Word> item : freqTable.entrySet()) {
            value = item.getValue();
            value.probHam = (value.hamCount + (1/uniqueWordsCount)) / (hamCount + 1);
            value.probSpam = (value.spamCount + (1/uniqueWordsCount)) / (spamCount + 1);
        }


        // apply feature to remove some words
        double temp;
        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, Word> item : freqTable.entrySet()) {
            value = item.getValue();
            if (value.hamCount + value.spamCount < 4) {
                keys.add(item.getKey());
            }
            temp = value.probSpam/(value.probSpam + value.probHam);
            if (0.45 <= temp && temp <= 0.55) {
                keys.add(item.getKey());
            }
        }
        for(String s : keys) {
            freqTable.remove(s);
        }

//        System.out.println(wordsCount);
//        System.out.println(hamCount);
//        System.out.println(spamCount);

    } // end train()


    public static void test(HashMap<String, Word> freqTable) {

        BufferedReader reader;
        String line;
        String[] tokens;

        double spamMailCount, hamMailCount;
        int correctSpamMailCount = 0;
        int correctHamMailCount = 0;
        int spamWrong = 0;
        int hamWrong = 0;

        double pSpamSum;        // P(Spam, a1, a2, ...)
        double pHamSum;         // P(Ham, a1, a2, ...)
        
        boolean firstHam, firstSpam;

        String word;

        File folder = new File("A4PartA/testing/spam");
        File[] listOfFiles = folder.listFiles();
        spamMailCount = listOfFiles.length;
        firstSpam = true;
        firstHam = true;

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                pSpamSum = Math.log10(probSpam);
                pHamSum = Math.log10(probHam);
                try {
                    reader = new BufferedReader(new FileReader(listOfFiles[i]));
                    while((line = reader.readLine()) != null) {
                        if ( line.trim().isEmpty() )
                            break;
                    }
                    while((line=reader.readLine()) != null) {
                        tokens = splitLine(line);

                        for(String s : tokens) {
                            word = stripWords(s);
                            if(freqTable.containsKey(word)) {
                                pSpamSum += Math.log10(freqTable.get(word).probSpam);
                                pHamSum += Math.log10(freqTable.get(word).probHam);
                            }

                        } // end for
                    } // end while

                } catch (IOException e) {
                    System.out.println(e);
                }

                // compare to see which is bigger
                if(pSpamSum > pHamSum) {
                    // it's Spam
                    correctSpamMailCount++;
                    if(firstSpam) {
                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
                        System.out.println("First classified spam (testing/spam):");
                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
                        try {
                            reader = new BufferedReader(new FileReader(listOfFiles[i]));
                            while((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        firstSpam = false;
                    }
                } else {
                    // it's Ham
                    spamWrong++;
                    if(firstHam) {
                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
                        System.out.println("First classified ham (testing/spam):");
                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
                        try {
                            reader = new BufferedReader(new FileReader(listOfFiles[i]));
                            while((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        firstHam = false;
                    }
                }

            }
        } // end for spam

        

        folder = new File("A4PartA/testing/ham");
        listOfFiles = folder.listFiles();
        hamMailCount = listOfFiles.length;
        firstHam = true;
        firstSpam = true;

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                pSpamSum = Math.log10(probSpam);
                pHamSum = Math.log10(probHam);
                try {
                    reader = new BufferedReader(new FileReader(listOfFiles[i]));
                    while((line = reader.readLine()) != null) {
                        if ( line.trim().isEmpty() )
                            break;
                    }
                    while((line=reader.readLine()) != null) {
                        tokens = splitLine(line);

                        for(String s : tokens) {
                            word = stripWords(s);
                            if(freqTable.containsKey(word)) {
                                pSpamSum += Math.log10(freqTable.get(word).probSpam);
                                pHamSum += Math.log10(freqTable.get(word).probHam);
                            }

                        } // end for
                    } // end while

                } catch (IOException e) {
                    System.out.println(e);
                }

                // compare to see which is bigger
                if(pSpamSum > pHamSum) {
                    // it's Spam
                    hamWrong++;
                    if(firstSpam) {
                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
                        System.out.println("First classified spam (testing/ham):");
                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
                        try {
                            reader = new BufferedReader(new FileReader(listOfFiles[i]));
                            while((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        firstSpam = false;
                    }
                } else {
                    // it's Ham
                    correctHamMailCount++;
                    if(firstHam) {
                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
                        System.out.println("First classified ham (testing/ham):");
                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&");
                        try {
                            reader = new BufferedReader(new FileReader(listOfFiles[i]));
                            while((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        firstHam = false;
                    }
                }

            }
        } // end for ham
        
        double trueNegativeRate = (correctSpamMailCount/spamMailCount);
        
        System.out.println("Number of spam emails: " + spamMailCount);
        System.out.println("Correct: " + correctSpamMailCount);
        System.out.println("Wrong: " + spamWrong);
        System.out.format("Correct Rate: %.2f%%\n", trueNegativeRate*100);
        System.out.format("Miss Rate: %.2f%%\n", (1-trueNegativeRate)*100);

        double truePositiveRate = (correctHamMailCount/hamMailCount);

        System.out.println("\nNumber of Ham: " +hamMailCount);
        System.out.println("Correct: " + correctHamMailCount);
        System.out.println("Wrong: " + hamWrong);
        System.out.format("Correct Rate: %.2f%%\n", truePositiveRate*100);
        System.out.format("Miss Rate: %.2f%%\n", (1-truePositiveRate)*100);

        
        double errorRate = 1 - (correctHamMailCount+correctSpamMailCount)/(hamMailCount+spamMailCount);

        System.out.format("\nOverall errorRate: %.2f%%", errorRate*100);
        
        // debug
//        System.out.println("Rfp    Rfn     Re");
//        System.out.format("%.2f%%  %.2f%%  %.2f %%", (1-truePositiveRate)*100,(1-trueNegativeRate)*100,errorRate*100);


    } // end test()

    public static String[] splitLine(String line) {
        String[] tokens;

        line = line.toLowerCase();
        // thanks http://slendermeans.org/ml4h-ch3.html
        // remove HTML stuff
        line = line.replaceAll("3D", "");
        line = line.replaceAll("<.*?>", " ");
        line = line.replaceAll("&lt;(.|\\\\n)\\*?&gt;", " ");
        line = line.replaceAll("&amp;\\\\w+;", " ");
        line = line.replaceAll("_+", "_");
        // remove single letter word
        line = line.replaceAll("(\\s.\\s)|(\\s.$)", " ");

        tokens = line.trim().split("\\s+");

        return tokens;
    }


    public static String stripWords(String word) {
        String w = word;

        if(w.matches("[^a-zA-Z]{3}")) {
            // if it's a trigram, then keep the symbols
            // do nothing
//            System.out.println(w);
        } else {
            // then it's a word, get rid of most symbols
            w = w.replaceAll("[\\p{Punct}]", "");
        }
        return w.trim();
    }
}
