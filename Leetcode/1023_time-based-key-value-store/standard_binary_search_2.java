class TimeMap {

    // Maps each key to a list of (timestamp, value) pairs.
    private Map<String, List<Pair<Integer, String>>> keyStore;

    public TimeMap() {
        keyStore = new HashMap<>();
    }

    /**
     * Stores the given value for the key at the specified timestamp.
     */
    public void set(String key, String value, int timestamp) {
        // If the key doesn't exist, create a new list for it.
        keyStore
                .computeIfAbsent(key, k -> new ArrayList<>())
                .add(new Pair<>(timestamp, value));
    }

    /**
     * Returns the value associated with the largest timestamp
     * that is less than or equal to the given timestamp.
     *
     * Returns an empty string if no valid timestamp is found.
     */
    public String get(String key, int timestamp) {

        // Get all timestamp-value pairs for this key.
        // If the key doesn't exist, use an empty list.
        List<Pair<Integer, String>> timestampedValues = keyStore.getOrDefault(key, new ArrayList<>());

        int left = 0;
        int right = timestampedValues.size() - 1;

        // Stores the latest valid value found.
        String result = "";

        // Binary search for the rightmost timestamp <= timestamp.
        while (left <= right) {
            int middle = left + (right - left) / 2;

            int currentTimestamp = timestampedValues.get(middle).getKey();

            if (currentTimestamp <= timestamp) {
                // This timestamp is valid.
                // Store its value and continue searching to the right
                // for a potentially newer valid timestamp.
                result = timestampedValues.get(middle).getValue();
                left = middle + 1;
            } else {
                // Current timestamp is too large.
                // Search the left half.
                right = middle - 1;
            }
        }

        return result;
    }

    /**
     * Simple generic Pair class used to store
     * a timestamp and its corresponding value.
     */
    class Pair<K, V> {

        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
