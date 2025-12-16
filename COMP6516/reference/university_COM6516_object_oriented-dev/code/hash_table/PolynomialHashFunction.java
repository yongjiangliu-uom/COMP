package hash_table;

class PolynomialHashFunction implements MyHashFunction {
    private int hashTableSize;

    public PolynomialHashFunction(int hashTableSize) {
        this.hashTableSize = hashTableSize;
    }

    /**
     * Calculates the hash value for a given word using the following formula:
     * hash = (hash * 31 + character) % hashTableSize
     * 
     * @param word the word to be hashed
     * @return the hash value of the word
     */
    @Override
    public int hash(String word) {
        int hash = 0;
        for (int i = 0; i < word.length(); i++) {
            hash = (hash * 31 + word.charAt(i)) % hashTableSize;
        }
        return hash;
    }

}
