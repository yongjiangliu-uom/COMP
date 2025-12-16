package hash_table;
/*
 * Author : Manu Kenchappa Junjanna
 * Email : mkenchappajunjanna1@sheffield.ac.uk
 * Created on Sun Dec 3 2023
 */

interface MyHashFunction {
    /**
     * Calculates the hash value for the given word.
     *
     * @param word the word to be hashed
     * @return the hash value of the word
     */
    public int hash(String word);
}
