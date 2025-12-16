package constants;

public class Constants {
    public static final int MAX_CHAR_LIMIT = 100000;
    public static int HASH_TABLE_SIZE = 10;

    public static enum HashFunctionType {
        SIMPLE_HASH_FUNCTION("Simple Hash Function"),
        POLYNOMIAL_HASH_FUNCTION("Polynomial Hash Function");

        private final String displayName;

        HashFunctionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
