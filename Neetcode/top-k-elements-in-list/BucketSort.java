class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        int n = nums.length;
        List<Integer>[] freq = new List[nums.length + 1]; // Explanaion detailed required for this
        Map<Integer, Integer> count = new HashMap<>();

        for (int i = 0; i <= n; i++) {
            freq[i] = new ArrayList<>();
        }

        for (int no : nums) {
            count.put(no, count.getOrDefault(no, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            freq[entry.getValue()].add(entry.getKey());
        }

        int[] res = new int[k];
        int index = 0;
        for (int i = freq.length - 1; i > 0; i--) {
            for (int no : freq[i]) {
                res[index++] = no;
                if (index == k) {
                    return res;
                }
            }
        }
        return res;
    }
}
