public class MultiplicativeHashing extends MyHashFunction {

    private final int hashTableSize;

    public MultiplicativeHashing(int hashTableSize) {
        super(hashTableSize);
        this.hashTableSize = hashTableSize;
    }

    private static final double A = 0.6180339887; // Constant A, often chosen as (sqrt(5) - 1) / 2

    // Hash function, takes input string wordSequence and hash table size tableSize
    @Override
    public int hash(String wordSequence) {
        int hashCode = 0;
        for (int i = 0; i < wordSequence.length(); i++) {
            int charValue = (int) wordSequence.charAt(i); // Get the ASCII or Unicode value of the character
            hashCode = (int) ((hashCode * A) + charValue); // Apply multiplication hashing to accumulate the hash code for the current character value
        }
        double product = hashCode * A; // Multiply the accumulated hash code by constant A
        double fractionalPart = product - Math.floor(product); // Get the fractional part of the product

        // Multiply the fractional part by the hash table size and take the floor to obtain the final hash value
        return (int) Math.floor(hashTableSize * fractionalPart);
    }

    public static void main(String[] args) {
        // Create an instance of MultiplicativeHashing with a specified hash table size of 10
        MultiplicativeHashing multiplicativeHashing = new MultiplicativeHashing(10);

        // Use the hash method of the MultiplicativeHashing class to generate hash codes and print the results
        int hash1 = multiplicativeHashing.hash("Hello");
        int hash2 = multiplicativeHashing.hash("Java");
        int hash3 = multiplicativeHashing.hash("Hacker");

        System.out.println("Hash code for hash1: " + hash1);
        System.out.println("Hash code for hash2: " + hash2);
        System.out.println("Hash code for hash3: " + hash3);
    }

}
