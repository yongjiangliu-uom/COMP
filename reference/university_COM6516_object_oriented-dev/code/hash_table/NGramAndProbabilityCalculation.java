package hash_table;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import constants.Constants;
import constants.Constants.HashFunctionType;

public class NGramAndProbabilityCalculation {
    private MyHashTable uniGramHashTable, biGramHashTable, triGramHashTable;

    public NGramAndProbabilityCalculation(String[] uniGramData, HashFunctionType myHashFunctionType,
            int hashTableSize) {
        // generate bi-gram and tri-gram
        String[] biGramData = getNGramArray(uniGramData, 2);
        String[] triGramData = getNGramArray(uniGramData, 3);

        // create hash table for uni-gram bi-gram and tri-gram
        MyHashFunction myHashFunction = myHashFunctionType == HashFunctionType.SIMPLE_HASH_FUNCTION
                ? new SimpleHashFunction(hashTableSize)
                : new PolynomialHashFunction(hashTableSize);

        uniGramHashTable = new MyHashTable(hashTableSize);
        uniGramHashTable.setMyHashFunction(myHashFunction);

        biGramHashTable = new MyHashTable(hashTableSize);
        biGramHashTable.setMyHashFunction(myHashFunction);

        triGramHashTable = new MyHashTable(hashTableSize);
        triGramHashTable.setMyHashFunction(myHashFunction);

        // insert uni-gram bi-gram and tri-gram into hash table
        for (String uniGramWord : uniGramData) {
            this.uniGramHashTable.add(uniGramWord);
        }
        for (String biGramWord : biGramData) {
            biGramHashTable.add(biGramWord);
        }
        for (String triGramWord : triGramData) {
            triGramHashTable.add(triGramWord);
        }
    }

    public MyHashTable getUniGramHashTable() {
        return uniGramHashTable;
    }

    public MyHashTable getBiGramHashTable() {
        return biGramHashTable;
    }

    public MyHashTable getTriGramHashTable() {
        return triGramHashTable;
    }

    public int getTotalTriGramWordCount() {
        return biGramHashTable.getTotalWordCount();
    }

    public int getTotalBiGramWordCount() {
        return triGramHashTable.getTotalWordCount();
    }

    public int getBiGramUniqueWordCount() {
        return biGramHashTable.getUniqueWordCount();
    }

    public int getTriGramUniqueWordCount() {
        return triGramHashTable.getUniqueWordCount();
    }

    public String get20MostFrequentWordsUsingBigram(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Cannot predict next word, since input is empty");
        }
        String[] inputWords = input.split(" ");
        if (uniGramHashTable.getCount(inputWords[inputWords.length - 1]) == 0) {
            throw new IllegalArgumentException(
                    "Cannot predict next word, since \"" + input + "\"is not in the dataset");
        }
        for (int i = 0; i < 20; i++) {
            String[] words = input.split(" ");
            String word_k_1 = words[words.length - 1];
            int countK_1 = uniGramHashTable.getCount(word_k_1);
            Map<Double, String> probability = new TreeMap<>(Collections.reverseOrder());
            for (Map.Entry<String, Integer> entry : uniGramHashTable.getAllWordAndItsCount().entrySet()) {
                String word_k = entry.getKey();
                double probabilityValue = getBiGramProbability(word_k_1, word_k, countK_1);
                // put the probability as key and word as value, and sorts the map in
                // descending
                probability.put(probabilityValue, word_k);
            }
            // so at the end of the loop, the first element of the map will be the most
            // probable word with highest probability
            String mostProbableWord = probability.values().iterator().next();
            String mostProbableWordProbability = probability.keySet().iterator().next().toString();
            System.out.println("mostProbableWord = " + mostProbableWord + " mostProbableWordProbability = "
                    + mostProbableWordProbability);
            input += " " + mostProbableWord;
        }
        return input;
    }

    public String get20MostFrequentWordsUsingTrigram(String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Cannot predict next word, since input is empty");
        }
        if (input.split(" ").length < 2) {
            throw new IllegalArgumentException("Cannot predict next word, since input is less than 2 words");
        }
        String[] inputWords = input.split(" ");
        if (biGramHashTable
                .getCount(inputWords[inputWords.length - 2] + " " + inputWords[inputWords.length - 1]) == 0) {
            throw new IllegalArgumentException(
                    "Cannot predict next word, since \"" + input + "\" is not in the dataset");
        }
        for (int i = 0; i < 20; i++) {
            String[] words = input.split(" ");
            String word_k_2 = words[words.length - 2];
            String word_k_1 = words[words.length - 1];
            Map<Double, String> probability = new TreeMap<>(Collections.reverseOrder());
            for (Map.Entry<String, Integer> entry : uniGramHashTable.getAllWordAndItsCount().entrySet()) {
                String word_k = entry.getKey();
                double probabilityValue = getTriGramProbability(word_k_2, word_k_1, word_k);
                // put the probability as key and word as value, and sorts the map in
                // descending
                probability.put(probabilityValue, word_k);
            }
            // so at the end of the loop, the first element of the map will be the most
            // probable word with highest probability
            String mostProbableWord = probability.values().iterator().next();
            String mostProbableWordProbability = probability.keySet().iterator().next().toString();
            System.out.println("mostProbableWord = " + mostProbableWord + " mostProbableWordProbability = "
                    + mostProbableWordProbability);
            input += " " + mostProbableWord;
        }
        return input;
    }

    private double getBiGramProbability(String previousWord, String currentWord, int previousWordCount) {
        int currentWordPairCount = biGramHashTable.getCount(previousWord + " " + currentWord);
        if (previousWordCount == 0) {
            return 0;
        }
        return (double) currentWordPairCount / previousWordCount;
    }

    private double getTriGramProbability(String previousTwoWords, String previousWord, String currentWord) {
        int countBiGram = biGramHashTable.getCount(previousTwoWords + " " + previousWord);
        int countTriGram = triGramHashTable.getCount(previousTwoWords + " " + previousWord + " " + currentWord);
        if (countBiGram == 0) {
            return 0;
        }
        return (double) countTriGram / countBiGram;
    }

    private String[] getNGramArray(String[] uniGram, int n) {
        String[] nGramArray = new String[uniGram.length - n + 1];
        // iterate through the array and create ngram
        if (uniGram.length < n) {
            System.out.println("The value of n is greater than the length of the array");
        }
        for (int i = 0; i < uniGram.length - n + 1; i++) {
            String[] temp = new String[n];
            for (int j = 0; j < n; j++) {
                temp[j] = uniGram[i + j];
            }
            nGramArray[i] = String.join(" ", temp);
        }
        return nGramArray;
    }

}
