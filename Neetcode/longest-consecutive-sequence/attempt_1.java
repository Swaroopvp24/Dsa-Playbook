class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> present = new HashSet<>();

        for (int n : nums) {
            present.add(n);
        }
        int maxC = 0;
        int ct = 0;
        for (int n : nums) {
            if(!present.contains(n - 1)) {
                int num = n;
                while (present.contains(num + 1)) {
                    ct++;
                    num++;
                }
            }
            maxC = Math.max(maxC, ct + 1);
            ct = 0;
        }
        return maxC;
    }
}
