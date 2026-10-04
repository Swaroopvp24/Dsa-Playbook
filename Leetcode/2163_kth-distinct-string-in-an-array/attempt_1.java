class Solution {
    public String kthDistinct(String[] arr, int k) {
        HashMap<String, Integer> frequencyMap = new HashMap<>();

        // Count frequency of each string
        for (String str : arr) {
            frequencyMap.put(
                str,
                frequencyMap.getOrDefault(str, 0) + 1
            );
        }

        int distinctCount = 0;

        // Traverse in original order to find the kth distinct string
        for (String str : arr) {
            if (frequencyMap.get(str) == 1) {
                distinctCount++;

                if (distinctCount == k) {
                    return str;
                }
            }
        }

        return "";
    }
}
