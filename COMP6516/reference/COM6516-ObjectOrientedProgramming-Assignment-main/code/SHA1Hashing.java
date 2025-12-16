import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Hashing extends MyHashFunction {
    public SHA1Hashing(int hashTableSize) {
        super(hashTableSize);
    }

    // Method used to perform SHA-1 hashing on the input string
    public int hash(String wordSequence) {
        try {
            // Create a MessageDigest instance, specifying the use of the SHA-1 hashing algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // Convert the input string to a byte array and update the digest
            md.update(wordSequence.getBytes());

            // Get the digest result
            byte[] hashBytes = md.digest();

            // Convert the byte array to a hexadecimal string representation
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Calculate an index based on the hash code and the hash table size
            int intIndex = Math.abs(hexString.toString().hashCode()) % super.hashTableSize;
            return intIndex;
        } catch (NoSuchAlgorithmException e) {
            // Handle exceptional cases
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        String input = "Hello, SHA-1!";
        SHA1Hashing sha1Hashing = new SHA1Hashing(10);

        // Get the SHA-1 hash value of the input string and print the result
        int sha1Hash = sha1Hashing.hash(input);
        System.out.println("SHA-1 hash for '" + input + "': " + sha1Hash);
    }
}
