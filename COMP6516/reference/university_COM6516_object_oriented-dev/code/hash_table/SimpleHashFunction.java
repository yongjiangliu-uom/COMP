package hash_table;
/*
 * Author : Manu Kenchappa Junjanna
 * Email : mkenchappajunjanna1@sheffield.ac.uk
 * Created on Sun Dec 10 2023
 */

class SimpleHashFunction implements MyHashFunction {
    private int hashTableSize;

    public SimpleHashFunction(int hashTableSize) {
        this.hashTableSize = hashTableSize;
    }

    /**
     * Calculates the hash value for the given input string.
     * The hash value is determined by taking the ASCII value of the first character
     * of the input string and performing modulo operation with the hash table size.
     *
     * @param input the input string
     * @return the hash value
     */
    @Override
    public int hash(String input) {
        int hash = input.charAt(0) % hashTableSize;
        return hash;
    }
}