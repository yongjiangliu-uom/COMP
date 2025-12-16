// FirstLetterHashFunction.java (Task 2 Requirement: First letter modulo)
public class FirstLetterHashFunction extends MyHashFunction {
    public FirstLetterHashFunction(int size) {
        super(size);
    }

    @Override
    public int hash(String s) {
        if (s == null || s.isEmpty()) return 0;
        char first = s.charAt(0);
        // Ensure non-negative index
        return Math.abs(first % hashTableSize);
    }
}