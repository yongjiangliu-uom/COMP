import java.util.Arrays;


public class MyHashTable {
    public int hashTableSize = 0;
    private MyHashFunction hashFunction;

    public MyLinkedObject[] getHashTable() {
        return hashTable;
    }

    private MyLinkedObject[] hashTable;

    public MyHashTable(int hashTableSize, HashEnum hashEnum){
        this.hashTableSize = hashTableSize;
        switch (hashEnum) {
            case MD5 -> this.hashFunction = new MD5Hashing(hashTableSize);
            case MODULO -> this.hashFunction = new ModuloHashing(hashTableSize);
            case SHA1 -> this.hashFunction = new SHA1Hashing(hashTableSize);
            case MULTIPLICATIVE -> this.hashFunction = new MultiplicativeHashing(hashTableSize);
        }

        this.hashTable = new MyLinkedObject[hashTableSize];
    }

    /**
     * Inset word sequence into HashTable for 1-gram
     * @param wordSequence
     */
    public void insert(String wordSequence){
        int key = hashFunction.hash(wordSequence);

        String[] words = wordSequence.split("\\s+");

        if (hashTable[key] == null) {
            // If the slot is empty, create a new linked list and add nodes
            MyLinkedObject headNode = new MyLinkedObject(words[0]);
            hashTable[key] = headNode;

            for (int i = 1; i < words.length; i++) {
                headNode.setWord(words[i]);
            }
        } else {
            // If the slot is not empty, traverse the linked list and append nodes
            MyLinkedObject current = hashTable[key];
            while (current.getNext() != null) {
                current = current.getNext();
            }

            for (String word : words) {
                current.setNext(new MyLinkedObject(word));
                current = current.getNext();
            }
        }
    }

    /**
     * Inset word sequence into HashTable for n-grams
     * @param lastWord last line‘s last word
     * @param wordSequence wordSequence that needed to insert in to HashTable
     * @param n number of grams
     * @return The lastWords in this word sequence
     */
    public String[] insertNGrams(String[] lastWord, String wordSequence, int n) {
        int key = hashFunction.hash(wordSequence);

        StringBuilder lastWordStr = new StringBuilder();

        if (lastWord!=null){
            for (String str : lastWord) {
                lastWordStr.append(str);
                lastWordStr.append(" ");
            }
        }

        wordSequence =  lastWordStr.toString() + wordSequence;

        String[] words = wordSequence.split("\\s+");

        for (int i = 0; i < words.length - (n - 1); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]);
                if (j < n - 1) {
                    sb.append(" ");
                }
            }
            String ngram = sb.toString();


            if (hashTable[key] == null) {
                // If the slot is empty, create a new linked list and add the bigram
                MyLinkedObject headNode = new MyLinkedObject(ngram);
                hashTable[key] = headNode;
            } else {
                // If the slot is not empty, traverse the linked list and append the bigram
                MyLinkedObject current = hashTable[key];
                while (current.getNext() != null) {
                    current = current.getNext();
                }
                current.setNext(new MyLinkedObject(ngram));
            }
        }


        if (words.length > n - 1) {
            return Arrays.copyOfRange(words, words.length - (n - 1), words.length);
        } else {
            return words;
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("MyHashTable {\n");

        for (int i = 0; i < hashTable.length; i++) {
            output.append("    Slot ").append(i).append(": ");

            if (hashTable[i] == null) {
                output.append("Empty\n");
            } else {
                MyLinkedObject current = hashTable[i];
                output.append("[\n        ");
                if (current != null) {
                    output.append(current);
                }
                output.append("\n    ]\n");
            }
        }
        output.append("}");

        return output.toString();
    }

}
