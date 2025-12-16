/**
 *  First letter's unicode value modulo ‘the hash table size’ as the hash code
 */
public class ModuloHashing extends MyHashFunction{

    public ModuloHashing(int hashTableSize) {
        super(hashTableSize);
    }

    @Override
    public int hash(String wordSequence) {
        // If the input is empty or the length is 0, the default value is returned

        if (wordSequence == null || wordSequence.isEmpty()) {
            return -1;
        }

        // Gets the first character of the word sequence and converts it to lowercase
        char firstLetter = wordSequence.toLowerCase().charAt(0);

        // The Unicode value of the letter modulates the hash table size and returns the result as a hash code
        return (int) firstLetter % super.hashTableSize;
    }

    public static void main(String[] args) {
        // 创建 FirstLetterHash 实例，指定哈希表大小为10
        ModuloHashing moduloHashing = new ModuloHashing(10);

        // 使用 FirstLetterHash 类的 hash 方法生成哈希码并打印结果
        int hash1 = moduloHashing.hash("Hello");
        int hash2 = moduloHashing.hash("Java");
        int hash3 = moduloHashing.hash("Hacker");

        System.out.println("Hash code for hash1: " + hash1);
        System.out.println("Hash code for hash2: " + hash2);
        System.out.println("Hash code for hash3: " + hash3);

    }
}
