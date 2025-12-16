package hash_table;
/*
 * Author : Manu Kenchappa Junjanna
 * Email : mkenchappajunjanna1@sheffield.ac.uk
 * Created on Sun Dec 3 2023
 */

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents a hash table data structure.
 * It stores objects of type MyLinkedObject using a hash function to determine
 * their position in the table.
 */
public class MyHashTable {
    public MyLinkedObject[] linkedList;
    private MyHashFunction hashFunction;
    private int totalWordCount = 0, uniqueWordCount = 0;

    /**
     * Constructs a new MyHashTable object with the specified size.
     * 
     * @param m the size of the hash table
     */
    public MyHashTable(int m) {
        linkedList = new MyLinkedObject[m];
    }

    public void setMyHashFunction(MyHashFunction hashFunction) {
        this.hashFunction = hashFunction;
    }

    /**
     * Adds a word to the hash table.
     * 
     * @param word the word to be added
     */
    public void add(String word) {
        if (word == null || word.isEmpty())
            return;
        totalWordCount++;
        int hash = hashFunction.hash(word);
        if (linkedList[hash] == null) {
            linkedList[hash] = new MyLinkedObject(word);
            uniqueWordCount++;
        } else {
            // if setWord returns 0, the word is already in the hash table else it is a new
            // word
            // and the unique word count is incremented
            uniqueWordCount += linkedList[hash].setWord(word);
        }
    }

    /**
     * Prints the hash table// for debugging purposes
     */
    public void printHashTable() {
        // print the hash table by iterating through it and printing each linked list
        for (int i = 0; i < linkedList.length; i++) {
            System.out.println("--------------------" + i + "--------------------");
            MyLinkedObject current = linkedList[i];
            while (current != null) {
                System.out.print(current);
                current = current.getNext();
            }
            System.out.println("--------------------------------------------------");
        }
    }

    public int getTotalWordCount() {
        return totalWordCount;
    }

    public int getUniqueWordCount() {
        return uniqueWordCount;
    }

    /**
     * Returns the count of occurrences of a given word in the hash table.
     *
     * @param word the word to be searched for
     * @return the count of occurrences of the word, or 0 if the word is not found
     */
    public int getCount(String word) {
        int hash = hashFunction.hash(word);
        MyLinkedObject current = linkedList[hash];
        while (current != null) {
            if (current.getWord().equals(word)) {
                return current.getCount();
            }
            current = current.getNext();
        }
        return 0;
    }

    public TreeMap<String, Integer> getAllWordAndItsCount() {
        TreeMap<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < linkedList.length; i++) {
            MyLinkedObject current = linkedList[i];
            while (current != null) {
                String word = current.getWord();
                int count = current.getCount();
                map.put(word, count);
                current = current.getNext();
            }
        }
        return map;
    }

    public Map<Integer, Integer> getWordsCountPresentInEachLinkedList() {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < linkedList.length; i++) {
            int count = 0;
            MyLinkedObject current = linkedList[i];
            while (current != null) {
                count++;
                current = current.getNext();
            }
            map.put(i, count);
        }
        return map;
    }

}
