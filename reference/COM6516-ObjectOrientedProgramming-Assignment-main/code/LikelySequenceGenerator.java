import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikelySequenceGenerator {

    private MyHashTable uniHashTable;
    private MyHashTable biHashTable;
    private MyHashTable triHashTable;
    private MyHashTable fourHashTable;
    Map<String, Integer> unigramFrequencyMap;
    Map<String, Integer> bigramFrequencyMap;
    Map<String, Integer> trigramFrequencyMap;
    Map<String, Integer> fourgramFrequencyMap;

    public LikelySequenceGenerator(MyHashTable uniHashTable, MyHashTable biHashTable, MyHashTable triHashTable,
                                   MyHashTable fourHashTable){
        this.uniHashTable = uniHashTable;
        this.biHashTable = biHashTable;
        this.triHashTable = triHashTable;
        this.fourHashTable = fourHashTable;

        unigramFrequencyMap = getWordFrequency(1);
        bigramFrequencyMap = getWordFrequency(2);
        trigramFrequencyMap = getWordFrequency(3);
        fourgramFrequencyMap = getWordFrequency(4);
    }

    /**
     * Calculate and return WordFrequency by hashtable
     * @param numGrams num of Grams
     * @return Map<String, Integer>
     */
    public Map<String, Integer> getWordFrequency(int numGrams){
        Map<String, Integer> countMap = new HashMap<>();

        MyHashTable hashtable;
        if (numGrams == 2) {
            hashtable = biHashTable;
        } else if (numGrams == 3) {
            hashtable = triHashTable;
        }else if (numGrams == 4) {
            hashtable = fourHashTable;
        } else {
            hashtable = uniHashTable;
        }

        MyLinkedObject[] slots = hashtable.getHashTable();
        for (int i = 0; i < slots.length; i++) {
            MyLinkedObject current = slots[i];
            while (current != null) {
                String word = current.getWord();
                int count = current.getCount();

                // Check that the word already exists in the countMap
                if (countMap.containsKey(word)) {
                    // If exists, update corresponding count value
                    count += countMap.get(word);
                }
                countMap.put(word, count);
                current = current.getNext();
            }
        }
        return countMap;
    }

    /**
     * Get FrequencyMap by number of grams
     * @param numGrams number of grams
     * @return FrequencyMap
     */
    public Map<String, Integer> getFrequencyMap(int numGrams) {
        Map<String, Integer> frequencyMap;
        if (numGrams == 2) {
            frequencyMap = bigramFrequencyMap;
        } else if (numGrams == 3) {
            frequencyMap = trigramFrequencyMap;
        }else if (numGrams == 4) {
            frequencyMap = fourgramFrequencyMap;
        } else {
            frequencyMap = unigramFrequencyMap;
        }

        return frequencyMap;
    }

    /**
     * Calculate the probability of a word in the language model.
     *
     * @param word The word for which the probability needs to be calculated.
     * @return The probability of the given word occurring in the language model.
     */
    public double calculateUnigramProbability(String word) {
        // The frequency values of all words are added together to calculate the total number of all words in the corpus
        int totalWords = unigramFrequencyMap.values().stream().mapToInt(Integer::intValue).sum();
        // Gets how often a given word appears in the corpus
        int wordFrequency = unigramFrequencyMap.getOrDefault(word, 0);
        return (double) wordFrequency / totalWords;
    }

    /**
     * Calculate the conditional probability of a bigram in a language model p(word2 | word1).
     *
     * @param word1 The first word of the bigram.
     * @param word2 The second word of the bigram.
     * @return The conditional probability of the bigram: p(word2 | word1).
     */
    public double calculateBigramProbability(String word1, String word2) {
        String biGram = word1 + " " + word2;
        // p(wk|wk-1) = c(wk-1, wk) / c(wk-1)
        int countBigram = bigramFrequencyMap.getOrDefault(biGram, 0);
        int countWord1 = unigramFrequencyMap.getOrDefault(word1, 0);
        return (double) countBigram / countWord1;
    }

    /**
     * Calculate the conditional probability of a trigram in a language model p(word3 | word1, word2).
     * @param word1 The first word of the trigram.
     * @param word2 The second word of the trigram.
     * @param word3 The third word of the trigram.
     * @return The conditional probability of the trigram: p(word3 | word1, word2).
     */
    public double calculateTrigramProbability(String word1, String word2, String word3) {
        String trigram = word1 + " " + word2 + " " + word3;
        // p(wk|wk-2, wk-1) = c(wk-2, wk-1, wk) / c(wk-2, wk-1)
        int countTrigram = trigramFrequencyMap.getOrDefault(trigram, 0);

        String bigram = word1 + " " + word2;
        int countBigram = bigramFrequencyMap.getOrDefault(bigram, 0);

        return (double) countTrigram / countBigram;
    }

    /**
     * Calculate the conditional probability of a fourgram in a language model p(word4 | word1, word2, word3).
     * @param word1 The first word of the trigram.
     * @param word2 The second word of the trigram.
     * @param word3 The third word of the trigram.
     * @param word4 The fourth word of the trigram.
     * @return The conditional probability of the fourgram: p(word4 | word1, word2, word3).
     */
    public double calculateFourgramProbability(String word1, String word2, String word3, String word4) {
        String fourgram = word1 + " " + word2 + " " + word3 + " " + word4;
        // p(wk|wk-3, wk-2, wk-1) = c(wk-3, wk-2, wk-1, wk) / c(wk-3, wk-2, wk-1)
        int countFourgram = fourgramFrequencyMap.getOrDefault(fourgram, 0);
        String trigram = word1 + " " + word2 + " " + word3;
        int countTrigram = trigramFrequencyMap.getOrDefault(trigram, 0);
        return (double) countFourgram / countTrigram;
    }


    /**
     * PredictNextWord using n-grams model by user input string
     * @param inputStr user input string
     * @param n numbers of grams
     * @return Predicted word
     */
    public String predictNextWord(String inputStr, int n) {
        String predictedWord = ";";
        double maxProbability = 0.0;

        String[] inputWords = inputStr.split("\\s+");
        if (inputWords.length < n - 1) {
            System.out.println("Not enough words for prediction.");
            return "; Error: Not enough words for prediction. At least input (n-1) words!";
        }

        // Depending on the value of n, choose to use bigram or trigram
        if (n == 1) {
            // iterate Unigram
            for (Map.Entry<String, Integer> entry : unigramFrequencyMap.entrySet()) {
                String key = entry.getKey();

                // calculate probability
                double probability = calculateUnigramProbability(key);
                // update max
                if (probability > maxProbability) {
                    maxProbability = probability;
                    predictedWord = key; // get word
                }
            }
        } else if (n == 2) {
            // iterate Bigram
            for (Map.Entry<String, Integer> entry : bigramFrequencyMap.entrySet()) {
                String[] key = entry.getKey().split("\\s+");

                // If the previous word of the current Bigram is the entered word
                if (key[0].equals(inputWords[inputWords.length - 1])) {
                    double probability = calculateBigramProbability(key[0], key[1]);
                    if (probability > maxProbability) {
                        maxProbability = probability;
                        predictedWord = key[1]; // get word
                    }
                }
            }
        } else if (n == 3) {
            // iterate Trigram
            for (Map.Entry<String, Integer> entry : trigramFrequencyMap.entrySet()) {
                String[] key = entry.getKey().split("\\s+");

                // If the first two words of the current Trigram match the first two words of the input
                if (key[0].equals(inputWords[inputWords.length - 2]) &&
                        key[1].equals(inputWords[inputWords.length - 1])) {
                    double probability = calculateTrigramProbability(key[0], key[1], key[2]);
                    if (probability > maxProbability) {
                        maxProbability = probability;
                        predictedWord = key[2];
                    }
                }
            }
        }else if (n == 4) {
            // iterate Fourgram
            for (Map.Entry<String, Integer> entry : fourgramFrequencyMap.entrySet()) {
                String[] key = entry.getKey().split("\\s+");

                // If the first three words of the current Fourgram match the first three words entered
                if (key[0].equals(inputWords[inputWords.length - 3]) &&
                        key[1].equals(inputWords[inputWords.length - 2]) &&
                        key[2].equals(inputWords[inputWords.length - 1])) {
                    double probability = calculateFourgramProbability(key[0], key[1], key[2], key[3]);
                    if (probability > maxProbability) {
                        maxProbability = probability;
                        predictedWord = key[3];
                    }
                }
            }
        }

        return predictedWord;
    }

}
