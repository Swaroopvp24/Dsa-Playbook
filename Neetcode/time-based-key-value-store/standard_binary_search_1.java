class TimeValue {
    int timestamp;
    String value;

    TimeValue(int timestamp, String value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}

class TimeMap {
    // Maps each key to a list of values stored along with their timestamps.
    private Map<String, List<TimeValue>> timeMap;

    public TimeMap() {
        timeMap = new HashMap<>();
    }

    /**
     * Stores the given value for a key at the specified timestamp.
     */
    public void set(String key, String value, int timestamp) {
        // Create a new list for the key if it doesn't already exist.
        timeMap
                .computeIfAbsent(key, k -> new ArrayList<>())
                .add(new TimeValue(timestamp, value));
    }

    /**
     * Returns the value associated with the largest timestamp
     * that is less than or equal to the given timestamp.
     *
     * If no such timestamp exists, return an empty string.
     */
    public String get(String key, int timestamp) {
        List<TimeValue> values = timeMap.get(key);

        // Key doesn't exist.
        if (values == null) {
            return "";
        }

        int left = 0;
        int right = values.size() - 1;

        // Stores the index of the latest valid timestamp found.
        int latestValidIndex = -1;

        // Binary search for the rightmost timestamp <= timestamp.
        while (left <= right) {
            int middle = left + (right - left) / 2;

            if (values.get(middle).timestamp <= timestamp) {
                // This timestamp is valid.
                // Try to find a later valid timestamp.
                latestValidIndex = middle;
                left = middle + 1;
            } else {
                // Timestamp is too large, so search on the left side.
                right = middle - 1;
            }
        }

        // No timestamp <= the requested timestamp was found.
        return latestValidIndex == -1
                ? ""
                : values.get(latestValidIndex).value;
    }
}

/**
 * Your TimeMap object will be instantiated and called as such:
 * TimeMap obj = new TimeMap();
 * obj.set(key,value,timestamp);
 * String param_2 = obj.get(key,timestamp);
 */