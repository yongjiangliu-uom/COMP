/**
 * MD5 encodes data of any length to obtain a 128-bit (16-byte) hash code
 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/security/MessageDigest.html
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD5Hashing extends MyHashFunction{
    public MD5Hashing(int hashTableSize) {
        super(hashTableSize);
    }
    public int hash(String wordSequence) {
        try {
            // Create an instance of MessageDigest, specifying the MD5 hash algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Converts the input string into a byte array and updates the summary
            md.update(wordSequence.getBytes());

            // Get summary results
            byte[] hashBytes = md.digest();

            // Converts a byte array to a hexadecimal string representation
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            int intIndex = Math.abs(hexString.toString().hashCode()) % super.hashTableSize;
            return intIndex;
        } catch (NoSuchAlgorithmException e) {
            // Handling exceptions
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        String input = "Hello, MD5!";

        MD5Hashing md5Hashing = new MD5Hashing(10);
        String md5Hash = String.valueOf(md5Hashing.hash(input));
        System.out.println("MD5 hash for '" + input + "': " + md5Hash);
    }
}
