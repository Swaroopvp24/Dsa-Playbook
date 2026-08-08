public class Solution {
    public int subarraySum(int[] nums, int k) {
        HashMap<Integer, Integer> count = new HashMap<>();
        count.put(0, 1);
        int[] prefix = new int[nums.length];
        prefix[0] = nums[0];
        int ct = 0;

        for (int i = 0; i < nums.length; i++) {
            if (i != 0) {
                prefix[i] = prefix[i - 1] + nums[i];
            }
            int val = prefix[i] - k;
            if (count.containsKey(val)) {
                ct += count.get(val);
            }
            count.put(prefix[i], count.getOrDefault(prefix[i], 0) + 1);
        }
        return ct;
    }
}